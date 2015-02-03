package org.usfirst.frc.team1672.robot;

import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.AnalogInput;


/**
 * MaxbotixUltrasonic
 * 
 * Custom class to talk to the Maxbotix Ultrasonic that came with the 2012 kit, because it doesn't
 * work with the Ultrasonic class in the WPILib.
 */
public class MaxbotixUltrasonic extends SensorBase {
	
	//constants
	//public final double IN_PER_CM = 2.54; we're not using CM so whatever
	
	private double minVoltage;	  //Minimum voltage the ultrasonic sensor can return
    private double voltageRange; //The range of the voltages returned by the sensor (maximum - minimum)
    private double minDistance;  //Minimum distance the ultrasonic sensor can return in inches
    private double distanceRange;//The range of the distances returned by this class in inches (maximum - minimum)
    private AnalogInput inChannel; //the analog channel that ultrasonic's plugged into, has both input AND output, disregard class name
    
    
    /**
     * MaxbotixUltrasonic class
     * This class provides a hook for the analog Maxbotix Ultrasonic sensor that came with the 
     * 2012 FRC kit, since apparently it doesn't work with the stupid Ultrasonic class that FRC provided.
     */
    public MaxbotixUltrasonic(int channel)
    {
    	inChannel = new AnalogInput(channel);
    	minVoltage = 0.5;
    	voltageRange = 5.0 - minVoltage;
    	minDistance = 3.0;
    	distanceRange = 60.0 - minDistance;
    }
    public double getRangeInches()
    {
    	double range = inChannel.getVoltage();
    	//normalize voltage
    	//in other words, make sure voltage doesn't include the voltage sensed if nothing was in front of the sensor
    	range = (range - minVoltage)/voltageRange;
    	//denormalize to unit range
    	range = (range * distanceRange) + minDistance;
    	return range;
    }
}
