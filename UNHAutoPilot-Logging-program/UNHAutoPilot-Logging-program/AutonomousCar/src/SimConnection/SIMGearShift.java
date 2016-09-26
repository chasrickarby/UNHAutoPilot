package SimConnection;

/******************************************************
 * This class abstracts the TCL command to set the desired
 * gear and to monitor the actual position of the gear shift.
 *
 *@author Tom Miller
 *@version 1.0
 *@since 6/24/2015
 ******************************************************/
public class SIMGearShift extends SIMFeedback {

    private SIMConnection simConnection;
    private String gear;
    private int gearid;

    /*******************************************
     * The constructor
     *
     * @param sc The simulator connection object
     ********************************************/
    public SIMGearShift(SIMConnection sc) {
        simConnection = sc;
        gear = "P";
        gearid = 0;
    }

    /*******************************************
     * This method queues a command to get the
     * actual position of the gear shift
     * of the subject vehicle.
     *
     ********************************************/
    public void get() {
        // get the position of the gear shift
        gearid = simConnection.getVariable("Gear", this);
    }

    /*******************************************
     * This method updates the actual position
     * of the gear shift of the subject
     * vehicle, from the simulator feedback.
     * (invoked automatically)
     *
     ********************************************/
    public void update() {
        // gear shift
        gear = simConnection.stringValueOf(gearid, "P");
    }

    /*******************************************
     * This method returns the actual position
     * of the gear shift of the subject
     * vehicle.
     *
     * @return The value of the gear shift: P R N D L 1
     ********************************************/
    public String value() {
        // actual gear shift
        return gear;
    }

    /*******************************************
     * This method sets the position of the
     * gear shift.
     *
     * @param gr The gear: P R N D L 1
     ********************************************/
    public void set(String gr) {
        simConnection.doCommand(cmd(gr));
    }

    /*******************************************
     * This method terminates control of the
     * gear shift.
     *
     ********************************************/
    public void release() {
        simConnection.doCommand("DynamicsSetGearValue DoneControlling");
    }

    /*******************************************
     * This method formats the TCL command to set
     * desired position of the gear shift.
     *
     * @param gr The gear: P R N D L 1
     * @return The formatted TCL command
     ********************************************/
    private String cmd(String gr) {
        return "DynamicsSetGearValue " + gr;
    }

}
