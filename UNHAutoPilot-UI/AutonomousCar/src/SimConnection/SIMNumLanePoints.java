package SimConnection;

import java.text.NumberFormat;

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
    private int numLanePoints;
    private int lanePointsId;
    private String lanename;
    /*******************************************
     * The constructor
     *
     * @param sc The simulator connection object
     ********************************************/
    public SIMNumLanePoints(SIMConnection sc) {
        simConnection = sc;
        numLanePoints = 0;
        lanePointsId = 0;
        lanename = "$::LaneName";
    }

    /*******************************************
     * This method queues a command to get the
     * number of lane points of the current lane.
     *
     ********************************************/
    public void get() {
        // get the speed limit of the tile the subject is on
        lanePointsId = simConnection.getExpression("LaneGetNumPoints " + lanename, this);
    }

    /*******************************************
     * This method updates the number of lane points
     * of the current lane which the subject is in,
     * from the simulator feedback.
     * (invoked automatically)
     *
     ********************************************/
    public void update() {
        try {
            numLanePoints = simConnection.intValueOf(lanePointsId, 0);
        }catch (NumberFormatException ex){
            // Something went wrong on startup because the "next" lane name could not be retrieved.
        }
    }

    /*******************************************
     * This method returns the number of lane
     * points of the current lane.
     *
     * @return The number of lane points.
     ********************************************/
    public int value() {
        // speed limit in mph
        return numLanePoints;
    }

    /*******************************************
     * This method sets the name of the lane to
     * get the number of points of.
     *
     ********************************************/
    public void setLane(String laneName){
        lanename = laneName;
    }
}

