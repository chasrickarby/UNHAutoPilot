package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Project54 on 12/1/2016.
 */
public class ControlUI {
    private JSlider feedforward;
    private JPanel panel1;
    private JSlider feedback;
    private JLabel forwardVal;
    private JLabel backVal;
    private JLabel currentSpeed;
    private JLabel currentSpeedLimit;
    private JLabel centerOffset;
    private JLabel steerAngle;
    private JLabel positionXYZ;
    private JLabel brakeAngle;
    private JLabel gasAngle;
    private JTextField desiredSpeedTextField;

    private double feedforwardVal;
    private double feedbackVal;
    private double desiredSpeed = -1;

    /*******************************************
     * The constructor
     *
     ********************************************/
    public ControlUI(){
        init();
        updateValues();
        desiredSpeedTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Desired Speed Set: " + desiredSpeedTextField.getText());
                desiredSpeed = Double.parseDouble(desiredSpeedTextField.getText());
            }
        });
    }

    /*******************************************
     * This method gets the value of the feed-forward
     * slider from the UI.
     *
     * @return The value of the feed-forward slider.
     ********************************************/
    public double getFeedforwardVal(){
        return feedforwardVal;
    }

    /*******************************************
     * This method gets the value of the feed-back
     * slider from the UI.
     *
     * @return The value of the feed-back slider.
     ********************************************/
    public double getFeedbackVal(){
        return feedbackVal;
    }

    /*******************************************
     * This method gets the value the desired speed,
     * if altered in the UI text field.
     *
     * @return The value of the new desired speed.
     ********************************************/
    public double getDesiredSpeed(){return desiredSpeed;}

    /*******************************************
     * This method updates the values of the sliders for use
     * in the Autonomous Control class.
     *
     ********************************************/
    public void updateValues(){
        feedforwardVal = ((double) feedforward.getValue() / 100);
        feedbackVal = ((double) feedback.getValue() / 100);

        forwardVal.setText("" + feedforwardVal);
        backVal.setText("" + feedbackVal);
    }

    /*******************************************
     * This method sets the values of the variables to be displayed in
     * the control UI.
     *
     ********************************************/
    public void doDisplay(double curSpeed, double curSpeedLimit, double ctrOffset, double wheelAngle, double gAngle, double bAngle, double xPos, double yPos, double zPos){
        currentSpeed.setText("" + (int)curSpeed);
        currentSpeedLimit.setText("" + (int)curSpeedLimit);

        centerOffset.setText("" + ctrOffset);
        steerAngle.setText("" + wheelAngle);

        gasAngle.setText("" + gAngle);
        brakeAngle.setText("" + bAngle);

        positionXYZ.setText("("+ xPos + ", " + yPos + ", " + zPos + ")");
    }

    /*******************************************
     * This method initializes the UI to display the
     * control UI java frame.
     *
     ********************************************/
    private void init(){
        JFrame frame = new JFrame("Control UI");
        Dimension minSize = new Dimension(550, 400);
        frame.setMinimumSize(minSize);
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    /*******************************************
     * This method sets the initial values of the two sliders,
     * feed-forward and feed-back.
     *
     ********************************************/
    public void setInitialValues(double fforward, double fback) {
        feedback.setValue((int) (fback * 100));
        feedforward.setValue((int) (fforward * 100));
        updateValues();
    }

}
