package SimConnection;
import SimUtilities.*;

/******************************************************
 * This class abstracts the TCL commands to get the
 * location of the subject vehicle.
 *
 *@author Tom Miller
 *@version 1.0
 *@since 7/14/2015
 */
public class SIMPosition extends SIMFeedback  {
    private SIMConnection simConnection;
    private double x;
    private double y;
    private double z;
    private int xid;
    private int yid;
    private int zid;

    /*******************************************
     * The constructor
     *
     * @param sc The simulator connection object
     ********************************************/
    public SIMPosition(SIMConnection sc) {
        simConnection = sc;
        x = 0;
        xid = 0;
        y = 0;
        yid = 0;
        z = 0;
        zid = 0;
    }

    /*******************************************
     * This method queues a command to get the
     * location of the subject vehicle.
     *
     ********************************************/
    public void get() {
        // get the car location
        xid = simConnection.getVariable("SubjectX", this);
        yid = simConnection.getVariable("SubjectY", this);
        zid = simConnection.getVariable("SubjectZ", this);
    }

    /*******************************************
     * This method updates the location
     * of the subject vehicle, from the simulator
     * feedback. (invoked automatically)
     *
     ********************************************/
    public void update() {
        // velocity in meters per second
        x = simConnection.doubleValueOf(xid, 0.0);
        y = simConnection.doubleValueOf(yid, 0.0);
        z = simConnection.doubleValueOf(zid, 0.0);
    }

    /*******************************************
     * This method returns the x location of the
     * subject vehicle as set by the last call
     * to the update() method.
     *
     * @return The value of the x location in meters.
     ********************************************/
    public double xvalue() {
        // location in meters
        return x;
    }

    /*******************************************
     * This method returns the y location of the
     * subject vehicle as set by the last call
     * to the update() method.
     *
     * @return The value of the y location in meters.
     ********************************************/
    public double yvalue() {
        // location in meters
        return y;
    }

    /*******************************************
     * This method returns the z location of the
     * subject vehicle as set by the last call
     * to the update() method.
     *
     * @return The value of the z location in meters.
      ********************************************/
    public double zvalue() {
        // velocity in meters per second
        return z;
    }

    /*******************************************
     * This method returns the location of the
     * subject vehicle as set by the last call
     * to the update() method.
     *
     * @return The value of the x,y,z location in meters.
     ********************************************/
    public Location value() {
        // velocity in meters per second
        return new Location(x, y, z);
    }

}
