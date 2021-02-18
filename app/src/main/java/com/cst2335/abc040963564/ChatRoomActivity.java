package com.cst2335.abc040963564;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    private MyListAdapter myAdapter;
    //private ArrayList elements = new ArrayList();
    final int TYPE_1 = 0;
    final int TYPE_2 = 1;
    int type;
    private Button sb;
    private Button rb;
    private Context context;
    EditText et;
    private List<Message> list = new ArrayList<>();


    class Message {
        private String text;
        int gender;
        public Message(String text, int gender){
                this.gender = gender;
                this.text = text;
        }
       public void setText(String text){this.text = text;}
       public String getText(){return text;}
       public void setGender(int gender){this.gender = gender;}
       public int getGender(){return gender;}
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        myAdapter = new MyListAdapter();
        ListView myList = (ListView) findViewById(R.id.theListView);
        myList.setAdapter(myAdapter = new MyListAdapter());
        et = findViewById(R.id.edit);

        Button sb = (Button) findViewById(R.id.sent_button);
        sb.setOnClickListener(click -> {
            Message msg  = new Message(et.getText().toString(), 0);
            et.setText("");
            list.add(msg);
            myAdapter.notifyDataSetChanged();
        });

        Button rb = (Button) findViewById(R.id.receive_button);
        rb.setOnClickListener(click -> {
            Message msg = new Message(et.getText().toString(),1);
            et.setText("");
            list.add(msg);
            myAdapter.notifyDataSetChanged();
        });

        myList.setOnItemLongClickListener((p, b, pos, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want to delete this?")
                    .setMessage("The selected row is:" + (pos+1) + "\n" + "The database id id:" + id)
                    //what the Yes button does:
                    .setPositiveButton("Yes", (click, arg) -> {
                        list.remove(pos);
                        myAdapter.notifyDataSetChanged();
                    })
                    //What the No button does:
                    .setNegativeButton("No", (click, arg) -> {
                    })
//                   .setView(getLayoutInflater().inflate(R.layout.row_layout, null))
                    .create().show();
            return true;
        });


        SwipeRefreshLayout refresher = findViewById(R.id.refresher);
        refresher.setOnRefreshListener(() -> refresher.setRefreshing(false));
    }

    class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long) position;
        }



        @Override
        public View getView(int position, View old, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            Message msg = (Message)getItem(position);
            View newView = old;

            //maake a new view
            //if (newView == null) {
                switch (msg.getGender()) {
                    case 0:
                         newView = inflater.inflate(R.layout.row_layout, parent, false);
//                         TextView tView = newView.findViewById(R.id.textview_row);
//                         tView.setText(et.getText());
                        break;

                    case 1:
                        newView = inflater.inflate(R.layout.arow_layout, parent, false);
//                        TextView atView = newView.findViewById(R.id.textview_arow);
//                        atView.setText(et.getText());
                        break;
                }
           // }
            TextView tv = newView.findViewById(R.id.rexit);
            tv.setText(msg.getText());
            return newView; }

    }
}











