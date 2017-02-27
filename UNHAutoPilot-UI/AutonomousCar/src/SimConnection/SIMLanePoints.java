package SimConnection;

import java.util.Vector;

/******************************************************
 * This class abstracts the TCL command to get the
 * location points in the current lane.
 *
 *@author Charles Rickarby
 *@version 1.0
 *@since 10/15/2016
 */

public class SIMLanePoints extends SIMFeedback {
    private SIMConnection simConnection;
    private double lanePointsToRequest;
    private Vector<Integer> lanePointsId;
    private int idIndex = 0;
    private Vector<String> points;
    private String laneName;
    /*******************************************
     * The constructor
     *
     * @param sc The simulator connection object
     ********************************************/
    public SIMLanePoints(SIMConnection sc) {
        simConnection = sc;
        lanePointsToRequest = 10;
        lanePointsId = new Vector<Integer>();
        points = new Vector<String>();
        laneName = "$::LaneName";
    }

    /*******************************************
     * This method queues a command to get the
     * lane points of the current lane.
     *
     ********************************************/
    public void get() {
        // get the speed limit of the tile the subject is on
        lanePointsId.clear();
        int index = 0;
        idIndex = 0;
        while(index < lanePointsToRequest){
            lanePointsId.add(simConnection.getExpression("LaneGetPoints " + laneName + " " + index, this));
            idIndex++;
            index += 10;
        }
    }

    /*******************************************
     * This method updates lane points of the current
     * lane which the subject is in,
     * from the simulator feedback.
     * (invoked automatically)
     *
     ********************************************/
    public void update() {
        points.clear();
        for(int i = 0; i < idIndex; i++){
            points.add(simConnection.stringValueOf(lanePointsId.elementAt(i), "---"));
        }
    }

    public void setLanePointsToRequest(int numPoints){
        lanePointsToRequest = numPoints;
    }

    /*******************************************
     * This method returns the lane
     * points of the current lane, as a string
     * array.
     *
     * @return The number of lane points as a
     * string vector.
     ********************************************/
    public Vector<String> value() {
        // speed limit in mph
        return points;
    }

    /*******************************************
     * This method sets the lane name for retrieval of lane
     * points on up-coming lanes.
     *
     * @param currentLaneName The name to set the lane name to
     ********************************************/
    public void setLaneName(String currentLaneName)
    {
        laneName = currentLaneName;
    }
}

