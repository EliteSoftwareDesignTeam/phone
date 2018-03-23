package com.teamness.smane.event;

/**
 * Created by mac15001900 on 3/3/18.
 */

public class BuzzerEvent extends Event {
    public double direction;
    public double strength;
    public double length; //In seconds

    public BuzzerEvent(double direction, double strength, double length) {
        this.direction = direction;
        this.strength = strength;
        this.length = length;
    }
}
