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
    private SensorListener sensorListener;

    public OrientationProvider(SensorManager sensorManager) {
        Sensor magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Sensor accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorListener = new SensorListener();

        //adds the listener
        sensorManager.registerListener(sensorListener, magSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public double getOrientation(){
        //this probably doesn't work
        System.out.println("azi: " + sensorListener.getAzimuth());
        System.out.println("pit: " + sensorListener.getPitch());
        System.out.println("rol: " + sensorListener.getRoll());
        return sensorListener.getAzimuth();
    }
}
