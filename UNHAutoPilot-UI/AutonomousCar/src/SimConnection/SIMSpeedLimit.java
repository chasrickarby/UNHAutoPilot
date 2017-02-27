package SimConnection;

/******************************************************
 * This class abstracts the TCL command to get the
 * speedlimit of the subject's current lane.
 *
 *@author Charles Rickarby, Eric Duross
 *@version 1.0
 *@since 10/15/2016
 */

public class SIMSpeedLimit extends SIMFeedback {
    private SIMConnection simConnection;
    private double speedlimit;
    private int speedlimitid;
    /*******************************************
     * The constructor
     *
     * @param sc The simulator connection object
     ********************************************/
    public SIMSpeedLimit(SIMConnection sc) {
        simConnection = sc;
        speedlimit = 0;
        speedlimitid = 0;
    }

    /*******************************************
     * This method queues a command to get the
     * current speed limit of the subject vehicle.
     *
     ********************************************/
    public void get() {
        // get the speed limit of the tile the subject is on
        speedlimitid = simConnection.getVariable("SpeedLimit", this);
    }

    /*******************************************
     * This method updates the current speed limit
     * of the subject vehicle, from the simulator
     * feedback. (invoked automatically)
     *
     ********************************************/
    public void update() {
        // speed limit in mph
        try
        {
            speedlimit = simConnection.doubleValueOf(speedlimitid, 0.0) / .44704;
        }
        catch (Exception e)
        {
            speedlimit = 0.0;
        }
    }

    /*******************************************
     * This method returns the speed limit of the
     * subject vehicle in degrees.
     *
     * @return The value of the speed limit in mph.
     ********************************************/
    public double value() {
        // speed limit in mph
        return speedlimit;
    }
}
