package com.teamness.smane.screens;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.teamness.smane.R;
import com.teamness.smane.containers.VoiceTTS;
import com.teamness.smane.prototype.ButtonDirectionController;

import java.util.Locale;

public class MainScreen extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private ButtonDirectionController output;
    private VoiceTTS voice = new VoiceTTS();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // check for TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, voice.MY_DATA_CHECK_CODE);

        output = new ButtonDirectionController(voice); //TODO provide bluetooth thingy

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
                    voice.speakWords("Left");
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
                    //voice.speakWords("Right");
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    output.setRightPressed(false);
                    Toast.makeText(getApplicationContext(), "Right up!",
                            Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        Button screenChanger = (Button) findViewById(R.id.screenSwitch1);
        screenChanger.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Prototype2.class);
                startActivityForResult(myIntent, 0);
            }

        });


        Thread controllerThread = new Thread(output);
        controllerThread.start(); //TODO re-enable when ready
    }

    /**
     * TTS setup
     */
    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            if(voice.myTTS.isLanguageAvailable(Locale.UK)==TextToSpeech.LANG_AVAILABLE) voice.myTTS.setLanguage(Locale.UK);
            voice.myTTS.setLanguage(Locale.UK);
        }
        else if (i == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Act on result of TTS data check
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == voice.MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                voice.myTTS = new TextToSpeech(this, this);
            }
            else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }
}
