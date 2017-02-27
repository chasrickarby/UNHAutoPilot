package SimConnection;

/**
 * Created by Project54 on 10/26/2016.
 */
public class SIMSubjectLocation extends SIMFeedback {
    private SIMConnection simConnection;
    private double xCoordinate;
    private double yCoordinate;
    private double zCoordinate;

    private int xCoordinateId;
    private int yCoordinateId;
    private int zCoordinateId;

    /*******************************************
     * The constructor
     *
     * @param sc The simulator connection object
     ********************************************/
    public SIMSubjectLocation(SIMConnection sc) {
        simConnection = sc;
        xCoordinate = 0;
        yCoordinate = 0;
        zCoordinate = 0;

        xCoordinateId = 0;
        yCoordinateId = 0;
        zCoordinateId = 0;
    }

    /*******************************************
     * This method queues a command to get the
     * current location of the subject vehicle.
     *
     ********************************************/
    public void get() {
        // get the location of the subject
        xCoordinateId = simConnection.getVariable("SubjectX", this);
        yCoordinateId = simConnection.getVariable("SubjectY", this);
        zCoordinateId = simConnection.getVariable("SubjectZ", this);
    }

    /*******************************************
     * This method updates the current location
     * of the subject vehicle, from the simulator
     * feedback. (invoked automatically)
     *
     ********************************************/
    public void update() {
        // speed limit in mph
        xCoordinate = simConnection.doubleValueOf(xCoordinateId, 0.0);
        yCoordinate = simConnection.doubleValueOf(yCoordinateId, 0.0);
        zCoordinate = simConnection.doubleValueOf(zCoordinateId, 0.0);
    }

    /*******************************************
     * This method returns the X location of the
     * subject vehicle.
     *
     * @return The X coordinate of the subject's location.
     ********************************************/
    public double xValue() {
        // X Coordinate
        return xCoordinate;
    }

    /*******************************************
     * This method returns the Y location of the
     * subject vehicle.
     *
     * @return The Y coordinate of the subject's location.
     ********************************************/
    public double yValue() {
        // Y Coordinate
        return yCoordinate;
    }

    /*******************************************
     * This method returns the Z location of the
     * subject vehicle.
     *
     * @return The Z coordinate of the subject's location.
     ********************************************/
    public double zValue() {
        // Z Coordinate
        return zCoordinate;
    }
}
