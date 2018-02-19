package com.teamness.smane.screens;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.teamness.smane.R;
import com.teamness.smane.containers.LocationProvider;
import com.teamness.smane.containers.Route;
import com.teamness.smane.containers.RouteFinder;
import com.teamness.smane.containers.RouteNode;

public class Prototype2 extends AppCompatActivity {
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        ActivityCompat.requestPermissions(this, new String [] {Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prototype2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final Button theButton = (Button) findViewById(R.id.thingDoer);
        final LocationProvider lp = new LocationProvider(mFusedLocationClient, this);

        theButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RouteFinder rf = new RouteFinder();

                Location currentLoc = lp.getLocation();

                Route route = rf.getRouteLatLng(currentLoc.getLatitude(), currentLoc.getLongitude(), 52.450817, -1.930534);
                System.out.println("distance: " + route.distance);
                System.out.println();
                for(RouteNode node: route.nodes){
                    System.out.println(node.instruction);
                }

            }

        });
    }

}
