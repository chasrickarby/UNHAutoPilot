package SimConnection;
import java.lang.Math;

/******************************************************
 * This class abstracts the TCL commands to get the
 * pitch and roll of the subject vehicle.
 *
 *@author Tom Miller
 *@version 1.0
 *@since 7/14/2015
 */
public class SIMPitch extends SIMFeedback {
    private SIMConnection simConnection;
    private double pitch;
    private double rollv;
    private int pitchid;
    private int rollid;

    /*******************************************
     * The constructor
     *
     * @param sc The simulator connection object
     ********************************************/
    public SIMPitch(SIMConnection sc) {
        simConnection = sc;
        pitch = 0;
        rollv = 0;
        pitchid = 0;
        rollid = 0;
    }

    /*******************************************
     * This method queues a command to get the
     * pitch and roll of the subject vehicle.
     *
     ********************************************/
    public void get() {
        // get the car pitch and roll
        pitchid = simConnection.getVariable("SubjectPitch", this);
        rollid = simConnection.getVariable("SubjectRoll", this);
    }

    /*******************************************
     * This method updates the pitch and roll
     * of the subject vehicle, from the simulator
     * feedback. (invoked automatically)
     *
     ********************************************/
    public void update() {
        // pitch and roll in radians
        pitch = simConnection.doubleValueOf(pitchid, 0.0);
        rollv = simConnection.doubleValueOf(rollid, 0.0);
    }

    /*******************************************
     * This method returns the pitch of the subject
     * vehicle in degrees.
     *
     * @return The value of the pitch in degrees.
     ********************************************/
    public double value() {
        // pitch in degrees
        return pitch;
    }

    /*******************************************
     * This method returns the roll of the
     * subject vehicle in degrees.
     *
     * @return The value of the roll in degrees.
     ********************************************/
    public double roll() {
        // roll in degrees
        return rollv;
    }

}
