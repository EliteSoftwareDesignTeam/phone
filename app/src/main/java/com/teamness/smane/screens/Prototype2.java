package com.teamness.smane.screens;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.teamness.smane.R;
import com.teamness.smane.bluetooth.CaneBluetooth;
import com.teamness.smane.containers.Command;
import com.teamness.smane.containers.LocationProvider;
import com.teamness.smane.containers.Route;
import com.teamness.smane.containers.RouteFinder;
import com.teamness.smane.containers.TextInterpreter;
import com.teamness.smane.containers.VoiceTTS;
import com.teamness.smane.controller.Controller;
import com.teamness.smane.controller.TemporaryBuzzerThingy;
import com.teamness.smane.event.ButtonEvent;
import com.teamness.smane.event.CaneEvents;
import com.teamness.smane.interfaces.IDirectionOutput;
import com.teamness.smane.interfaces.ITextOutput;
import com.teamness.smane.prototype.CommandOutput;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class Prototype2 extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final int SPEECH_REQUEST_CODE = 42;
    private static final int SYNTHESIS_REQUEST_CODE = 24601;
    private Controller controller;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationProvider lp;
    private RouteFinder rf;
    private List<IDirectionOutput> directionOutputs;
    private List<ITextOutput> textOutputs;
    private VoiceTTS voice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            new CaneBluetooth().init();
            CaneEvents.BT_IN.on(ButtonEvent.class, "onCaneButtonPressed", this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prototype2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setup TTS
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, SYNTHESIS_REQUEST_CODE);

        //Setup location stuff
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        lp = new LocationProvider(mFusedLocationClient, this);
        rf = new RouteFinder();

        //Give Controller lists of outputs
        List<IDirectionOutput> directionOutputs = new LinkedList<>();
        directionOutputs.add(new CommandOutput());
        directionOutputs.add(new TemporaryBuzzerThingy());
        List<ITextOutput> textOutputs = new LinkedList<>();
        textOutputs.add(new CommandOutput());
        //if (voice == null) throw new RuntimeException("Not again...");
        //textOutputs.add(voice);


        controller = new Controller(directionOutputs, textOutputs, lp);

        //Handle main button
        Button theButton = (Button) findViewById(R.id.thingDoer);
        theButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                displaySpeechRecognizer();
            }
        });

        //Handle next step button
        Button nextButton = (Button) findViewById(R.id.nextStep);
        nextButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                controller.triggerNextNode();
            }
        });

    }

    public void onCaneButtonPressed(ButtonEvent event) {
        // TODO
    }

    /**
     * Call this method to start voice recognition
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
        System.out.println("Results received: " + requestCode);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            TextInterpreter TI = new TextInterpreter();


            for (int i = 0; i < results.size(); i++) {
                System.out.println("SR: " + results.get(i));
            }

            Command command = TI.Interpret(results);
            switch (command.type) {
                case DIRECTIONS:
                    Location currentLoc = lp.getLocation();
                    //Route route = rf.getRouteLatLng(currentLoc.getLatitude(), currentLoc.getLongitude(), 52.450817, -1.930534);
                    System.out.println("Checking directions to " + command.content);
                    Route route = rf.getRoute(currentLoc.getLatitude(), currentLoc.getLongitude(), command.content);
                    if (route != null && route.nodes.size() > 0) {
                        controller.startGuiding(route);
                        System.out.println(controller.printRoute());
                    } else {
                        for (ITextOutput textOutput : textOutputs) {
                            textOutput.sendMessage("Sorry, I could not find a path to " + command.content);
                        }
                    }
                    break;
                case TEST_CONNECTION:
                    for (ITextOutput textOutput : textOutputs) {
                        //TODO Test connection
                        textOutput.sendMessage("I would tell you whether the bluetooth connection is working but I will need Sam to get that to work.");
                    }
                    break;
                default:
                    for (ITextOutput textOutput : textOutputs) {
                        textOutput.sendMessage("Command not recognised. Please try again.");
                    }
                    System.err.println("Unimplemented command type " + command.type);
            }

        } else if (requestCode == SYNTHESIS_REQUEST_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                voice = new VoiceTTS(this, this);
                //voice.myTTS = new TextToSpeech(this, this);
            } else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onInit(int i) {
        System.err.println("onInit called with " + i);
        if (i == TextToSpeech.SUCCESS) {
            if (voice.myTTS.isLanguageAvailable(Locale.UK) == TextToSpeech.LANG_AVAILABLE)
                voice.myTTS.setLanguage(Locale.UK);
            voice.myTTS.setLanguage(Locale.UK);
            textOutputs.add(voice);
        } else if (i == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

}
