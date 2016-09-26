package SimConnection;

import java.awt.Color;

/******************************************************
 *This class represents text displayed on the simulator
 *screen.
 *
 *@author Tom Miller
 *@version 1.0
 *@since 6/22/2015
 ******************************************************/
public class SIMDisplayText {

    private SIMConnection simConnection;
    private String thisname;
    private double x;
    private double y;
    private String channel;
    private double size;
    private Color tcolor;
    private String value;
    private String persistence;

    /*******************************************
     * The constructor
     *
     * @param sc The simulator connection object
     * @param name Any name to tag the displayed
     * text (needed by the TCL script)

     ********************************************/
    public SIMDisplayText(SIMConnection sc, String name) {
        simConnection = sc;
        thisname = name;
        x = 0.5;
        y = 0.5;
        channel = "AllChannels";
        persistence = "PERMANENT";
        size = 1.0;
        tcolor = Color.black;
    }

    /*******************************************
     * This method actually displays the text
     * using the previously defined properties.
     *
     * @param newtext The text to display.
     ********************************************/
    public void set(String newtext) {
        simConnection.doCommand(cmd(newtext));
    }

    /*******************************************
     * This method selects the simulator screen
     * (channel #) to display the text.
     *
     * @param ch The channel number (0, 1, 2, 3, ...)
     * or "AllChannels"
     ********************************************/
    public void channel(String ch) {
        channel = ch;
    }

    /*******************************************
     * This method selects the simulator screen
     * (channel #) to display the text.
     *
     * @param ch The channel number (0, 1, 2, 3, ...)
     ********************************************/
    public void channel(int ch) {
        channel = String.valueOf(ch);
    }

    /*******************************************
     * This method selects the persistence in
     * seconds for the displayed text.
     *
     * @param dur The persistence in seconds
     * as a string, or "PERMANENT"
     ********************************************/
    public void duration(String dur) {
        persistence = dur;
    }

    /*******************************************
     * This method selects the persistence in
     * seconds for the displayed text.
     *
     * @param dur The persistence in seconds
     ********************************************/
    public void duration(int dur) {
        persistence = String.valueOf(dur);
    }

    /*******************************************
     * This method selects the RGB color
     * for the displayed text.
     *
     * @param r The red component (0 - 255)
     * @param g The green component (0 - 255)
     * @param b The blue component (0 - 255)
     ********************************************/
    public void color(int r, int g, int b) {
        tcolor = new Color(r,g,b);
    }

    /*******************************************
     * This method selects the RGB color
     * for the displayed text.
     *
     * @param clr The Color object for the text
     ********************************************/
    public void color(Color clr) {
        tcolor = clr;
    }

    /*******************************************
     * This method selects the screen position
     * for the displayed text. Lower left is
     * (0.0 0.0) and upper right is (1.0 1.0)
     *
     * @param xn The x coordinate for the text
     * @param yn The x coordinate for the text
     ********************************************/
    public void position(double xn, double yn) {
        x = xn;
        y = yn;
    }

    /*******************************************
     * This method selects the relative size
     * for the displayed text. 1.0 is small but
     * legible.
     *
     * @param s The relative size the text
      ********************************************/
    public void size(double s) {
        size = s;
    }

    /*******************************************
     * This method formats the TCL command to
     * display the text as requested.
     *
     * @param newtext The text to display
     * @return The formatted TCL command
     ********************************************/
    private String cmd(String newtext) {
        return "VisualsDisplayText "+thisname+" "+channel+" "+x+" "+y+" "+size+
                " "+tcolor.getRed()+" "+tcolor.getGreen()+" "+tcolor.getBlue()+
                " "+persistence+" \""+newtext+"\"";
    }
}

