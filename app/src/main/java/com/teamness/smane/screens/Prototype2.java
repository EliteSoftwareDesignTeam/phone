package com.teamness.smane.screens;

import android.Manifest;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.teamness.smane.R;
import com.teamness.smane.containers.LocationProvider;
import com.teamness.smane.containers.OrientationProvider;

public class Prototype2 extends AppCompatActivity {
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        final SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prototype2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button theButton = (Button) findViewById(R.id.thingDoer);
        final LocationProvider lp = new LocationProvider(mFusedLocationClient, this);

        final OrientationProvider op = new OrientationProvider(sensorManager);

        theButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                op.getOrientation();
            }

        });

        onResume();
    }

}
