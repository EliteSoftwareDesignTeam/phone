package com.teamness.smane.containers;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.teamness.smane.interfaces.ILocationProvider;
import com.teamness.smane.interfaces.IOrientationProvider;

/**
 * Created by aidan on 17/02/18.
 */

public class OrientationProvider implements IOrientationProvider{

    public double getOrientation(){
        //TODO get orientation code
        return 0;
    }
}
