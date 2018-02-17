package com.teamness.smane.containers;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

/**
 * Created by aidan on 17/02/18.
 */

public class GPS extends Activity {
    private FusedLocationProviderClient mFusedLocationClient;

    public GPS(FusedLocationProviderClient mFusedLocationClient) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    public Location getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Need permissions");
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }

        Task t = mFusedLocationClient.getLastLocation();

        return (Location) t.getResult();
    }
}
