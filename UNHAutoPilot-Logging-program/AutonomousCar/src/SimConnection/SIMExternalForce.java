package SimConnection;

/******************************************************
 *This class abstracts the TCL command to set the desired
 * external force on the simulated vehicle.
 *
 *@author Tom Miller
 *@version 1.0
 *@since 6/22/2015
 ******************************************************/
public class SIMExternalForce {

    private SIMConnection simConnection;
    private int ixforce;
    private int iyforce;

    /*******************************************
     * The constructor
     *
     * @param sc The simulator connection object
     ********************************************/
    public SIMExternalForce(SIMConnection sc) {
        simConnection = sc;
        ixforce = 0;
        iyforce = 0;
    }

    /*******************************************
     * This method sets the magnitude of the
     * desired external force.
     *
     * @param xf The x component for the force
     * @param yf The y component for the force
     ********************************************/
    public void set(double xf, double yf) {
        ixforce = (int)xf;
        iyforce = (int)yf;
        simConnection.doCommand(cmd());
    }

    /*******************************************
     * This method formats the TCL command to
     * set the external force as requested.
     *
     * @return The formatted TCL command
     ********************************************/
    private String cmd() {
        return "DynamicsSetExternalForce 1 "+ixforce+" "+iyforce+" 0 0 0 0";
    }
}
