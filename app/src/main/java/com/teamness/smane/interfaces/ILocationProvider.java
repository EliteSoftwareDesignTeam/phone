package com.teamness.smane.interfaces;

import android.location.Location;

/**
 * Provides a location of the device.
 */

public interface ILocationProvider {
    /**
     * Provides access to GPS information. In general hasNewData() should be called first.
     * @return Information about the current location of the device
     * Can return null if called before the first reading has taken.
     */
    public Location getLocation();

    /**
     * Checks whether new data has arrived since the last call of getLocation()
     * @return True if new data has arrived since last call of getLocation, false otherwise. Also
     *  false if not data was received since the app was started.
     */
    public boolean hasNewData();

    /**
     * Checks whether the GPS network is currently available.
     */
    public boolean hasSignal();

    /**
     * Check how much time elapsed since data was last received.
     * @return The amount of time passed since last update from GPS, in seconds.
     */
    public double timeSinceLastData();

}
