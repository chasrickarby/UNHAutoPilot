package SimUtilities;
import java.lang.Math;

/******************************************************
 * This class represents an x,y,z location in the simulated
 * world (in meters).
 *
 *@author Tom Miller
 *@version 1.0
 *@since 7/14/2015
 */
public class Location {
    private double x;
    private double y;
    private double z;

    /*******************************************
     * The constructor
     *
     * @param xi x coordinate
     * @param yi y coordinate
     * @param zi z coordinate
     ********************************************/
    public Location(double xi, double yi, double zi) {
        x = xi;
        y = yi;
        z = zi;
    }

    /*******************************************
     * This method changes the coordinates of a
     * the location object.
     *
     * @param xi x coordinate
     * @param yi y coordinate
     * @param zi z coordinate
     ********************************************/
    public void set(double xi, double yi, double zi) {
        x = xi;
        y = yi;
        z = zi;
    }

    /*******************************************
     * This method returns the x location.
     *
     * @return The value of the location in meters.
     ********************************************/
    public double xvalue() {
        // location in meters
        return x;
    }

    /*******************************************
     * This method returns the y location.
     *
     * @return The value of the location in meters.
     ********************************************/
    public double yvalue() {
        // location in meters
        return y;
    }

    /*******************************************
     * This method returns the z location.
     *
     * @return The value of the location in meters.
     ********************************************/
    public double zvalue() {
        // velocity in meters per second
        return z;
    }

    /*******************************************
     * This method returns the same location in
     * a new object.
     *
     * @return The value of the x,y,z location in meters.
     ********************************************/
    public Location value() {
        // velocity in meters per second
        return new Location(x, y, z);
    }

    /*******************************************
     * This method returns the planar distance
     * in meters to this location from another location.
     * The Z axis is ignored.
     *
     * @return The distance in meters.
     ********************************************/
    public double distancefrom(Location refloc) {
        // compute the distance
        double xd = (x - refloc.xvalue());
        double yd = (y - refloc.yvalue());
        return Math.sqrt((xd*xd) + (yd*yd));
    }

    /*******************************************
     * This method returns the compass direction
     * in degrees to this location from another
     * location. Y is north (0 degrees), X is east
     * (90 degrees). The Z axis is ignored.
     *
     * @return The direction in degrees: 0 to 360.
     ********************************************/
    public double directionfrom(Location refloc) {
        // compute the distance
        double xd = (x - refloc.xvalue());
        double yd = (y - refloc.yvalue());
        double dir = Math.toDegrees(Math.acos(yd / Math.sqrt((xd*xd) + (yd*yd))));
        if (xd >= 0.0) {
            return dir;
        } else {
            return 360.0 - dir;
        }
    }
}
