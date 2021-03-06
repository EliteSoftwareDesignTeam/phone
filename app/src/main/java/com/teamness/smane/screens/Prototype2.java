package com.teamness.smane.screens;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.teamness.smane.R;
import com.teamness.smane.containers.Command;
import com.teamness.smane.containers.LocationProvider;
import com.teamness.smane.containers.Route;
import com.teamness.smane.containers.RouteFinder;
import com.teamness.smane.containers.TextInterpreter;
import com.teamness.smane.controller.Controller;
import com.teamness.smane.controller.TemporaryBuzzerThingy;
import com.teamness.smane.interfaces.IDirectionOutput;
import com.teamness.smane.interfaces.ITextOutput;
import com.teamness.smane.prototype.CommandOutput;

import java.util.LinkedList;
import java.util.List;

public class Prototype2 extends AppCompatActivity {

    private static final int SPEECH_REQUEST_CODE = 0;
    private Controller controller;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationProvider lp;
    private RouteFinder rf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prototype2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setup location stuff
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        ActivityCompat.requestPermissions(this, new String [] {Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        lp = new LocationProvider(mFusedLocationClient, this);
        rf = new RouteFinder();

        //Give Controller lists of outputs
        List<IDirectionOutput> directionOutputs = new LinkedList<>();
        directionOutputs.add(new CommandOutput());
        directionOutputs.add(new TemporaryBuzzerThingy());
        List<ITextOutput> textOutputs = new LinkedList<>();
        textOutputs.add(new CommandOutput());

        controller = new Controller(directionOutputs,textOutputs,lp);

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

            Command command = TI.Interpret(results);
            switch (command.type){
                case DIRECTIONS:
                    Location currentLoc = lp.getLocation();
                    //Route route = rf.getRouteLatLng(currentLoc.getLatitude(), currentLoc.getLongitude(), 52.450817, -1.930534);
                    System.out.println("Checking directions to "+command.content);
                    Route route = rf.getRoute(currentLoc.getLatitude(), currentLoc.getLongitude(), command.content);
                    controller.startGuiding(route);
                    System.out.println(controller.printRoute());
                    break;
                default:
                    System.err.println("Unimplemented command type "+command.type);
            }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
