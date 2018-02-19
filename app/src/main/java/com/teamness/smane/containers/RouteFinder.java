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
import com.google.maps.model.EncodedPolyline;
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

    public Route getRouteName(String origin, String destination) {
        DirectionsApiRequest apiRequest = DirectionsApi.newRequest(context);
        apiRequest.origin(origin);
        apiRequest.destination(destination);
        apiRequest.mode(TravelMode.WALKING); //set travelling mode

        return toRoute(getRoute(apiRequest));
    }

    public Route getRouteLatLng(double originLat, double originLong, double destLat, double destLong) {
        DirectionsApiRequest apiRequest = DirectionsApi.newRequest(context);
        apiRequest.origin(new com.google.maps.model.LatLng(originLat, originLong));
        apiRequest.destination(new com.google.maps.model.LatLng(destLat, destLong));
        apiRequest.mode(TravelMode.WALKING); //set travelling mode

        return toRoute(getRoute(apiRequest));
    }

    private Route toRoute(DirectionsRoute[] directionsRoutes) {
        List<RouteNode> nodes = new ArrayList<>();

        for (DirectionsRoute route : directionsRoutes) {
            for (DirectionsLeg leg : route.legs) {
                for (int x = 0; x < leg.steps.length; x++) {
                    DirectionsStep step = leg.steps[x];

                    //TODO make this section

                }
            }
        }
        Route route = new Route(nodes);
        return route;
    }

    private List<RouteNode> processStep(DirectionsStep previousStep, DirectionsStep currentStep, DirectionsStep nextStep) {

        List<RouteNode> nodes = new ArrayList<>();
        String instructions = currentStep.htmlInstructions;

        Location current = newLocation(currentStep.startLocation);
        Location next = new Location("");

        double previousBearing = 0;
        double nextBearing = 0;
        double distance = 0;
        boolean hasNext = false;

        if (nextStep != null) {
            hasNext = true;
            next = newLocation(nextStep.startLocation);

            nextBearing = current.bearingTo(next);
        }

        if (previousStep != null) {
            Location previous = newLocation(previousStep.endLocation);
            previousBearing = previous.bearingTo(current);
            distance = previous.distanceTo(current);
        }

        double bearingChange = getBearingChange(previousBearing, nextBearing);

        nodes.add(new RouteNode(instructions, bearingChange, distance, current));

        double previousDistance = current.distanceTo(next);
        previousBearing = nextBearing;

        List<LatLng> polylines = currentStep.polyline.decodePath();
        for (int i = 0; i < polylines.size(); i++) {
            current = newLocation(polylines.get(i));

            if (i + 1 == polylines.size()) {
                if(hasNext) {
                    next = newLocation(nextStep.startLocation);
                }else{
                    //TODO deal with final step
                }
            } else {
                next = newLocation(polylines.get(i + 1));
            }

            nextBearing = current.bearingTo(next);
            bearingChange = getBearingChange(previousBearing, nextBearing);

            instructions = "Turn: " + bearingChange;
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
        //TODO finish bearing change thing
        double change = 0;
        if (previous < 0 && next < 0) {
            if (next < previous) {
                change = next + previous;
            } else {
                change = next + Math.abs(previous);
            }
        } else if (previous < 0) {
            change = next + Math.abs(previous);
        } else if (next < 0) {
            if (Math.abs(next) < previous) {

            } else {

            }
            change = next - previous;
        } else {
            change = next - previous;
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
