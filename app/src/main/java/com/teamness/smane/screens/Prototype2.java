package com.teamness.smane.screens;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.teamness.smane.R;

public class Prototype2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prototype2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button theButton = (Button) findViewById(R.id.thingDoer);
        theButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //TODO do things here
            }

        });
    }

}