package SimConnection;

/******************************************************
 * This class abstracts the TCL command to get the
 * number of lane points in the subject's current lane.
 *
 *@author Charles Rickarby, Eric Duross
 *@version 1.0
 *@since 10/15/2016
 */

public class SIMNumLanePoints extends SIMFeedback {
    private SIMConnection simConnection;
    private double numLanePoints;
    private int lanePointsId;
    /*******************************************
     * The constructor
     *
     * @param sc The simulator connection object
     ********************************************/
    public SIMNumLanePoints(SIMConnection sc) {
        simConnection = sc;
        numLanePoints = 0;
        lanePointsId = 0;
    }

    /*******************************************
     * This method queues a command to get the
     * number of lane points of the current lane.
     *
     ********************************************/
    public void get() {
        // get the speed limit of the tile the subject is on
        lanePointsId = simConnection.getExpression("LaneGetNumPoints $::LaneName", this);
    }

    /*******************************************
     * This method updates the number of lane points
     * of the current lane which the subject is in,
     * from the simulator feedback.
     * (invoked automatically)
     *
     ********************************************/
    public void update() {
        numLanePoints = simConnection.doubleValueOf(lanePointsId, 0.0);
    }

    /*******************************************
     * This method returns the number of lane
     * points of the current lane.
     *
     * @return The number of lane points.
     ********************************************/
    public double value() {
        // speed limit in mph
        return numLanePoints;
    }
}

