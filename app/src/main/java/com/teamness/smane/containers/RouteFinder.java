package com.teamness.smane.containers;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;

import java.io.IOException;

/**
 * Created by Aidan on 15/02/2018.
 */

public class RouteFinder {
    private GeoApiContext context;

    public RouteFinder (){
        context = new GeoApiContext.Builder()
                .apiKey("AIzaSyBoha6CIWKg0vA68Ca3_ZMnVpzt582KP3Q")
                .build();

        System.out.println("key setup complete");
    }

    public DirectionsRoute[] getRouteName(String origin, String destination){
        DirectionsApiRequest apiRequest = DirectionsApi.newRequest(context);
        apiRequest.origin(origin);
        apiRequest.destination(destination);
        apiRequest.mode(TravelMode.WALKING); //set travelling mode

        return getRoute(apiRequest);
    }

    public DirectionsRoute [] getRouteLatLng(double originLat, double originLong, double destLat, double destLong){
        DirectionsApiRequest apiRequest = DirectionsApi.newRequest(context);
        apiRequest.origin(new com.google.maps.model.LatLng(originLat, originLong));
        apiRequest.destination(new com.google.maps.model.LatLng(destLat, destLong));
        apiRequest.mode(TravelMode.WALKING); //set travelling mode

        return getRoute(apiRequest);
    }

    private DirectionsRoute [] getRoute(DirectionsApiRequest apiRequest){
        DirectionsRoute [] routes = null;
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
