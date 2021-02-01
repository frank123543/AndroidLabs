package com.cst2335.abc040963564;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout. activity_main_relative);

        Switch sw = findViewById(R.id.switch1);
        ImageButton switch1iButton = findViewById(R.id.imageButton5);
        Button but = findViewById(R.id.button);
        CheckBox chb = findViewById(R.id.checkBox);
        Switch swh = findViewById(R.id.switch1);







        but.setOnClickListener(
                btn -> {
                    Toast.makeText(this, getResources().getString(R.string.toast_message),
                            Toast.LENGTH_LONG).show();//

                });


        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ;
                    Snackbar.make(swh, getResources().getString(R.string.snackbar_on), Snackbar.LENGTH_SHORT)
                            .setAction(getResources().getString(R.string.snackbar_undo), click -> compoundButton.setChecked(!b))
                            .show();
                    //ImageButton.
                }else {
                    Snackbar.make(swh, getResources().getString(R.string.snackbar_off), Snackbar.LENGTH_SHORT)
                            .setAction(getResources().getString(R.string.snackbar_undo), click -> compoundButton.setChecked(!b))
                            .show();
                }}

        });






    } }




