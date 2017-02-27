package AutonomousCar;

import java.lang.Double;
import java.awt.*;
import java.util.*;

import SimConnection.*;
import SimUtilities.Location;
import UI.*;

/******************************************************
 * This contains the procedures to get control related feedback
 * from the simulator, and to perform control computations
 * based on that feedback.
 *
 *@author Tom Miller
 *@version 1.0
 *@since 9/02/2016
 */
public class AutonomousControl {

    private SIMConnection simConnection;
    private SIMHeading heading;
    private SIMVelocity velocity;
    private SIMAccelPedal gaspedal;
    private SIMBrakePedal brakepedal;
    private SIMSteeringWheel steeringwheel;
    private SIMLanePosition laneposition;
    private SIMNumLanePoints numlanepoints;
    private SIMLanePoints lanepoints;
    private SIMLaneName lanename;
    private SIMNumNextLanes numnextlanes;
    private SIMNextLane nextlane;
    private SIMNumLanePoints numnextlanepoints;
    private SIMSubjectLocation location;
    private SIMSpeedLimit speedLimit;
    private SIMDisplayText velDisplay;
    private SIMDisplayText posDisplay;
    private SIMDisplayText strDisplay;
    private SIMDisplayText brkDisplay;
    private SIMDisplayText xlocDisplay;
    private SIMDisplayText ylocDisplay;
    private SIMDisplayText zlocDisplay;
    private SIMDisplayText numPtsDisplay;
    private SIMDisplayText gasDisplay;
    private int id;
    private double desiredspeedmph = 50.0;
    private double speedLimitMph = 0.0;
    private double speedmph = 0.0;
    private double oldspeedmph = 0.0;
    private double oldgaspedalangle = 0.0;
    private double oldbrakepedalangle = 0.0;
    private double laneposmeters = 0.0;
    private double oldlanepos = 0.0;
    private boolean inlane = true;
    private boolean autopilot = true;
    private double steerangle = 0.0;
    private double brakeangle = 0.0;
    private double gasangle = 0.0;
    private double xLocation = 0.0;
    private double yLocation = 0.0;
    private double zLocation = 0.0;
    private int numLanePoints = 0;
    private int numNextLanes = 0;
    private int numNextLanePoints = 0;
    private int numNextNextLanes = 0;
    private String nextLaneName = "";
    private Vector<String> lanePoints;
    private String laneName = "";
    private String oldLaneName = "";
    private boolean dirtyBit = false;
    private Vector<Location> locations;
    private boolean isNumNextNextLanesKnown = false;
    private boolean approachingIntersection = false;
    private double totalStoppingDistance;
    private boolean shouldStop = false;
    private boolean waitingForInput = false;
    private int waitCounter = 0;
    private boolean readyToExitIntersection = false;
    private Location oldLocation;
    private ControlUI controlUI;
    private NavigationUI navigationUI;

    /*******************************************
     * The constructor
     *
     * @param sc The simulator connection object
     ********************************************/
    public AutonomousControl(SIMConnection sc) {
        controlUI = new ControlUI();
        controlUI.setInitialValues(0.1, 0.35);

        navigationUI = new NavigationUI();

        simConnection = sc;
        // feedback items
        heading = new SIMHeading(simConnection);
        velocity = new SIMVelocity(simConnection);
        laneposition = new SIMLanePosition(simConnection);
        speedLimit = new SIMSpeedLimit(simConnection);
        location = new SIMSubjectLocation(simConnection);
        numlanepoints = new SIMNumLanePoints(simConnection);
        lanepoints = new SIMLanePoints(simConnection);
        lanename = new SIMLaneName(simConnection);
        numnextlanes = new SIMNumNextLanes(simConnection);
        nextlane = new SIMNextLane(simConnection);
        numnextlanepoints = new SIMNumLanePoints(simConnection);
        // actuators
        gaspedal = new SIMAccelPedal(simConnection);
        brakepedal = new SIMBrakePedal(simConnection);
        steeringwheel = new SIMSteeringWheel(simConnection);
        // display items
        velDisplay = new SIMDisplayText(simConnection, "velocity");
        velDisplay.position(0.1, 0.1);
        velDisplay.color(Color.white);
        velDisplay.size(1.5);
        velDisplay.channel(2);
        posDisplay = new SIMDisplayText(simConnection, "lanepos");
        posDisplay.position(0.1, 0.2);
        posDisplay.color(Color.white);
        posDisplay.size(1.5);
        posDisplay.channel(2);
        strDisplay = new SIMDisplayText(simConnection, "steering");
        strDisplay.position(0.3, 0.1);
        strDisplay.color(Color.blue);
        strDisplay.size(1.5);
        strDisplay.channel(2);

        brkDisplay = new SIMDisplayText(simConnection, "brake");
        brkDisplay.position(0.3, 0.2);
        brkDisplay.color(Color.red);
        brkDisplay.size(1.5);
        brkDisplay.channel(2);

        xlocDisplay = new SIMDisplayText(simConnection, "subjectx");
        xlocDisplay.position(0.5, 0.1);
        xlocDisplay.color(Color.orange);
        xlocDisplay.size(1.5);
        xlocDisplay.channel(2);

        ylocDisplay = new SIMDisplayText(simConnection, "subjecty");
        ylocDisplay.position(0.6, 0.1);
        ylocDisplay.color(Color.orange);
        ylocDisplay.size(1.5);
        ylocDisplay.channel(2);

        zlocDisplay = new SIMDisplayText(simConnection, "subjectz");
        zlocDisplay.position(0.7, 0.1);
        zlocDisplay.color(Color.orange);
        zlocDisplay.size(1.5);
        zlocDisplay.channel(2);

        numPtsDisplay = new SIMDisplayText(simConnection, "numlanepoints");
        numPtsDisplay.position(0.7, 0.2);
        numPtsDisplay.color(Color.red);
        numPtsDisplay.size(1.5);
        numPtsDisplay.channel(2);

        gasDisplay = new SIMDisplayText(simConnection, "gasangle");
        gasDisplay.position(0.5, 0.2);
        gasDisplay.color(Color.red);
        gasDisplay.size(1.5);
        gasDisplay.channel(2);
    }

    /*******************************************
     * This method queues the commands needed to
     * get the feedback items desired for the
     * control algorithm.
     ********************************************/
    public void getFeedback() {
        heading.get();
        velocity.get();
        laneposition.get();
        steeringwheel.get();
        speedLimit.get();
        brakepedal.get();
        location.get();
        numlanepoints.get();
        lanename.get();
        numnextlanes.get();
        nextlane.get();
        numnextlanepoints.get();

        if(!laneName.equals(oldLaneName)) {
            lanepoints.setLanePointsToRequest((int)numLanePoints);
            lanepoints.get();
            numNextLanes = numnextlanes.value();
            System.out.println("The next tile has " + numNextLanes + " lanes.");
            dirtyBit = true;
            isNumNextNextLanesKnown = false;
            numnextlanepoints.setLane(nextlane.value());

            // Check for an intersection on the next tile
            approachingIntersection = false;
            if(numNextLanes > 1){
                approachingIntersection = true;
            }
        }
        numnextlanes.setCurrentLane(nextlane.value());
    }

    /*******************************************
     * This method determines the control actions
     * based on the feedback, and queues the
     * simulator commands needed to make those
     * actions happen.
     ********************************************/
    public void doControl() {

        if (waitingForInput){
            System.out.println("Waiting for input...");
            waitCounter++;
            // Check for user input here in if statement, if input has happened, then reset waiting for input and
            // return out of doControl
            Vector<Location> newLocations = new Vector<Location>(parseLanePoints(lanePoints));
            if (newLocations != null && oldLocation != null && newLocations.firstElement() != oldLocation){
                waitingForInput = false;
                Location subjectLocation = new Location(xLocation, yLocation, zLocation);
                doTurn(subjectLocation, newLocations.firstElement());
                System.out.println("TURNING");
            }else if(navigationUI.getUserInput() == NavigationUI.direction.waiting){
                oldLocation = locations.firstElement();
                //waitingForInput = false;
                waitCounter = 0;
                //set direction of turn here
                nextlane.setNextLaneDirection(navigationUI.getUserInput());
                lanepoints.setLaneName(nextlane.value());
                navigationUI.resetDirection();
                return;
            }else{
                return;
            }
        }



        // grab the feedback values

        double uiDesiredSpeed = controlUI.getDesiredSpeed();
        if(uiDesiredSpeed != -1){
            desiredspeedmph = uiDesiredSpeed;
        }

        speedmph = velocity.mph();
        brakeangle = brakepedal.value();
        inlane = laneposition.inlane();
        laneposmeters = laneposition.value();
        numLanePoints = numlanepoints.value();
        numNextLanePoints = numnextlanepoints.value();

        double tilePointAngle = 0;

        if(dirtyBit){
            lanePoints = new Vector<>(lanepoints.value());
            locations = new Vector<Location>(parseLanePoints(lanePoints));
            dirtyBit = false;

            desiredspeedmph = speedLimit.value();
            speedLimitMph = desiredspeedmph;

            // Check to see if the lane is a straight road. If not, decrease desired speed based off of original
            // speed limit.
            if(locations.size() != 2){
                Location subjectLocation = new Location(xLocation, yLocation, zLocation);
                double subjectHeading = heading.value();
                tilePointAngle = locations.get(locations.size() - 1).directionfrom(subjectLocation);
                double angleBetween = (tilePointAngle - subjectHeading) % 360;
                desiredspeedmph = desiredspeedmph - ((desiredspeedmph * Math.abs(angleBetween)/45) * 0.1);
            }

            // Check if there is a really sharp turn on the tile ahead, and if so, slow down more.
            if (numNextLanePoints >= 80){
                desiredspeedmph *= .75;
            }
        }

        if (approachingIntersection){
            Location stopLocation = locations.lastElement();

            // Calculate stopping distance
            Location subjectLocation = new Location(xLocation, yLocation, zLocation);
            totalStoppingDistance = subjectLocation.distancefrom(stopLocation);
            approachingIntersection = false;
            shouldStop = true;
        }

        oldLaneName = laneName;
        laneName = lanename.value();

        xLocation = location.xValue();
        yLocation = location.yValue();
        zLocation = location.zValue();

        if(locations != null){
            RemovePastPoints();
        }

        controlUI.updateValues();

        if (inlane) {
            // do steering control (if moving)
            if (speedmph > 1.0) {
                // proportional control
                steerangle = -50 * 0.35 * laneposmeters;
                // differential control
                steerangle += -15 * 0.35 * (laneposmeters - oldlanepos) * speedmph;


                oldlanepos = laneposmeters;
                if(locations.size() != 0)
                {
                    Location subjectLocation = new Location(xLocation, yLocation, zLocation);
                    double subjectHeading = heading.value();
                    double pointAngle = locations.get(0).directionfrom(subjectLocation);
                    double angleBetween = (pointAngle - subjectHeading) % 360;
                    steerangle += 0.25 * angleBetween;
                }
                // set the new steering angle
                if(steerangle > 188)
                {
                    steeringwheel.set(188);
                }
                else if (steerangle < -188)
                {
                    steeringwheel.set(-188);
                }
                else
                {
                    steeringwheel.set(steerangle);
                }
                autopilot = true;
            }


            // do brake and gas pedal control
            // (not yet supported)
            oldbrakepedalangle = brakeangle;
            oldgaspedalangle = gasangle;
            oldspeedmph = speedmph;

            // If the car is currently approaching an intersection and should be slowing down
            if(shouldStop && speedmph > 0) {
                Location endLocation = locations.lastElement();
                // Calculate stopping distance
                Location currentLocation = new Location(xLocation, yLocation, zLocation);
                double currentStoppingDistance = currentLocation.distancefrom(endLocation);

                desiredspeedmph = (currentStoppingDistance / totalStoppingDistance) * 0.125 * speedLimitMph;
                System.out.println("Desired Speed: " + desiredspeedmph);
                System.out.println("Distance From Intersection: " + currentStoppingDistance);
            }

            if (speedmph > desiredspeedmph) {
                // Set brake
                if (Math.abs(speedmph - desiredspeedmph) < 2) {
                    oldbrakepedalangle = .1;
                    oldgaspedalangle = .1;
                } else {
                    gasangle = 0.0;
                    gaspedal.set(gasangle);
                    // proportional control
                    brakeangle = .1 + .01 * (speedmph / desiredspeedmph);
                    // differential control
                    brakeangle += .01 * (double) ((speedmph - oldspeedmph) / 100) * oldbrakepedalangle;
                    brakepedal.set(brakeangle);
                }
            } else {
                // Set gas
                if (Math.abs(speedmph - desiredspeedmph) < 1) {
                    oldbrakepedalangle = .1;
                    oldgaspedalangle = .1;
                } else {
                    brakeangle = 0.0;
                    brakepedal.set(brakeangle);
                    // proportional control
                    gasangle = .1 + 0.15 * (speedmph / desiredspeedmph);
                    // differential control
                    gasangle += 0.15 * (double) ((oldspeedmph - speedmph) / 100) * oldgaspedalangle;
                    gaspedal.set(gasangle);
                }
            }
        } else {
            // if not in lane, turn off autopilot
            if (autopilot) {
                steeringwheel.release();
                brakepedal.release();
                gaspedal.release();
                autopilot = false;
            }
        }
        if((int)(speedmph) == 0 && shouldStop){
            waitingForInput = true;
        }

    }

    /*******************************************
     * This method overrides the control actions
     * based on the feedback, and queues the
     * simulator commands needed to make those
     * actions happen, In order to allow the car to
     * make a turn at an intersection.
     ********************************************/
    public void doTurn(Location startLoc, Location endLoc) {

    }

    /*******************************************
     * This method removes lane points from the locations
     * vector, that the subject has already driven past.
     ********************************************/
    private void RemovePastPoints() {
        Location subjectLocation = new Location(xLocation, yLocation, zLocation);
        double subjectHeading = heading.value();
        Vector<Location> toRemove = new Vector<>();
        for (Location loc:locations) {
            double pointAngle = loc.directionfrom(subjectLocation);
            double angleBetween = Math.abs(pointAngle - subjectHeading) % 360;
            angleBetween = angleBetween > 180 ? 360 - angleBetween : angleBetween;
            if(angleBetween > 90){
                toRemove.add(loc);
            }
        }

        locations.removeAll(toRemove);
    }

    /*******************************************
     * This method parses the lane points from a vector
     * into a vector of Location objects.
     *
     * @param lanePoints lane points vector to be parsed.
     * @return Vector<Location> of lane points.
     ********************************************/
    private Vector<Location> parseLanePoints(Vector<String> lanePoints) {
        String pointsString = lanePoints.toString();
        pointsString = pointsString.replace("{", "");
        pointsString = pointsString.replace("}", "");
        pointsString = pointsString.replace("[", "");
        pointsString = pointsString.replace("]", "");
        pointsString = pointsString.replace(",", "");
        ArrayList<String> coordinatesVector = new ArrayList<String>(Arrays.asList(pointsString.split(" ")));
        coordinatesVector.removeAll(Collections.singleton(""));

        Vector<Location> locations = new Vector<Location>();
        int i = 1;

        while(i < coordinatesVector.size()){
            Location newLoc = new Location(Double.parseDouble(coordinatesVector.get(i)), Double.parseDouble(coordinatesVector.get(i+1)), Double.parseDouble(coordinatesVector.get(i+2)));
            locations.add(newLoc);
            i += 3;
            if(i%31 == 0){
                i++;
            }
        }
        return locations;
    }

    /*******************************************
     * This method displays useful info on
     * the simulator screen.
     ********************************************/
    public void doDisplay() {

        controlUI.doDisplay(speedmph, speedLimitMph, laneposmeters, steerangle, gasangle, brakeangle, xLocation, yLocation, zLocation);

        String vels = Double.toString(speedmph);
        String lpos = Double.toString(laneposmeters);
        String sangle = Double.toString(steerangle);

//        String bangle = Double.toString(brakeangle);
//        String gangle = Double.toString(gasangle);

        String xLoc = Double.toString(xLocation);
        String yLoc = Double.toString(yLocation);
        String zLoc = Double.toString(zLocation);

        String numPts = Double.toString(numLanePoints);

        // display velocity
        velDisplay.set(vels.substring(0, vels.indexOf('.')));
        // display lane position
        if (inlane) {
            posDisplay.set(lpos.substring(0, lpos.indexOf('.') + 2));
        } else {
            posDisplay.set("---");
        }
        // display autopilot steering angle
        if (autopilot) {
            strDisplay.set(sangle.substring(0, sangle.indexOf('.')));
        } else {
            strDisplay.set("---");
        }

//        // display brake angle
//        if (autopilot){
//            brkDisplay.set(bangle);
//        }else{
//            brkDisplay.set("---");
//        }
//
//        // display gas angle
//        if (autopilot){
//            gasDisplay.set(gangle);
//        }else{
//            gasDisplay.set("---");
//        }

        // display X location
        if (autopilot){
            xlocDisplay.set(xLoc.substring(0, xLoc.indexOf('.')));
        }else{
            zlocDisplay.set("---");
        }

        // display Y location
        if (autopilot){
            ylocDisplay.set(yLoc.substring(0, yLoc.indexOf('.')));
        }else{
            zlocDisplay.set("---");
        }

        // display Z location
        if (autopilot){
            zlocDisplay.set(zLoc.substring(0, zLoc.indexOf('.')));
        }else{
            zlocDisplay.set("---");
        }

        // display number of lane points
        if (autopilot){
            numPtsDisplay.set(numPts.substring(0, numPts.indexOf('.')));
        }else{
            numPtsDisplay.set("---");
        }
    }
}
