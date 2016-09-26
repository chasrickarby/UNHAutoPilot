package AutonomousCar;

import java.io.*;

import java.lang.Math;
import java.lang.Double;
import java.awt.*;

import SimConnection.*;
import SimUtilities.*;

public class Main {

    public static void main(String[] args) throws IOException {

        //Set the port that the server will open and use for TCP communications
        SIMConnection simConnection = new SIMConnection(4444);

        // Define the controller object
        AutonomousControl autoControl = new AutonomousControl(simConnection);

        boolean synched = true;

        System.out.println("Starting Server\n");

        // wait here for simulator to connect
        if (!simConnection.acceptConnection()) {
            System.err.println("TCP connection to simulator failed!");
            System.err.println(simConnection.getMessage());
            return;
        }
        System.out.println("Connection Established");

        // set the control update rate
        simConnection.changeRate(30);

        // start timers for pacing things
        Timer t100 = new Timer(100);
        Timer t1000 = new Timer(1000);
		Timer t10 = new Timer (10);
		PrintWriter writer = new PrintWriter("C:\\Users\\Project54\\Desktop\\log.txt", "UTF-8");

        // the main control loop
        while (true) {

            // update the periodic timers once each cycle
            t100.update();
            t1000.update();
			t10.update();
            // get feedback for control
            autoControl.getFeedback();

            // communicate/synchronize with the simulator
            if (!simConnection.synchSimulator()) {
                System.err.println("TCP exchange with simulator failed!");
                System.err.println(simConnection.getMessage());
                return;
            } else {
                // verify synchronization with simulator OK
                if (simConnection.checkSynch() != 1) {
                    synched = false;
                }
                // display the synch error 1 times per second
                if (t1000.timeout()) {
                    if (!synched) {
                        synched = true;
                        System.err.println("WARNING: Simulation synchronization failure");
                    }
                }
            }

            // compute next control actions based on feedback
            autoControl.doControl();

            // display controller state 10 times per second
            if (t100.timeout()) {
                autoControl.doDisplay();
            }
			if(t10.timeout())
			{
				String loginfo[] = autoControl.getValues();
				for(int i = 0; i < loginfo.length; i++)
				{
					writer.print(loginfo[i] + " ");
                    writer.println();
				}
			}

        }
    }
}
