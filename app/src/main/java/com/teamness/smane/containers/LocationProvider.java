package com.teamness.smane.containers;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.teamness.smane.interfaces.ILocationProvider;

/**
 * Created by aidan on 17/02/18.
 */

public class LocationProvider extends Activity implements ILocationProvider {
    private FusedLocationProviderClient mFusedLocationClient;

    public LocationProvider(FusedLocationProviderClient mFusedLocationClient) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    public Location getLocation() {
        checkPermission();

        Task<Location> t = mFusedLocationClient.getLastLocation();

        return t.getResult();
    }

    @Override
    public boolean hasNewData() {
        checkPermission();
        Task<LocationAvailability> t = mFusedLocationClient.getLocationAvailability();

        return t.getResult().isLocationAvailable();
    }

    @Override
    public boolean hasSignal() {
        return false;
    }

    @Override
    public double timeSinceLastData() {
        return 0;
    }

    private void checkPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Need permissions");
            // TODO: Manage case where permissions not given
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
    }
}
