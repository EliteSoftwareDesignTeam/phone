package com.teamness.smane.interfaces;

/**
 * Represents a way of communicating a direction to the user, e.g. vibrations, screen text or voice.
 * Cen
 */

public interface IDirectionOutput {

    /**
     * Gives directions to the user. Might only use the first parameter.
     * @param angle Which direction to direct the user to. Expressed in degrees in standard notation,
     *              i.e. positive numbers being left and negative being right. E.g. 90 means to
     *              turn left by 90 degrees, 260 (or -100) to turn 100 degrees right
     * @param strength Strength of the signal. Might be higher for obstacles than regular directions. Likely not all outputs will use this.
     */
    public void giveDirection(double angle, double strength);


}
