package com.cst2335.abc040963564;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Boolean.TRUE;

public class ChatRoomActivity extends AppCompatActivity {

    private MyListAdapter myAdapter;
    //private ArrayList elements = new ArrayList();
//    final int TYPE_1 = 0;
//    final int TYPE_2 = 1;
    int type;
    private Button sb;
    private Button rb;
    private Context context;
    EditText et;
    private List<Message> mylist = new ArrayList<>();
    SQLiteDatabase db;
    public static final String ITEM_SELECTED = "ITEM";
//    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_TYPE = "TYPE";
    public static final String ITEM_ID = "ID";
    DetailsFragment dFragment;
//    ArrayList<String>   source = new ArrayList<>(Arrays.asList("One", "Two", "Three", "Four"));



    class Message {
        private String text;
        boolean type;
        long id;
        public Message(String text, boolean type,long id){
                this.type = type ;
                this.text = text;
                this.id = id;
        }
        public void setText(String text){this.text = text;}
       public String getText(){return text;}
       public void setType(boolean type){this.type = type;}
       public boolean getType(){return type;}
       public void  setId(long id){this.id = id;}
       public long getId(){return id;}

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        boolean isTablet = findViewById(R.id.fragmentLocation) != null;
        Log.i("isTablet",""+isTablet);
        ListView myList = (ListView) findViewById(R.id.theListView);
        et = findViewById(R.id.edit);
        //load database
        loadDataFromDatabase();
        myAdapter = new MyListAdapter();

        myList.setAdapter(myAdapter = new MyListAdapter());




        ContentValues newRowValues = new ContentValues();
        Button sb = (Button) findViewById(R.id.sent_button);
        sb.setOnClickListener(click -> {
            //Message msg  = new Message(et.getText().toString(), true);
            String text = et.getText().toString();
            et.setText("");
           // list.add(msg);
            // add to the database
            newRowValues.put(MyOpener.COL_TYPE,0);
            newRowValues.put(MyOpener.COL_MESSAGE, text);
            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);
            Message newMessage = new Message(text,true,newId);
            mylist.add(newMessage);
            myAdapter.notifyDataSetChanged();
        });

        Button rb = (Button) findViewById(R.id.receive_button);
        rb.setOnClickListener(click -> {
            String text = et.getText().toString();
            //Message msg = new Message(et.getText().toString(),false);
            et.setText("");
           // list.add(msg);
           //add to database
            newRowValues.put(MyOpener.COL_TYPE,1);
            newRowValues.put(MyOpener.COL_MESSAGE, text);
            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);
            Message newMessage = new Message(text,false,newId);
            mylist.add(newMessage);
            myAdapter.notifyDataSetChanged();
        });

        myList.setOnItemLongClickListener((p, b, pos, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to delete this?")
                    .setMessage("The selected row is:" + (pos+1) + "\n" + "The database id id:" + id)
                    //what the Yes button does:
                    .setPositiveButton("Yes", (click, arg) -> {
                        deleteContact(mylist.get(pos));
                        mylist.remove(pos);
                        //db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(list.get(pos).getId())});
                        myAdapter.notifyDataSetChanged();
                        getSupportFragmentManager().beginTransaction().remove(dFragment).commit();
                    })
                    //What the No button does:
                    .setNegativeButton("No", (click, arg) -> {
                    })
//                   .setView(getLayoutInflater().inflate(R.layout.row_layout, null))
                    .create().show();
            return true;
        });

        myList.setOnItemClickListener((list, item, position, id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, mylist.get(position).getText() );
//            dataToPass.putInt(ITEM_POSITION, position);
            dataToPass.putBoolean(ITEM_TYPE,mylist.get(position).getType());
            dataToPass.putLong(ITEM_ID, id);

            if(isTablet)
            {
                dFragment = new DetailsFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }});
//        SwipeRefreshLayout refresher = findViewById(R.id.refresher);
//        refresher.setOnRefreshListener(() -> refresher.setRefreshing(false));
    }

    protected void deleteContact(Message c)
    {
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(c.getId())});
    }



    private void loadDataFromDatabase()
    {
        //get a database connection:
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer
        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = {MyOpener.COL_ID,MyOpener.COL_TYPE, MyOpener.COL_MESSAGE};
        //query all the results from the database:
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int MESSAGE_index = results.getColumnIndex(MyOpener.COL_MESSAGE);
        int TYPE_index = results.getColumnIndex(MyOpener.COL_TYPE);
        int ID_index = results.getColumnIndex(MyOpener.COL_ID);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            Boolean TYPE2 ;
            //int TYPE = results.getInt(TYPE_index);
            if(results.getInt(TYPE_index) == 0){TYPE2 = true;}
            else {TYPE2 = false;}
            String MESSAGE = results.getString(MESSAGE_index);
            long ID = results.getLong(ID_index);
            //add the new Contact to the array list:
            mylist.add(new Message(MESSAGE,TYPE2, ID));

        }
        printCursor( results, db.getVersion());
        //At this point, the contactsList array has loaded every row from the cursor.
    }

    class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mylist == null ? 0 : mylist.size();
        }

        @Override
        public Object getItem(int position) {
            return mylist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mylist.get(position).getId();
        }



        @Override
        public View getView(int position, View old, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            Message msg = (Message)getItem(position);
            View newView = old;

            //maake a new view
            //if (newView == null) {
            if (msg.getType()) {
                newView = inflater.inflate(R.layout.row_layout, parent, false);
//
            } else if (!(msg.getType())) {
                newView = inflater.inflate(R.layout.arow_layout, parent, false);
//
            }
           // }
            TextView tv = newView.findViewById(R.id.rexit);
            tv.setText(msg.getText());
            return newView; }

    }

    public  void printCursor( Cursor c, int version){
      Log.e("Version", String.valueOf(db.getVersion()));
      Log.e("Number of columns", String.valueOf(c.getColumnCount()));
      for(int i = 0; i < c.getColumnCount(); i++ ){
      Log.e("Name of columns", c.getColumnName(i));}
      Log.e("Numbers of rows", String.valueOf(c.getCount()));

      c.moveToFirst(); while(!c.isAfterLast() ){
            for(int i = 0; i < c.getColumnCount(); i++ ){
                Log.e("Each row of results", c.getString(i));
                 }
            c.moveToNext();
           }
    }
}











