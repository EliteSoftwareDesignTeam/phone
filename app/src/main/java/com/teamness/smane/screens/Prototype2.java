package com.teamness.smane.screens;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.teamness.smane.containers.TextInterpreter;
import com.teamness.smane.R;

import java.util.List;

public class Prototype2 extends AppCompatActivity {

    private static final int SPEECH_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prototype2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button theButton = (Button) findViewById(R.id.thingDoer);
        theButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                displaySpeechRecognizer();
            }

        });
    }

    /**
     *  Call this method to start voice recognition
     */
    private void displaySpeechRecognizer() {
        //System.out.println("starting");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
        //System.out.println("started");

    }

    /**
     * This method receives the result from voice recognition
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        System.out.println("SR: "+"got result");

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            TextInterpreter TI=new TextInterpreter();


            for(int i=0;i<results.size();i++)
            {
                System.out.println("SR: "+results.get(i));
            }

            TI.Interpret(results);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
