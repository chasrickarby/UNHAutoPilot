package SimConnection;

/******************************************************
 * This class abstracts the TCL command to set the desired
 * position of the brake pedal and to monitor the
 * actual position of the pedal.
 *
 *@author Tom Miller
 *@version 1.0
 *@since 6/23/2015
 ******************************************************/
public class SIMBrakePedal extends SIMFeedback {

    private SIMConnection simConnection;
    private double brakepedal;
    private int brakepedalid;

    /*******************************************
     * The constructor
     *
     * @param sc The simulator connection object
     ********************************************/
    public SIMBrakePedal(SIMConnection sc) {
        simConnection = sc;
        brakepedal = 0;
        brakepedalid = 0;
    }

    /*******************************************
     * This method queues a command to get the
     * actual position of the brake pedal
     * of the subject vehicle.
     *
     ********************************************/
    public void get() {
        // get the position of the brake pedal
        brakepedalid = simConnection.getVariable("Brake", this);
    }

    /*******************************************
     * This method updates the actual position
     * of the brake pedal of the subject
     * vehicle, from the simulator feedback.
     * (invoked automatically)
     *
     ********************************************/
    public void update() {
        // brake pedal
        brakepedal = simConnection.doubleValueOf(brakepedalid, 0.0);
    }

    /*******************************************
     * This method returns the actual position
     * of the brake pedal of the subject
     * vehicle.
     *
     * @return The value of the pedal (0 - 1.0)
     ********************************************/
    public double value() {
        // actual brake pedal
        return brakepedal;
    }

    /*******************************************
     * This method sets the magnitude of the
     * desired position of the brake pedal.
     *
     * @param pedal The pedal position (0 - 1.0)
     ********************************************/
    public void set(double pedal) {
        simConnection.doCommand(cmd(pedal));
    }

    /*******************************************
     * This method terminates control of the
     * brake pedal.
     *
     ********************************************/
    public void release() {
        simConnection.doCommand("DynamicsSetBrakePedalValue DoneControlling");
    }

    /*******************************************
     * This method formats the TCL command to set
     * desired position of the brake pedal.
     *
     * @param pedal The pedal position (0 - 1.0)
     * @return The formatted TCL command
     ********************************************/
    private String cmd(double pedal) {
        if (brakepedal < 0.0) {
            return "DynamicsSetBrakePedalValue 0.0";
        } else if (brakepedal > 1.0) {
            return "DynamicsSetBrakePedalValue 1.0";
        }
        return "DynamicsSetBrakePedalValue " + brakepedal;
    }

}
