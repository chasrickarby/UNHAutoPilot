package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/******************************************************
 * This class handles all the code and event listeners for the
 * navigation UI.
 *
 *@author Charles Rickarby, Eric Duross
 *@version 1.0
 *@since 2/27/17
 */
public class NavigationUI {

    public enum direction{
        left, right, straight, waiting;
    }

    private JButton straightButton;
    private JButton leftButton;
    private JButton rightButton;
    private JPanel panel2;
    private direction returnDirection;

    /*******************************************
     * The constructor.
     *
     ********************************************/
    public NavigationUI(){

        init();
        returnDirection = direction.waiting;
        straightButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                returnDirection = direction.straight;
                System.out.println("Straight");
            }
        });

        leftButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                returnDirection = direction.left;
                System.out.println("Left");
            }
        });

        rightButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                returnDirection = direction.right;
                System.out.println("Right");
            }
        });

    }

    /*******************************************
     * This method sets up the java client to display
     * the graphics for the navigation UI.
     *
     ********************************************/
    private void init(){
        JFrame frame = new JFrame("Navigation UI");
        Dimension minSize = new Dimension(437, 304);
        frame.setMinimumSize(minSize);
        frame.setContentPane(panel2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /*******************************************
     * This method retrieves the user input from the UI.
     *
     * @return Returns the direction that was indicated
     * by the user.
     ********************************************/
    public direction getUserInput(){
        return returnDirection;
    }

    /*******************************************
     * This method resets the UI to "waiting" after the
     * Autonomous Control class successfully retrieves the
     * direction specified by the user.
     *
     ********************************************/
    public void resetDirection(){
        returnDirection = direction.waiting;
    }
}
