package com.teamness.smane.controller;


import android.location.Location;

import com.teamness.smane.containers.Route;
import com.teamness.smane.containers.RouteNode;
import com.teamness.smane.interfaces.IDirectionOutput;
import com.teamness.smane.interfaces.ILocationProvider;
import com.teamness.smane.interfaces.ITextOutput;

import java.util.List;

public class Controller implements Runnable {

    //Distance at which giving directions will trigger, in meters. Temporary, to be replaced with a confidence-based system
    private final double TRIGGER_DISTANCE = 2;

    private List<IDirectionOutput> directionOutputs;
    private List<ITextOutput> textOutputs;
    private ILocationProvider gps;
    private Route route;

    private boolean active = false;
    private Location location;
    private double confidence; //TODO Take confidence into account, deal with unreliable GPS
    private int step;

    private Thread guidingThread;

    /**
     * Creates a new controller. It takes some output devices and will use all of them to communicate with the user.
     *
     * @param directionOutputs All devices which provide directions in the form of left and right
     * @param textOutputs      All devices that output text to the user.
     * @param gps              A device that provides the user's location.
     */
    public Controller(List<IDirectionOutput> directionOutputs, List<ITextOutput> textOutputs, ILocationProvider gps) {
        this.directionOutputs = directionOutputs;
        this.textOutputs = textOutputs;
        this.gps = gps;
    }

    /**
     * Start guiding the user towards a destination. This will create a new thread to handle the
     * guiding process.
     *
     * @param directions The route to guide the user through. See {@link com.teamness.smane.containers.Route} for more details.
     */
    public void startGuiding(Route directions) {
        //Update state
        active = true;
        route = directions;
        step = 0;

        //Get current location date
        location = gps.getLocation();
        double heading = location.getBearing(); //TODO check if this is the correct function

        //Update the heading on first node given user's current orientation
        route.nodes.get(0).angleChange = normaliseAngle(route.nodes.get(0).angleChange - heading);

        //Give details about the journey (might get expanded later)
        say("Your journey will take " + prettifyDistance(route.distance));

        //Start guiding
        triggerNextNode();
        guidingThread = new Thread(this);
        (guidingThread).start();
    }

    /**
     * Cancels guiding and kills the thread that handles it.
     */
    public void cancelGuiding() {
        guidingThread.interrupt();
        active = false;
        //TODO Save data to file, to resume later
    }

    /**
     * Call after guiding was interrupted but the user wants to resume. Currently will only work
     * if the app was not closed.
     */
    public void resumeGuiding() {
        active = true;
        location = gps.getLocation();
        guidingThread = new Thread(this);
        (guidingThread).start();
    }

    /**
     * Repeats the previous instruction, in the same way as it was originally communicated.
     */
    public void repeatInstruction() {
        step--;
        triggerNextNode();
    }

    public String printRoute(){
        StringBuilder res = new StringBuilder();
        for (RouteNode node : route.nodes) {
            res.append(node.location.getLatitude()).append(", ").append(node.location.getLongitude())
                    .append(" - ").append(node.instruction).append("\n");
        }
        return res.toString();
    }

    /**
     * @return The node the user is currently heading towards
     */
    private RouteNode nextNode() {
        if (step >= route.nodes.size())
            throw new IndexOutOfBoundsException("Controller internal error, attempted accessing non-existing route node.");
        return route.nodes.get(step);
    }

    /**
     * Triggers next node, giving directions associated with it.
     */
    private void triggerNextNode() {
        RouteNode current = nextNode();
        step++;
        say(current.instruction);
        giveDirection(Math.toDegrees(current.angleChange), 1);
    }

    /**
     * Uses all voice outputs to communicate a message to the user
     */
    private void say(String message) {
        for (ITextOutput output : textOutputs) {
            output.sendMessage(message);
        }
    }

    /**
     * Uses all direction outputs to communicate a change in walking direction to the user.
     * For parameter clarification refer to {@link com.teamness.smane.interfaces.IDirectionOutput}.
     */
    private void giveDirection(double direction, double strength) {
        for (IDirectionOutput output : directionOutputs) {
            output.giveDirection(direction, strength);
        }
    }


    @Override
    public void run() {
        try {
            while (step < route.nodes.size()) {
                Thread.sleep(100);
                //Update location
                location = gps.getLocation();
                //Check for triggering directions
                if (location.distanceTo(nextNode().location) < TRIGGER_DISTANCE) {
                    triggerNextNode();
                }
            }
        } catch (InterruptedException e) {
            active = false;
        }
    }

    /**
     * Turns a raw distance into a String to be said to the user
     *
     * @param meters Distance in meters
     */
    private String prettifyDistance(double meters) {
        int km = (int) meters / 1000;
        int m = (int) meters % 1000;
        if (meters < 1000) return m + " meters";
        if (meters < 3000) return km + " kilometers and " + m + " meters";
        if (meters < 10000) return km + "kilometers and " + round(m, -2) + " meters";
        else return km + " kilometers";
    }

    /**
     * Rounds a number to a specified amount of digits after the dot
     *
     * @param number Number to round
     * @param digits How many digits after the dot should be kept. Negative arguments mean
     *               rounding even more, e.g. round(1234,-1) = 1230
     */
    private double round(double number, int digits) {
        return Math.round(number * Math.pow(10, digits)) / Math.pow(0.1, digits);
    }

    /**
     * Normalises an angle (e.g. turn 3.88PI into 1.88PI, or -0.5PI into 1.5PI)
     */
    private double normaliseAngle(double angle) {
        while (angle >= Math.PI * 2) angle -= Math.PI * 2;
        while (angle < 0) angle += Math.PI * 2;
        return angle;
    }

}