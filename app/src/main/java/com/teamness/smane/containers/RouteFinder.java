package com.teamness.smane.containers;

import android.location.Location;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aidan on 15/02/2018.
 */

public class RouteFinder {
    private GeoApiContext context;

    public RouteFinder() {
        context = new GeoApiContext.Builder()
                .apiKey("AIzaSyBoha6CIWKg0vA68Ca3_ZMnVpzt582KP3Q")
                .build();

        System.out.println("key setup complete");
    }

    public Route getRoute(double originLat, double originLong, String destination){
        DirectionsApiRequest apiRequest = DirectionsApi.newRequest(context);
        apiRequest.origin(new com.google.maps.model.LatLng(originLat, originLong));
        apiRequest.destination(destination);
        apiRequest.mode(TravelMode.WALKING);

        return toRoute(getRoute(apiRequest));
    }

    public Route getRoute(String origin, String destination) {
        DirectionsApiRequest apiRequest = DirectionsApi.newRequest(context);
        apiRequest.origin(origin);
        apiRequest.destination(destination);
        apiRequest.mode(TravelMode.WALKING);

        return toRoute(getRoute(apiRequest));
    }

    public Route getRoute(double originLat, double originLong, double destLat, double destLong) {
        DirectionsApiRequest apiRequest = DirectionsApi.newRequest(context);
        apiRequest.origin(new com.google.maps.model.LatLng(originLat, originLong));
        apiRequest.destination(new com.google.maps.model.LatLng(destLat, destLong));
        apiRequest.mode(TravelMode.WALKING);

        return toRoute(getRoute(apiRequest));
    }

    private Route toRoute(DirectionsRoute[] directionsRoutes) {
        List<RouteNode> nodes = new ArrayList<>();

        for (DirectionsRoute route : directionsRoutes) {
            for (DirectionsLeg leg : route.legs) {
                for (int x = 0; x < leg.steps.length; x++) {
                    DirectionsStep previousStep = null;
                    DirectionsStep currentStep = leg.steps[x];
                    DirectionsStep nextStep = null;

                    if (x < leg.steps.length - 1) {
                        nextStep = leg.steps[x + 1];
                    }

                    if (x > 0) {
                        previousStep = leg.steps[x - 1];
                    }

                    nodes.addAll(processStep(previousStep, currentStep, nextStep));
                }
            }
        }

        Route route = new Route(nodes);
        return route;
    }

    private List<RouteNode> processStep(DirectionsStep previousStep, DirectionsStep currentStep, DirectionsStep nextStep) {
        List<RouteNode> nodes = new ArrayList<>();

        Location previous;
        Location current;
        Location next;

        String instructions = currentStep.htmlInstructions;
        double previousBearing = 0;
        double bearingChange = 0;
        double previousDistance = 0;

        List<LatLng> polylines = currentStep.polyline.decodePath();
        for (int i = 0; i < polylines.size(); i++) {
            current = newLocation(polylines.get(i));

            if (i + 1 == polylines.size()) {
                if (nextStep != null) {
                    next = newLocation(nextStep.startLocation);
                } else {
                    //there is no next, just use current instead
                    next = current;
                }
            } else {
                next = newLocation(polylines.get(i + 1));

                if (i == 0 && previousStep != null) {
                    previous = newLocation(previousStep.endLocation);
                    previousBearing = toRadianBearing(previous.bearingTo(current));
                    previousDistance = previous.distanceTo(previous);
                } else {
                    instructions = "Turn: " + bearingChange;
                }
            }

            double nextBearing = toRadianBearing(current.bearingTo(next));
            bearingChange = getBearingChange(previousBearing, nextBearing);

            nodes.add(new RouteNode(instructions, bearingChange, previousDistance, current));

            previousDistance = current.distanceTo(next);
            previousBearing = nextBearing;
        }

        return nodes;
    }

    private Location newLocation(LatLng currentLatLng) {
        Location location = new Location("");

        location.setLatitude(currentLatLng.lat);
        location.setLongitude(currentLatLng.lng);

        return location;
    }

    private double toRadianBearing(double bearing) {
        double rad = Math.toRadians(bearing);
        if (rad > Math.PI) {
            rad = -2 * Math.PI + rad;
        }
        return rad;
    }

    private double getBearingChange(double previous, double next) {
        double change = next - previous;

        if(change > Math.PI){
            change -= 2* Math.PI;
        }else if (change <= - Math.PI){
            change += 2*Math.PI;
        }

        return change;
    }

    private DirectionsRoute[] getRoute(DirectionsApiRequest apiRequest) {
        DirectionsRoute[] routes = null;
        try {
            DirectionsResult result = apiRequest.await();
            routes = result.routes;

        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return routes;
    }
}
