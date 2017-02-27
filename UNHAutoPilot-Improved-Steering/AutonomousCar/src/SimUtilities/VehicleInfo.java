package SimUtilities;
import java.lang.Math;

/******************************************************
 * This class abstracts assorted information about
 * a vehicle.
 *
 *@author Tom Miller
 *@version 1.0
 *@since 7/16/2015
 */
public class VehicleInfo {
    private String vehname;
    private double distance;
    private double priordistance;
    private double direction;
    private double priordirection;
    private long timestamp;
    private long priortimestamp;

    /*******************************************
     * The constructor
     *
     ********************************************/
    public VehicleInfo() {
        vehname = "";
        distance = 0;
        direction = 0;
        timestamp = 0;
        priordistance = 0;
        priordirection = 0;
        priortimestamp = 0;
    }

    /*******************************************
     * The constructor
     *
     * @param nm  The vehicle name
     * @param dst The distance from subject vehicle
     * @param dir The direction from the subject vehicle
     * @param tim The observation time in milliseconds
     ********************************************/
    public VehicleInfo(String nm, double dst, double dir, long tim) {
        vehname = nm;
        distance = dst;
        direction = dir;
        timestamp = tim;
        priordistance = 0;
        priordirection = 0;
        priortimestamp = 0;
    }

    /*******************************************
     * This method changes the vehicle info object
     * a new vehicle.
     *
     * @param nm  The vehicle name
     * @param dst The distance from subject vehicle
     * @param dir The direction from the subject vehicle
     * @param tim The observation time in milliseconds
     ********************************************/
    public void set(String nm, double dst, double dir, long tim) {
        vehname = nm;
        distance = dst;
        direction = dir;
        timestamp = tim;
        priordistance = 0;
        priordirection = 0;
        priortimestamp = 0;
    }

    /*******************************************
     * This method copies the vehicle info from
     * an existing vehicle object
     *
     * @param vi  The vehicle name
     ********************************************/
    public void copy(VehicleInfo vi) {
        vehname = vi.name();
        distance = vi.relativedistance();
        direction = vi.relativedirection();
        timestamp = vi.time();
        priordistance = 0;
        priordirection = 0;
        priortimestamp = 0;
    }

    /*******************************************
     * This method returns the vehicle name
     *
     * @return The name.
     ********************************************/
    public String name() {
        return vehname;
    }

    /*******************************************
     * This method returns the object state
     *
     * @return The valid state.
     ********************************************/
    public boolean isvalid() {
        if (timestamp == 0) {
            return false;
        }
        return true;
    }

    /*******************************************
     * This method returns the timestamp for the most
     * recent update.
     *
     * @return The time in milliseconds.
     ********************************************/
    public long time() {
        return timestamp;
    }

    /*******************************************
     * This method returns the direction to the vehicle
     * in degrees relative to the subject vehicle location
     * and orientation
     *
     * @return The relative direction -180 to 180.
     ********************************************/
    public double relativedirection() {
        return direction;
    }

    /*******************************************
     * This method returns the vehicle distance
     * in meters relative to the subject vehicle
     * location
     *
     * @return The relative distance.
     ********************************************/
    public double relativedistance() {
        return distance;
    }

    /*******************************************
     * This method returns the change in distance
     * relative to the subject vehicle in meters
     * per second
     *
     * @return The escape velocity
     * (- converge, + diverge)
     ********************************************/
    public double relativevelocity() {
        if (priortimestamp == 0) return 0.0;
        return 1000.0 * (distance - priordistance) / (timestamp - priortimestamp);
    }

    /*******************************************
     * This method returns the change in direction
     * relative to the subject vehicle in degrees
     * per second
     *
     * @return The relative angular velocity
     * (- to left, + to right)
     ********************************************/
    public double relativeangularvel() {
        if (priortimestamp == 0) return 0.0;
        return 1000.0 * (direction - priordirection) / (timestamp - priortimestamp);
    }

    /*******************************************
     * Update internal vehicle information
     *
     * @param dst The distance from subject vehicle
     * @param dir The direction from the subject vehicle
     * @param tim The observation time in milliseconds
     ********************************************/
    public void update(double dst, double dir, long tim) {
        // copy the old information for derivatives
        priordistance = distance;
        priordirection = direction;
        priortimestamp = timestamp;
        // save the new state
        distance = dst;
        direction = dir;
        timestamp = tim;
    }
}
