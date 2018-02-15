package com.teamness.smane.containers;

import java.util.List;

/**
 * Represents the entire route produced by Google Maps
 */

public class Route {

    public List<RouteNode> nodes;
    public Position start;
    public Position end;
    public double distance; //Total distance, in meters

    public Route(List<RouteNode> nodes, Position start) {
        this.nodes = nodes;
        this.start = start;
        this.end = nodes.get(nodes.size() - 1).location;
        double totalDistance = 0;
        for (RouteNode node : nodes) {
            totalDistance += node.distanceFromPrevious;
        }
        this.distance = totalDistance;
    }
}
