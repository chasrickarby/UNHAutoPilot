package SimConnection;

/******************************************************
 * This class abstracts the TCL command to get the
 * number of lanes that connects to the current lane. This is
 * usually expected to be one, however in the circumstance of
 * approaching an intersection this number will be greater than
 * one.
 *
 *@author Charles Rickarby, Eric Duross
 *@version 1.0
 *@since 2/8/17
 */

public class SIMNumNextLanes extends SIMFeedback {
    private SIMConnection simConnection;
    private int numConnectingLanes;
    private int connectingLanesId;
    private String laneName;
    /*******************************************
     * The constructor
     *
     * @param sc The simulator connection object
     ********************************************/
    public SIMNumNextLanes(SIMConnection sc) {
        simConnection = sc;
        numConnectingLanes = 0;
        connectingLanesId = 0;
        laneName = "$::LaneName";
    }

    /*******************************************
     * This method queues a command to get the
     * number of lanes connecting to the current lane.
     *
     ********************************************/
    public void get() {
        connectingLanesId = simConnection.getExpression("LaneGetNumNextLanes " + laneName, this);
    }

    /*******************************************
     * This method updates the count of the number of
     * lanes that connects to the current lane.
     * (invoked automatically)
     *
     ********************************************/
    public void update() {
            try {
                numConnectingLanes = simConnection.intValueOf(connectingLanesId, 0);
            }catch (NumberFormatException ex){
                // Do nothing, this will happen every once in a while when the
                // vehicle changes lane tiles.
            }
        }

    /*******************************************
     * This method returns the number of lanes that connect
     * to the current lane.
     *
     * @return The number of connecting lanes.
     ********************************************/
    public int value() {
        return numConnectingLanes;
    }

    /*******************************************
     * This method sets the name of the lane for which the number
     * of connecting lanes is to be determined.
     *
     ********************************************/
    public void setCurrentLane(String currentLaneName){
        laneName = currentLaneName;
    }
}

