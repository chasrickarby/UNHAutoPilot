package SimConnection;

import UI.NavigationUI;

/******************************************************
 * This class abstracts the TCL command to get the
 * name of the next lane that connects to the current name.
 *
 *@author Charles Rickarby, Eric Duross
 *@version 1.0
 *@since 2/8/17
 */

public class SIMNextLane extends SIMFeedback {
    private SIMConnection simConnection;
    private String nextLaneName;
    private int nextLaneId;
    private String direction;
    private String laneName;
    /*******************************************
     * The constructor
     *
     * @param sc The simulator connection object
     ********************************************/
    public SIMNextLane(SIMConnection sc) {
        simConnection = sc;
        nextLaneName = "";
        nextLaneId = 0;
        direction = "Straight";
        laneName = "$::LaneName";
    }

    /*******************************************
     * This method queues a command to get the
     * name of the next lane.
     *
     ********************************************/
    public void get() {
        nextLaneId = simConnection.getExpression("LaneGetNextLane " + laneName + " " + direction, this);
    }

    /*******************************************
     * This method updates the name of the next lane.
     * (invoked automatically)
     *
     ********************************************/
    public void update() {
        nextLaneName = simConnection.stringValueOf(nextLaneId, "---");
    }

    /*******************************************
     * This method returns the name of the lane that
     * connects to the current lane.
     *
     * @return The number of connecting lanes.
     ********************************************/
    public String value() {
        return nextLaneName;
    }

    /*******************************************
     * This method sets the direction of the next lane
     * to retrieve.
     *
     * @param nextDirection The direction of the next lane
     *                      to retrieve. [Straight, Left, Right]
     ********************************************/
    public void setNextLaneDirection(NavigationUI.direction nextDirection){
        switch (nextDirection){
            case left:
                direction = "Left";
                break;
            case right:
                direction = "Right";
                break;
            case straight:
                direction = "Straight";
                break;
            default:
                direction = "Straight";
        }
    }

    /*******************************************
     * This method sets the name of the current lane.
     *
     * @param currentLaneName The name that the simulator gives
     *                        the current lane.
     ********************************************/
    public void setLaneName(String currentLaneName){laneName = currentLaneName;}
}

