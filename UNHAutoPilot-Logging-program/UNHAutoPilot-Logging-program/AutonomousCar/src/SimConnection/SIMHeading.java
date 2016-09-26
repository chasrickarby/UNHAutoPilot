package SimConnection;
import java.lang.Math;

/******************************************************
 * This class abstracts the TCL command to get the
 * heading of the subject vehicle.
 *
 *@author Tom Miller
 *@version 1.0
 *@since 7/14/2015
 */
public class SIMHeading extends SIMFeedback {
    private SIMConnection simConnection;
    private double heading;
    private int headingid;

    /*******************************************
     * The constructor
     *
     * @param sc The simulator connection object
     ********************************************/
    public SIMHeading(SIMConnection sc) {
        simConnection = sc;
        heading = 0;
        headingid = 0;
    }

    /*******************************************
     * This method queues a command to get the
     * current heading of the subject vehicle.
     *
     ********************************************/
    public void get() {
        // get the car direction of motion
        headingid = simConnection.getVariable("SubjectHeading", this);
    }

    /*******************************************
     * This method updates the current heading
     * of the subject vehicle, from the simulator
     * feedback. (invoked automatically)
     *
     ********************************************/
    public void update() {
        // heading in radians
        heading = simConnection.doubleValueOf(headingid, 0.0);
    }

    /*******************************************
     * This method returns the heading of the
     * subject vehicle in degrees.
     *
     * @return The value of the heading in degrees.
     ********************************************/
    public double value() {
        // heading in degrees
        return heading;
    }
}
