package com.teamness.smane.interfaces;

import android.location.Location;

/**
 * Created by aidan on 17/02/18.
 */

public interface IOrientationProvider {

    /**
     * @return Information about the current orientation of the device
     * Can return null if called before the first reading has taken.
     */
    public double getOrientation();
}
