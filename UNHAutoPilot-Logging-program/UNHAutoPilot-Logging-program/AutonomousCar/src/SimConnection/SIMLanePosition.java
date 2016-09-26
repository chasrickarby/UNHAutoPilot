package SimConnection;

/******************************************************
 * The lane offset in meters within the current lane. It will be '-' if the subject
 * is not in a lane. Positive is to the right, negative is to the left.
 * This value is reported with three digits to the right of the decimal.
 *
 *@author Tom Miller
 *@version 1.0
 *@since 9/02/2016
 */
public class SIMLanePosition extends SIMFeedback {
    private SIMConnection simConnection;
    private String lanepos;
    private double dlanepos;
    private boolean inlane;
    private int laneposid;
    private String notinlane;

    /*******************************************
     * The constructor
     *
     * @param sc The simulator connection object
     ********************************************/
    public SIMLanePosition(SIMConnection sc) {
        simConnection = sc;
        lanepos = "";
        dlanepos = 0.0;
        laneposid = 0;
        notinlane = "-";
        inlane = true;
    }

    /*******************************************
     * This method queues a command to get the
     * current position of the subject vehicle.
     *
     ********************************************/
    public void get() {
        // get the car direction of motion
        laneposid = simConnection.getVariable("LanePos", this);
    }

    /*******************************************
     * This method updates the position
     * of the subject vehicle, from the simulator
     * feedback. (invoked automatically)
     *
     ********************************************/
    public void update() {
        // heading in radians
        lanepos = simConnection.stringValueOf(laneposid, "");
        if (lanepos.equals(notinlane)) {
            inlane = false;
            dlanepos = 0.0;
        } else {
            inlane = true;
            dlanepos = Double.valueOf(lanepos);
        }
    }

    /*******************************************
     * This method returns the position of the
     * subject vehicle in the lane.
     *
     * @return The value of the position in meters.
     ********************************************/
    public double value() {
        // lane position in meters
        return dlanepos;
    }

    /*******************************************
     * This method returns true if the lane position
     * value is valid.
     *
     * @return The valid state of the lane position.
     ********************************************/
    public boolean inlane() {
        // true means in a lane
        return inlane;
    }
}
