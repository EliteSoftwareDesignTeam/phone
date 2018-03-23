package com.teamness.smane.event;

public class DistanceEvent extends Event {
    public final int distance;

    public DistanceEvent(int distance) {
        this.distance = distance;
    }
}
