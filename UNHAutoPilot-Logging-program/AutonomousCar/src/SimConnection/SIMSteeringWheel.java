package SimConnection;
import java.lang.Math;

/******************************************************
 * This class abstracts the TCL command to set the desired
 * position of the steering wheel and to monitor the
 * actual position of the wheel, in degrees.
 *
 *@author Tom Miller
 *@version 1.0
 *@since 6/23/2015
 ******************************************************/
public class SIMSteeringWheel extends SIMFeedback {

    private SIMConnection simConnection;
    private double steerposition;
    private int steerpositionid;

    /*******************************************
     * The constructor
     *
     * @param sc The simulator connection object
     ********************************************/
    public SIMSteeringWheel(SIMConnection sc) {
        simConnection = sc;
        steerposition = 0;
        steerpositionid = 0;
    }

    /*******************************************
     * This method queues a command to get the
     * actual position of the steering wheel
     * of the subject vehicle.
     *
     ********************************************/
    public void get() {
        // get the position of the steering wheel
        steerpositionid = simConnection.getVariable("Steer", this);
    }

    /*******************************************
     * This method updates the actual position
     * of the steering wheel of the subject
     * vehicle, from the simulator feedback.
     * (invoked automatically)
     *
     ********************************************/
    public void update() {
        // steering wheel
        steerposition = simConnection.doubleValueOf(steerpositionid, 0.0);
    }

    /*******************************************
     * This method returns the actual position
     * of the steering wheel of the subject
     * vehicle in degrees.
     *
     * @return The value of the steering wheel
     * (-540 to +540 degrees)
     ********************************************/
    public double value() {
        // actual steering wheel
        return steerposition;
    }

    /*******************************************
     * This method sets the magnitude of the
     * desired position of the steering wheel
     * in degrees.
     *
     * @param steer The steering wheel
     * position (-540 to +540 degrees)
     ********************************************/
    public void set(double steer) {
        double tsteer = steer;
        if (steer < -540) {
            tsteer = -540;
        } else if (steer > 540) {
            tsteer = 540;
        }
        simConnection.doCommand(cmd(Math.toRadians(tsteer)));
    }

    /*******************************************
     * This method terminates control of the
     * steering wheel.
     *
     ********************************************/
    public void release() {
        simConnection.doCommand("DynamicsSetSteerValue DoneControlling");
    }

    /*******************************************
     * This method formats the TCL command to set
     * desired position of the steering wheel.
     *
     * @param steer The steering wheel
     * position (-3pi to 3pi radians)
     * @return The formatted TCL command
     ********************************************/
    private String cmd(double steer) {
        return "DynamicsSetSteerValue " + steer;
    }
}
