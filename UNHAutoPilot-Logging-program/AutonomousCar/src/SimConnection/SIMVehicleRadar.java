package SimConnection;
import java.lang.Math;
import SimUtilities.*;

/******************************************************
 * This class abstracts the TCL command to get info
 * about the vehicles nearest to the subject vehicle.
 *
 *@author Tom Miller
 *@version 1.0
 *@since 7/16/2015
 */
public class SIMVehicleRadar extends SIMFeedback  {
    private SIMConnection simConnection;
    private String closest;
    private int closestid;
    private SIMHeading heading;
    private boolean privateheading;
    private VehicleInfo vehicles[];
    private VehicleInfo priorvehicles[];
    private VehicleInfo nullvehicle;
    private Timer vtime;
    private double range;
    private int maxvehicles;

    // initialize vehicle storage
    private void initvehicles() {
        vehicles = new VehicleInfo[maxvehicles];
        priorvehicles = new VehicleInfo[maxvehicles];
        for (int i=0; i<maxvehicles; ++i) {
            vehicles[i] = new VehicleInfo();
            priorvehicles[i] = new VehicleInfo();
        }
        nullvehicle = new VehicleInfo();
    }

    /*******************************************
     * The constructor (closest 8 vehicles)
     *
     * @param sc The simulator connection object
     ********************************************/
    public SIMVehicleRadar(SIMConnection sc) {
        simConnection = sc;
        closest = "";
        closestid = 0;
        privateheading = true;
        heading = new SIMHeading(sc);
        vtime = new Timer();
        range = 1000000000.0;
        maxvehicles = 8;
        initvehicles();
    }

    /*******************************************
     * The constructor
     *
     * @param sc The simulator connection object
     * @param hd External SIMHeading object
     ********************************************/
    public SIMVehicleRadar(SIMConnection sc, SIMHeading hd) {
        simConnection = sc;
        closest = "";
        closestid = 0;
        privateheading = false;
        heading = hd;
        vtime = new Timer();
        range = 1000000000.0;
        maxvehicles = 8;
        initvehicles();
    }

    /*******************************************
     * This method queues a command to get the
     * vehicles nearest to the subject vehicle.
     *
     ********************************************/
    public void get() {
        // if necessary, get the subject vehicle heading
        if (privateheading) {
            heading.get();
        }
        // get the nearest vehicles
        closestid = simConnection.getExpression("EntityGetClosestEntities SUBJECT 8 Vehicles", this);
    }

    /*******************************************
     * This method updates the vehicles nearest
     * to the subject vehicle, from the simulator
     * feedback. (invoked automatically)
     *
     ********************************************/
    public void update() {
        // get the nearest vehicles
        closest = simConnection.stringValueOf(closestid, "");
        parseclosest();
    }

    /*******************************************
     * This method sets the maximum range of the
     * radar
     *
     * @param rnge The maximum radar range
     ********************************************/
    public void setrange(double rnge) {
        range = rnge;
    }

    /*******************************************
     * This method sets the maximum number of
     * close vehicles the radar can detect
     *
     * @param numveh The maximum number of vehicles
     ********************************************/
    public void settracking(int numveh) {
        maxvehicles = numveh;
        initvehicles();
    }

    /*******************************************
     * This method returns the vehicle information
     * selected by the index
     *
     * @return The name.
     ********************************************/
    public VehicleInfo value(int i) {
        if (i < maxvehicles) return vehicles[i];
        return nullvehicle;
    }

    /*******************************************
     * This method parses the string containing
     * the vehicles nearest to the subject vehicle
     *
     ********************************************/
    private void parseclosest() {
        // copy existing vehicle information
        for (int i=0; i<maxvehicles; ++i) {
            priorvehicles[i].copy(vehicles[i]);
        }

        // set up for parsing entire return
        long updatetime = vtime.time();
        int maxindex = closest.length();
        int firstindex = 0;
        int lastindex = 0;
        int vindex = 0;
        int priorindex = 0;
        String vname;
        double vdistance;
        double vdirection;
        double rdirection = heading.value();

        // try to find all vehicle descriptions
        while (vindex < maxvehicles) {
            // find vehicle name
            while (firstindex < maxindex) {
                if (closest.charAt(firstindex) == ' ') ++firstindex;
                else break;
            }
            lastindex = firstindex;
            while (lastindex < maxindex) {
                if (closest.charAt(lastindex) != ' ') ++lastindex;
                else break;
            }
            if (lastindex > firstindex) {
                vname = closest.substring(firstindex, lastindex);
            } else {
                vname = "";
            }
            firstindex = lastindex;
            // find vehicle distance
            while (firstindex < maxindex) {
                if (closest.charAt(firstindex) == ' ') ++firstindex;
                else break;
            }
            lastindex = firstindex;
            while (lastindex < maxindex) {
                if (closest.charAt(lastindex) != ' ') ++lastindex;
                else break;
            }
            if (lastindex > firstindex) {
                vdistance = Double.valueOf(closest.substring(firstindex, lastindex));
            } else {
                vdistance = 0.0;
            }
            firstindex = lastindex;
            // find vehicle direction
            while (firstindex < maxindex) {
                if (closest.charAt(firstindex) == ' ') ++firstindex;
                else break;
            }
            lastindex = firstindex;
            while (lastindex < maxindex) {
                if (closest.charAt(lastindex) != ' ') ++lastindex;
                else break;
            }
            if (lastindex > firstindex) {
                vdirection = Double.valueOf(closest.substring(firstindex, lastindex)) - rdirection;
                if (vdirection > 180) vdirection -= 360;
                else if (vdirection < -180) vdirection += 360;
            } else {
                vdirection = 0.0;
            }
            firstindex = lastindex;
            // store information in vehicleinfo array (if found name and in range)
            if ((vname.length() > 0) && (vdistance < range)) {
                // now search for matching name in prior vehicles
                priorindex = -1;
                for (int i=0; i<maxvehicles; ++i) {
                    if (priorvehicles[i].name().equals(vname)) {
                        priorindex = i;
                        break;
                    }
                }
                // if vehicle already existed, update information
                if (priorindex >= 0) {
                    vehicles[vindex].copy(priorvehicles[priorindex]);
                    vehicles[vindex].update(vdistance, vdirection, updatetime);
                } else {
                    // we haven't seen this vehicle recently
                    vehicles[vindex].set(vname, vdistance, vdirection, updatetime);
                }

            } else {
                // we didn't find a close vehicle
                vehicles[vindex].copy(nullvehicle);
            }
            // do next vehicle in list
            ++vindex;
        }
    }

}
