package com.teamness.smane.screens;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.teamness.smane.R;
import com.teamness.smane.interfaces.IDirectionOutput;
import com.teamness.smane.prototype.ButtonDirectionController;

public class MainScreen extends AppCompatActivity {

    private ButtonDirectionController output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        output = new ButtonDirectionController(null); //TODO provide bluetooth thingy


        //Toast.makeText(getApplicationContext(), "That was left, i hope.",
        //Toast.LENGTH_SHORT).show();

        Button left = findViewById(R.id.buttonLeft);
        Button right = findViewById(R.id.buttonRight);

        left.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean pressed) {
                output.setLeftPressed(pressed);
                Toast.makeText(getApplicationContext(), "Left button is "+pressed,
                Toast.LENGTH_SHORT).show();
            }
        });

        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    output.setLeftPressed(true);
                    Toast.makeText(getApplicationContext(), "Left down!",
                            Toast.LENGTH_SHORT).show();
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    output.setLeftPressed(false);
                    Toast.makeText(getApplicationContext(), "Left up!",
                            Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    output.setRightPressed(true);
                    Toast.makeText(getApplicationContext(), "Right down!",
                            Toast.LENGTH_SHORT).show();
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    output.setRightPressed(false);
                    Toast.makeText(getApplicationContext(), "Right up!",
                            Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });


        Thread controllerThread = new Thread(output);
        //controllerThread.start(); //TODO re-enable when ready


    }
}
