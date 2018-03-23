package com.teamness.smane.containers;

import android.location.Location;

/**
 * Represents a single significant point in a route, i.e. a point when direction is delivered to the user
 */

public class RouteNode {

    public String instruction;
    /**
     * The change in angle the user should be facing, after crossing this node.
     * In radians, standard notation (positive is left, negative is right).
     * For example, if the node is an intersection and we need to turn left, the value would be PI/2
     * <p>
     * For the very first node, assume the user is facing exactly north
     */
    public double angleChange;
    public double distanceFromPrevious; //In meters

    public Location location;

    public RouteNode(String instruction, double angleChange, double distanceFromPrevious, Location location) {
        this.instruction = instruction;
        this.angleChange = angleChange;
        this.distanceFromPrevious = distanceFromPrevious;
        this.location = location;
    }
}
