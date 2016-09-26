package SimConnection;

/******************************************************
 * This class abstracts the TCL commands to set the desired
 * position of the acceleration pedal and to monitor the
 * actual position of the pedal.
 *
 *@author Tom Miller
 *@version 1.0
 *@since 6/23/2015
 ******************************************************/
public class SIMAccelPedal extends SIMFeedback  {

    private SIMConnection simConnection;
    private double accelpedal;
    private int accelid;

    /*******************************************
     * The constructor
     *
     * @param sc The simulator connection object
     ********************************************/
    public SIMAccelPedal(SIMConnection sc) {
        simConnection = sc;
        accelpedal = 0;
        accelid = 0;
    }

    /*******************************************
     * This method queues a command to get the
     * actual position of the acceleration pedal
     * of the subject vehicle.
     *
     ********************************************/
    public void get() {
        // get the position of the acceleration pedal
        accelid = simConnection.getVariable("Accel", this);
    }

    /*******************************************
     * This method updates the actual position
     * of the acceleration pedal of the subject
     * vehicle, from the simulator feedback.
     * (invoked automatically)
     *
     ********************************************/
    public void update() {
        // acceleration pedal
        accelpedal = simConnection.doubleValueOf(accelid, 0.0);
    }

    /*******************************************
     * This method returns the actual position
     * of the acceleration pedal of the subject
     * vehicle.
     *
     * @return The value of the pedal (0 - 1.0)
     ********************************************/
    public double value() {
        // actual acceleration pedal
        return accelpedal;
    }

    /*******************************************
     * This method sets the magnitude of the
     * desired position of the acceleration pedal.
     *
     * @param pedal The pedal position (0 - 1.0)
     ********************************************/
    public void set(double pedal) {
        simConnection.doCommand(cmd(pedal));
    }

    /*******************************************
     * This method terminates control of the
     * acceleration pedal.
     ********************************************/
    public void release() {
        simConnection.doCommand("DynamicsSetAccelPedalValue DoneControlling");
    }

    /*******************************************
     * This method formats the TCL command to set
     * desired position of the acceleration pedal.
     *
     * @param pedal The pedal position (0 - 1.0)
     * @return The formatted TCL command
     ********************************************/
    private String cmd(double pedal) {
        if (pedal < 0.0) {
            return "DynamicsSetAccelPedalValue 0.0";
        } else if (pedal > 1.0) {
            return "DynamicsSetAccelPedalValue 1.0";
        }
        return "DynamicsSetAccelPedalValue " + pedal;
    }

}
