package SimConnection;

/******************************************************
 *This class abstracts the TCL command to set the desired
 * ignition switch position.
 *
 *@author Tom Miller
 *@version 1.0
 *@since 6/24/2015
 ******************************************************/
public class SIMIgnition {

    private SIMConnection simConnection;
    private String ignition;

    /*******************************************
     * The constructor
     *
     * @param sc The simulator connection object
     ********************************************/
    public SIMIgnition(SIMConnection sc) {
        simConnection = sc;
        ignition = "Off";
    }

    /*******************************************
     * This method sets the position of the
     * ignition switch.
     *
     * @param ign The ignition state: Off Start Run
     ********************************************/
    public void set(String ign) {
        ignition = ign;
        simConnection.doCommand(cmd());
    }

    /*******************************************
     * This method terminates control of the
     * ignition switch.
     *
     ********************************************/
    public void release() {
        simConnection.doCommand("DynamicsSetIgnitionValue DoneControlling");
    }

    /*******************************************
     * This method formats the TCL command to set
     * desired position of the ignition switch.
     *
     * @return The formatted TCL command
     ********************************************/
    private String cmd() {
        return "DynamicsSetIgnitionValue " + ignition;
    }

}
