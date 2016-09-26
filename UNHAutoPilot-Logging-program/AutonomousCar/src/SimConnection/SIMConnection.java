package SimConnection;

import java.net.*;
import java.io.*;
import java.lang.Double;

/******************************************************
 *This class will create a server that will
 *communicate with a single client from the sim host.
 *This server will receive parameters from the sim host
 *and determine how to control it and send TCL
 *commands back to the client
 *
 *@author Tom Miller
 *@version 1.0
 *@since 6/22/2015
 ******************************************************/
public class SIMConnection {

    protected int portNumber;
    protected PrintWriter out;
    protected BufferedReader in;
    protected ServerSocket serverSocket;
    protected Socket clientSocket;
    protected String commandString;
    protected String feedbackString;
    protected int idcounter;
    protected int fbcounter;
    protected int idlimit;
    protected String errormessage;
    protected SIMFeedback simFeedback[];

    // parse string returned from simulator
    private static String str_piece(String str, char separator, int index) {
        String str_result = "";
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == separator) {
                count++;
                if (count == (index+1)) {
                    break;
                }
            } else {
                if (count == index) {
                    str_result += str.charAt(i);
                }
            }
        }
        return str_result;
    }

    // initialize common object components
    private void init_object() {
        portNumber = 4444;
        commandString = "puts $::sockchan \"$::synch\"\n";
        idcounter = 0;
        idlimit = 0;
        feedbackString = "";
        errormessage = "no errors";
        fbcounter = 0;
        simFeedback = new SIMFeedback[64];
    }

    // create connection using default port number
    public SIMConnection(){
        init_object();
    }

    // create connection object with new port number
    public SIMConnection(int port){
        init_object();
        portNumber = port;
    }

    /*******************************************
     * This method queues a TCL command to send
     * back the current value of a simulator
     * global variable.
     *
     * @param var The name of a TCL global variable
     * without the preceeding ::
     * @return An integer identifier to be used
     * to retrieve the value after the next
     * Java-TCL synchronization.
     ********************************************/
    public int getVariable(String var){
        commandString = commandString + "puts $::sockchan $::" + var + "\n";
        return ++idcounter;
    }

    /*******************************************
     * This method queues a TCL command to send
     * back the current value of a simulator
     * global variable.
     *
     * @param var The name of a TCL global variable
     * without the preceeding ::
     * @param sf The calling SIMFeedback object
     * @return An integer identifier to be used
     * to retrieve the value after the next
     * Java-TCL synchronization.
     ********************************************/
    public int getVariable(String var, SIMFeedback sf){
        commandString = commandString + "puts $::sockchan $::" + var + "\n";
        simFeedback[fbcounter++] = sf;
        return ++idcounter;
    }

    /*******************************************
     * This method queues a command to evaluate a
     * TCL expression and send back the result.
     *
     * @param exp The TCL expression to evaluate
     * @return An integer identifier to be used
     * to retrieve the value after the next
     * Java-TCL synchronization.
     ********************************************/
    public int getExpression(String exp){
        commandString = commandString + "puts $::sockchan [" + exp + "]\n";
        return ++idcounter;
    }

    /*******************************************
     * This method queues a command to evaluate a
     * TCL expression and send back the result.
     *
     * @param exp The TCL expression to evaluate
     * @param sf The calling SIMFeedback object
     * @return An integer identifier to be used
     * to retrieve the value after the next
     * Java-TCL synchronization.
     ********************************************/
    public int getExpression(String exp, SIMFeedback sf){
        commandString = commandString + "puts $::sockchan [" + exp + "]\n";
        simFeedback[fbcounter++] = sf;
        return ++idcounter;
    }

    /*******************************************
     * This method queues a TCL command to execute.
     *
     * @param cmd The TCL command to execute
     ********************************************/
    public void doCommand(String cmd){
        commandString = commandString + cmd + "\n";
    }

    /*******************************************
     * This method queues a command to change the
     * update rate of the synchronization event
     * (virtual trigger)in the TCL code. The new
     * rate will not take effect for 1 to 2 seconds.
     *
     * @param rate The desired rate in updates per
     * second. Note that this must be a factor of 60
     * (60, 30, 20, 15, 12, 10, 6, 5, 4, 3, 2, 1).
     ********************************************/
    public void changeRate(int rate){
        commandString = commandString + "set ::cntrate " + rate + "\n" +
                "TimerProcAdd ChangeRate 1\n";
    }

    /*******************************************
     * This method listens on a specified TCP port
     * for a connection request from the TCL
     * script in the simulator
     *
     * @return True for a successful connection,
     * false otherwise.
     ********************************************/
    public boolean acceptConnection(){
        try {
            serverSocket = new ServerSocket(portNumber);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            errormessage = e.getMessage();
            return false;
        }
        return true;
    }

    /*******************************************
     * This method sends all of the queued commands
     * to the connected TCL script in the simulator
     * and then waits for all of the requested
     * feedback.
     * @return True for a successful Java-TCL exchange,
     * false otherwise.
     ********************************************/
    public boolean synchSimulator() {
        String outputString = commandString + "set ::synch 0" + "\nSYNCH\n";
        commandString = "puts $::sockchan \"$::synch\"\n";
        idlimit = ++idcounter;
        idcounter = 0;
        try {
            int i;
            feedbackString = "";
            out.print(outputString);
            out.flush();
            //System.out.print(outputString);
            for (i=0; i<idlimit; ++i) {
                feedbackString = feedbackString + in.readLine() + ";";
                //System.out.println(inputString);
            }
        } catch (IOException e) {
            errormessage = e.getMessage();
            idlimit = 0;
            feedbackString = "";
            return false;
        }
        // update all SIMFeedback objects automatically
        int i;
        for (i=0; i<fbcounter; ++i){
            simFeedback[i].update();
        }
        fbcounter = 0;
        return true;
    }

    /*******************************************
     * This method returns error messages produced
     * by TCP socket calls during communication with
     * the simulator.
     *
     * @return error message from last failed TCP
     * operation.
     ********************************************/
    public String getMessage() {
        return errormessage;
    }

    /*******************************************
     * This method returns the number of simulator
     * cycles between successful synchronization
     * events between the java and the TCL.
     *
     * @return Normally the return value should be
     * 1. 0 means some failure occurred and 2+ means
     * the java code can't keep up with the synch rate.
     ********************************************/
    public int checkSynch() {
        if (idlimit > 0) {
            return Integer.valueOf(str_piece(feedbackString, ';', 0));
        }
        return 0;
    }

    /*******************************************
     * This method returns the string value of
     * a requested TCL global variable or TCL
     * expression evaluation.
     *
     * @param index The integer identifier returned
     * by the corresponding getVariable or getExpression
     * method.
     * @param err The String to return if something
     * goes wrong.
     * @return The requested String value.
     ********************************************/
    public String stringValueOf(int index, String err) {
        if ((index > 0) && (index < idlimit)) {
            return str_piece(feedbackString, ';', index);
        }
        return err;
    }

    /*******************************************
     * This method returns the integer value of
     * a requested TCL global variable or TCL
     * expression evaluation.
     *
     * @param index The integer identifier returned
     * by the corresponding getVariable or getExpression
     * method.
     * @param err The integer to return if something
     * goes wrong.
     * @return The requested integer value.
     ********************************************/
    public int intValueOf(int index, int err) {
        if ((index > 0) && (index < idlimit)) {
            return Integer.valueOf(str_piece(feedbackString, ';', index));
        }
        return err;
    }

    /*******************************************
     * This method returns the floating point value
     * of a requested TCL global variable or TCL
     * expression evaluation.
     *
     * @param index The integer identifier returned
     * by the corresponding getVariable or getExpression
     * method.
     * @param err The floating point value to return
     * if something goes wrong.
     * @return The requested double precision value.
     ********************************************/
    public double doubleValueOf(int index, double err) {
        if ((index > 0) && (index < idlimit)) {
            return Double.valueOf(str_piece(feedbackString, ';', index));
        }
        return err;
    }
}

