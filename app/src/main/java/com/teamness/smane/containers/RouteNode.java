package com.teamness.smane.containers;

/**
 * Represents a single significant point in a route, i.e. a point when direction is delivered to the user
 */

public class RouteNode {

    public String instruction;
    public double angleChange; //In radians, standard notation (positive is left, negative is right)
    public double distanceFromPrevious; //In meters

    public Position location;

    public RouteNode(String instruction, double angleChange, double distanceFromPrevious, Position location) {
        this.instruction = instruction;
        this.angleChange = angleChange;
        this.distanceFromPrevious = distanceFromPrevious;
        this.location = location;
    }
}
