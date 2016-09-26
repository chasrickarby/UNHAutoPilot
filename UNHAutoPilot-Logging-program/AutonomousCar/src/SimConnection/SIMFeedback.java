package SimConnection;

/******************************************************
 * This is the base class for other classes which
 * abstract TCL commands to get feedback from the
 * simulator.
 *
 *@author Tom Miller
 *@version 1.0
 *@since 7/14/2015
 */
abstract public class SIMFeedback {

    /*******************************************
     * This method queues a command to get the
     * class specific information.
     *
     ********************************************/
    abstract public void get();

    /*******************************************
     * This method updates the class specific
     * information from the simulator feedback.
     *
     ********************************************/
    abstract public void update();

}
