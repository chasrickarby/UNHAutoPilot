package SimConnection;
import java.lang.Math;

/******************************************************
 * This class abstracts the TCL command to get the
 * velocity of the subject vehicle.
 *
 *@author Tom Miller
 *@version 1.0
 *@since 7/14/2015
 */
public class SIMVelocity extends SIMFeedback {
    private SIMConnection simConnection;
    private double velocity;
    private int velocityid;

    /*******************************************
     * The constructor
     *
     * @param sc The simulator connection object
     ********************************************/
    public SIMVelocity(SIMConnection sc) {
        simConnection = sc;
        velocity = 0;
        velocityid = 0;
    }

    /*******************************************
     * This method queues a command to get the
     * current velocity of the subject vehicle.
     *
     ********************************************/
    public void get() {
        // get the car velocity
        velocityid = simConnection.getVariable("Velocity", this);
    }

    /*******************************************
     * This method updates the current velocity
     * of the subject vehicle, from the simulator
     * feedback. (invoked automatically)
     *
     ********************************************/
    public void update() {
        // velocity in meters per second
        velocity = simConnection.doubleValueOf(velocityid, 0.0);
    }

    /*******************************************
     * This method returns the velocity of the
     * subject vehicle as set by the last call
     * to the update() method.
     *
     * @return The value of the velocity in meters
     * per second.
     ********************************************/
    public double value() {
        // velocity in meters per second
        return velocity;
    }

    /*******************************************
     * This method returns the velocity of the
     * subject vehicle as set by the last call
     * to the update() method.
     *
     * @return The value of the velocity in miles
     * per hour.
     ********************************************/
    public double mph() {
        double meterspermile = 1609.344;
        double secondsperhour = 3600.0;
        // velocity in miles per hour
        return velocity * secondsperhour /  meterspermile;
    }
}
