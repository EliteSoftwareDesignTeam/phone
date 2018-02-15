package com.teamness.smane.containers;

import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;

/**
 * Created by Aidan on 15/02/2018.
 */

public class TestDirections {
    private static GeoApiContext context;

    public static void main(String[] args) {
        RouteFinder rf = new RouteFinder();

        System.out.println("between points in selly oak");
        DirectionsRoute [] routes = rf.getRouteLatLng(52.4435412, -1.9362401, 52.4437288, -1.9360641);
        printRoutes(routes);

        System.out.println("from Selly oak to university of Birmingham");
        routes = rf.getRouteName("Selly Oak", "Birmingham University");
        printRoutes(routes);

    }

    //Testing method, just prints html
    private static void printRoutes(DirectionsRoute[] routes){
        for(DirectionsRoute route: routes){
            for(DirectionsLeg leg: route.legs){
                for(DirectionsStep step: leg.steps){
                    System.out.println(step.htmlInstructions);
                }
            }
        }
    }

}
