package com.teamness.smane.containers;

/**
 * Created by MXB551 on 03/02/2018.
 */

public class Position {

    private static final int EarthsRadius = 6371; // Radius of the Earth in km

    public double latitude;
    public double longitude;


    public Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * This is an implementation Haversine Distance Algorithm between two places
     * Adapted from https://bigdatanerd.wordpress.com/2011/11/03/java-implementation-of-haversine-formula-for-distance-calculation-between-two-points/
     * R = earth’s radius (mean radius = 6,371km)
     * Δlat = lat2− lat1
     * Δlong = long2− long1
     * a = sin²(Δlat/2) + cos(lat1).cos(lat2).sin²(Δlong/2)
     * c = 2.atan2(√a, √(1−a))
     * d = R.c
     */
    public double distanceTo(Position pos2) {
        Double latDistance = toRad(pos2.latitude - this.latitude);
        Double lonDistance = toRad(pos2.longitude - this.longitude);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(toRad(this.latitude)) * Math.cos(toRad(pos2.latitude)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EarthsRadius * c;

    }

    private static Double toRad(Double value) {
        return value * Math.PI / 180;
    }


}
