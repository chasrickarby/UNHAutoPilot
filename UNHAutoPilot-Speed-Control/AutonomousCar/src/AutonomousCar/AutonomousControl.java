package AutonomousCar;

import java.io.*;

import java.lang.Math;
import java.lang.Double;
import java.awt.*;

import SimConnection.*;
import SimUtilities.*;

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
    private SIMIgnition ignition;
    private SIMSubjectLocation location;
    private SIMSpeedLimit speedLimit;
    private SIMDisplayText velDisplay;
    private SIMDisplayText posDisplay;
    private SIMDisplayText strDisplay;
    private SIMDisplayText brkDisplay;
    private SIMDisplayText xlocDisplay;
    private SIMDisplayText ylocDisplay;
    private SIMDisplayText zlocDisplay;
    private int id;
    private double desiredspeedmph = 50.0;
    private double speedmph = 0.0;
    private double laneposmeters = 0.0;
    private double oldlanepos = 0.0;
    private boolean inlane = true;
    private boolean autopilot = true;
    private double steerangle = 0.0;
    private double brakeangle = 0.0;
    private double xLocation = 0.0;
    private double yLocation = 0.0;
    private double zLocation = 0.0;

    /*******************************************
     * The constructor
     *
     * @param sc The simulator connection object
     ********************************************/
    public AutonomousControl(SIMConnection sc) {
        simConnection = sc;
        // feedback items
        heading = new SIMHeading(simConnection);
        velocity = new SIMVelocity(simConnection);
        laneposition = new SIMLanePosition(simConnection);
        speedLimit = new SIMSpeedLimit(simConnection);
        location = new SIMSubjectLocation(simConnection);
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
    }

    /*******************************************
     * This method determines the control actions
     * based on the feedback, and queues the
     * simulator commands needed to make those
     * actions happen.
     ********************************************/
    public void doControl() {
        // grab the feedback values
        speedmph = velocity.mph();
        brakeangle = brakepedal.value();
        inlane = laneposition.inlane();
        laneposmeters = laneposition.value();

        xLocation = location.xValue();
        yLocation = location.yValue();
        zLocation = location.zValue();

        desiredspeedmph = speedLimit.value();
        if (inlane) {
            // do steering control (if moving)
            if (speedmph > 1.0) {
                // proportional control
                steerangle = -50 * laneposmeters;
                // differential control
                steerangle += -15 *(laneposmeters - oldlanepos) * speedmph;
                oldlanepos = laneposmeters;
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
            if(desiredspeedmph  > speedmph)
            {
                if( (desiredspeedmph - speedmph) < 1 )
                {
                    //gaspedal.set(0);
                    brakepedal.set(0);
                }
                else if( (desiredspeedmph - speedmph) < 5 )
                {
                    gaspedal.set(0.3);
                    brakepedal.set(0);
                }
                else
                {
                    gaspedal.set(0.5);
                    brakepedal.set(0);
                }
            }
            else
            {
                if( (speedmph - desiredspeedmph) < 1 )
                {
                    //brakeangle = 0.3;
                    //brakepedal.set(brakeangle);
                    //gaspedal.set(0.7);
                }
                else if( (speedmph - desiredspeedmph) < 3 )
                {
                    brakeangle = 0.3;
                    brakepedal.set(brakeangle);
                    //gaspedal.set(0.5);
                }
                else if( (speedmph - desiredspeedmph) < 5 )
                {
                    brakeangle = 0.7;
                    brakepedal.set(brakeangle);
                    //gaspedal.set(0.3);
                }
                else
                {
                    brakeangle = 1.0;
                    brakepedal.set(brakeangle);
                    gaspedal.set(0);
                }
            }
            // do brake and gas pedal control
            // (not yet supported)
        } else {
            // if not in lane, turn off autopilot
            if (autopilot) {
                steeringwheel.release();
                brakepedal.release();
                gaspedal.release();
                autopilot = false;
            }
        }
    }

    /*******************************************
     * This method displays useful info on
     * the simulator screen.
     ********************************************/
    public void doDisplay() {
        String vels = Double.toString(speedmph);
        String lpos = Double.toString(laneposmeters);
        String sangle = Double.toString(steerangle);

        String bangle = Double.toString(brakeangle);

        String xLoc = Double.toString(xLocation);
        String yLoc = Double.toString(yLocation);
        String zLoc = Double.toString(zLocation);

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

        // display brake angle
        if (autopilot){
            brkDisplay.set(bangle);
        }else{
            brkDisplay.set("---");
        }

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
    }
}
