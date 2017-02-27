package SimUtilities;

/******************************************************
 * This class creates periodic timers with no integral error.
 *
 *@author Tom Miller
 *@version 1.0
 *@since 7/15/2015
 */
public class Timer {
    private long interval;
    private long basetime;
    private long timertic;
    private boolean btimeout;

    /*******************************************
     * The constructor for a periodic timer
     *
     * @param period The timer period in milliseconds.
     ********************************************/
    public Timer(long period) {
        interval = period;
        timertic = 1;
        basetime = System.currentTimeMillis();
        btimeout = false;
    }

    /*******************************************
     * The constructor for a free running timer
     *
     ********************************************/
    public Timer() {
        interval = 1000000000;
        timertic = 1;
        basetime = System.currentTimeMillis();
        btimeout = false;
    }

    /*******************************************
     * This method updates the timer state.
     *
     ********************************************/
    public void update() {
        if ((System.currentTimeMillis()-basetime) >= (timertic*interval)) {
            while ((System.currentTimeMillis()-basetime) >= (timertic*interval)) {
                timertic += 1;
            }
            btimeout = true;
        } else {
            btimeout = false;
        }
    }

    /*******************************************
     * This method returns the timeout state of
     * the timer. True means the timer period
     * expired this cycle, false means it did not.
     *
     * @return The state of the timer (true or false).
     ********************************************/
    public boolean timeout() {
        return btimeout;
    }

    /*******************************************
     * This method returns the number of milliseconds
     * since the last timer timeout.
     *
     * @return The number of milliseconds
     * since the last timer timeout.
     ********************************************/
    public long elapsed() {
        return ((System.currentTimeMillis()-basetime) - ((timertic-1)*interval));
    }

    /*******************************************
     * This method returns the number of milliseconds
     * since the timer was created (or reset).
     *
     * @return The total number of milliseconds
     * since the timer started.
     ********************************************/
    public long time() {
        return (System.currentTimeMillis()-basetime);
    }

    /*******************************************
     * This method returns the number of intervals
     * since the timer was created (or reset).
     *
     * @return The number of time intervals completed
     ********************************************/
    public long tics() {
        return (timertic-1);
    }

    /*******************************************
     * This method resets the timer state.
     *
     ********************************************/
    public void reset(){
        timertic = 1;
        basetime = System.currentTimeMillis();
        btimeout = false;
    }

}
