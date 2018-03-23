package com.teamness.smane.containers;

import android.location.Location;

import java.util.List;

/**
 * Represents the entire route produced by Google Maps
 */

public class Route {

    public List<RouteNode> nodes;
    public Location start;
    public Location end;
    public double distance; //Total distance, in meters

    public Route(List<RouteNode> nodes) {
        this.nodes = nodes;
        this.start = nodes.get(0).location;
        this.end = nodes.get(nodes.size() - 1).location;
        double totalDistance = 0;
        for (RouteNode node : nodes) {
            totalDistance += node.distanceFromPrevious;
        }
        this.distance = totalDistance;
    }
}
