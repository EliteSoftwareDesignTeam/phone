package containers;

/**
 * Created by MXB551 on 03/02/2018.
 */

public class Position {

    public double latitude;
    public double longitude;

    public Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * TODO: Write a proper distance function, currently a placeholder
     * Returns the distance between this position and an argument, in meters
     * @param pos2 Other position
     * @return Approximate distance between the two points, in meters. Not accurate over long
     *  distances due to Earths curvature.
     */
    public double distanceTo(Position pos2){
        return Math.sqrt(Math.pow((this.latitude - pos2.latitude), 2) + Math.pow((this.longitude - pos2.longitude), 2));
    }


}
