package com.teamness.smane.containers;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by aidan on 17/02/18.
 */

public class SensorListener implements SensorEventListener {

    private float[] inR = new float[16];
    private float[] I = new float[16];
    private float[] gravity = new float[3];
    private float[] geomag = new float[3];
    private float[] orientVals = new float[3];

    private double azimuth = 0;
    private double pitch = 0;
    private double roll = 0;

    public void onSensorChanged(SensorEvent sensorEvent) {
        // If the sensor data is unreliable return
        if (sensorEvent.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
            return;

        // Gets the value of the sensor that has been changed
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                gravity = sensorEvent.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                geomag = sensorEvent.values.clone();
                break;
        }

        // If gravity and geomag have values then find rotation matrix
        if (gravity != null && geomag != null) {

            // checks that the rotation matrix is found
            boolean success = SensorManager.getRotationMatrix(inR, I,
                    gravity, geomag);
            if (success) {
                SensorManager.getOrientation(inR, orientVals);
                azimuth = orientVals[0];
                pitch = orientVals[1];
                roll = orientVals[2];
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //TODO
    }

    public double getAzimuth() {
        return azimuth;
    }

    public double getPitch() {
        return pitch;
    }

    public double getRoll() {
        return roll;
    }
}

