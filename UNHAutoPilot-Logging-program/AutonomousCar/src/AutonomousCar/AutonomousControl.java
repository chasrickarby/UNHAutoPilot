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

    private SIMDisplayText velDisplay;
    private SIMDisplayText posDisplay;
    private SIMDisplayText strDisplay;

    private double speedmph = 0.0;
    private double laneposmeters = 0.0;
    private double oldlanepos = 0.0;
    private boolean inlane = true;
    private boolean autopilot = true;
    private double steerangle = 0.0;

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
        inlane = laneposition.inlane();
        laneposmeters = laneposition.value();
		steerangle = steeringwheel.value();
        /*
        // do control if in lane
        if (inlane) {
            // do steering control (if moving)
            if (speedmph > 1.0) {
                // proportionl control
                steerangle = -100 * laneposmeters;
                // differential control
                steerangle += -30 *(laneposmeters - oldlanepos) * speedmph;
                oldlanepos = laneposmeters;
                // set the new steering angle
                steeringwheel.set(steerangle);
                autopilot = true;
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
        */
    }

    /*******************************************
     * This method displays useful info on
     * the simulator screen.
     ********************************************/
    public void doDisplay() {
        String vels = Double.toString(speedmph);
        String lpos = Double.toString(laneposmeters);
        String sangle = Double.toString(steerangle);
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
    }
	
	public String[] getValues()
	{
		String logs[] = new String[3];
		logs[0] = (Double.toString(speedmph));
		logs[1] = (Double.toString(laneposmeters));
		logs[2] = (Double.toString(steerangle));
		return logs;
	}
}
