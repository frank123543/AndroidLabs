package com.cst2335.abc040963564;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs = null;

    EditText et1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_linear);
        et1 = findViewById(R.id.editText1);
        prefs = getSharedPreferences("filename", Context.MODE_PRIVATE);
        String savedString = prefs.getString("ReserveName", "");
        et1.setText(savedString);


        Button b = findViewById(R.id.button2);
        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String et1Str = et1.getText().toString();
                Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);
                goToProfile.putExtra("et1", et1Str);
                startActivity(goToProfile);

    }
    });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();

        saveSharedPrefs(et1.getText().toString());
    }

        private void saveSharedPrefs (String stringToSave){
            prefs = getSharedPreferences("filename", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("ReserveName", stringToSave);
            editor.commit();
        }



    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}

