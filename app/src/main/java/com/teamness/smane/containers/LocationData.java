package com.teamness.smane.containers;

/**
 * Represents data obtained from GPS, about the position and heading of the device, as well as
 * certainty of the reading, here represented by the radius of a circle which contains the true
 * position with reasonable certainty
 */

public class LocationData {

    public Position position;
    public double heading;
    public double radius;

    public LocationData(Position position, double heading, double radius) {
        this.position = position;
        this.heading = heading;
        this.radius = radius;
    }

    public LocationData(double latitude, double longitude, double heading, double radius) {
        this.position = new Position(latitude, longitude);
        this.heading = heading;
        this.radius = radius;
    }

    /**
     * Checks whether this location contains a specified point within its radius
     * @param point Point to test
     */
    public boolean contains(Position point){
        return this.position.distanceTo(point) < this.radius;
    }


    public double getLatitude(){
        return this.position.latitude;
    }
    public double getLongitude(){
        return this.position.longitude;
    }
    public double getHeading() {
        return heading;
    }
    public Position getPosition() {
        return position;
    }
    public double getRadius() {
        return radius;
    }
}
