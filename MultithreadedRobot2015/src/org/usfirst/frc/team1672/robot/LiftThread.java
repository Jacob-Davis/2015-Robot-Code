package org.usfirst.frc.team1672.robot;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;

/*
 * class LiftThread:
 * A subclass of Thread that encapsulates the robot's interfacing with the lift.
 */
public class LiftThread extends Thread {
	private RobotDrive liftDrive;
	private boolean manualEnabled;
	private Joystick inputStick;
	private double desiredHeight;
	private double liftHeight;
	private MaxbotixUltrasonic liftSensor;
	private Master master;
	
	//constants
	public final int BTN_LIFT_ONE = 2;
	public final int BTN_LIFT_TWO = 3;
	public final int BTN_LIFT_THREE = 4;
	public final int BTN_LIFT_FOUR = 5;
	public final int BTN_LIFT_FIVE = 10;
	 
	public final double GROUND_SENSOR_DISTANCE = 0.1; //ground level
	public final double FIRST_SENSOR_DISTANCE = 12.1; //1st level
	public final double SECOND_SENSOR_DISTANCE = 24.2; //2nd level
	public final double THIRD_SENSOR_DISTANCE = 36.3; //3rd level
	public final double FOURTH_SENSOR_DISTANCE = 48.4; //4th level
	public final double FIFTH_SENSOR_DISTANCE = 56; //5th level (container)
	
	
	public LiftThread(Jaguar motor1, Jaguar motor2)
	{
		super("LiftThread"); //create the thread and name it "LiftThread"
		liftDrive = new RobotDrive(motor1, motor2);
		liftDrive.setSafetyEnabled(false);
		manualEnabled = false;
		inputStick = null;
		liftSensor = null;
		master = null;
		desiredHeight = GROUND_SENSOR_DISTANCE;
	}
	public void setJoystick(Joystick j)
	{
		inputStick = j;
	}
	public void setUltrasonic(MaxbotixUltrasonic u)
	{
		liftSensor = u;
	}
	public void setMaster(Master m)
	{
		master = m;
	}
	public void start()
	{
		
	}
	public void run()
	{
		System.out.println("lift thread run started");
		if(master != null)
		{
			while(master.operatorEnabled())
			{
				System.out.println("sjabfaub");
				desiredHeight = getInputHeight();
				liftHeight = getLiftHeight();
				if(liftHeight != -1.0)
				{
					//if the ultrasonic exists and is returning an accurate liftHeight
					//make the lift go to the desired height
					if(liftHeight < desiredHeight)
					{
						liftDrive.arcadeDrive(0.5, 0.0);
					}
					if(liftHeight > desiredHeight)
					{
						liftDrive.arcadeDrive(-0.5, 0.0);
					}
					if(liftHeight == desiredHeight)
					{
						liftDrive.arcadeDrive(0.0, 0.0);
					}
				}
				System.out.println("Joystick Y:" + inputStick.getY());
				//if the ultrasonic does not exist, button control is disabled
				liftDrive.arcadeDrive(inputStick.getY(), 0.0);
			}
		}
	}
	private double getLiftHeight()
    {
		if(liftSensor != null)
		{
			return liftSensor.getRangeInches();
		}
    	return -1.0;
    }
	private double getInputHeight()
    {
		if(inputStick != null)
		{
	    	if(inputStick.getTrigger())
			{
				desiredHeight = getLiftHeight(); //lock the lift height
			}
			if(inputStick.getRawButton(BTN_LIFT_ONE))
			{
				desiredHeight = GROUND_SENSOR_DISTANCE;
			}
			if(inputStick.getRawButton(BTN_LIFT_TWO))
			{
				desiredHeight = SECOND_SENSOR_DISTANCE;
			}
			if(inputStick.getRawButton(BTN_LIFT_THREE))
			{
				desiredHeight = THIRD_SENSOR_DISTANCE;
			}
			if(inputStick.getRawButton(BTN_LIFT_FOUR))
			{
				desiredHeight = FOURTH_SENSOR_DISTANCE;
			}
			if(inputStick.getRawButton(BTN_LIFT_FIVE))
			{
				desiredHeight = FIFTH_SENSOR_DISTANCE;
			}
		}
    	return desiredHeight;
    }
}
