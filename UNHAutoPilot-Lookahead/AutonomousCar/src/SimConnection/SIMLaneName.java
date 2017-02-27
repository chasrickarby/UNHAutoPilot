package SimConnection;

/******************************************************
 * This class abstracts the TCL command to get the
 * lane name of the subject's current lane.
 *
 *@author Charles Rickarby, Eric Duross
 *@version 1.0
 *@since 11/8/2016
 */

public class SIMLaneName extends SIMFeedback {
    private SIMConnection simConnection;
    private String lanename;
    private int laneid;
    /*******************************************
     * The constructor
     *
     * @param sc The simulator connection object
     ********************************************/
    public SIMLaneName(SIMConnection sc) {
        simConnection = sc;
        laneid = 0;
    }

    /*******************************************
     * This method queues a command to get the
     * current name of the lane that the subject is
     * traveling in.
     *
     ********************************************/
    public void get() {
        // get the speed limit of the tile the subject is on
        laneid = simConnection.getVariable("LaneName", this);
    }

    /*******************************************
     * This method updates the current lane name
     * of the subject vehicle, from the simulator
     * feedback. (invoked automatically)
     *
     ********************************************/
    public void update() {
        lanename = simConnection.stringValueOf(laneid, "---");
    }

    /*******************************************
     * This method returns the lane name of the
     * subject vehicle.
     *
     * @return The lane name of the subject's lane.
     ********************************************/
    public String value() {
        // speed limit in mph
        return lanename;
    }
}
