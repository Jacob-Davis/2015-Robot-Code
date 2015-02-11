package org.usfirst.frc.team1672.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;

public class DriveThread implements Runnable {
	private RobotDrive drive;
	private Joystick inputStick;
	private Master master;
	private double snapAngle; //the direction the robot drives on can only be multiples of this angle
	private double rotSensitivity; //the sensitivity of rotation
	private double strafeSensitivity; //the sensitivity of moving forward/backward and side-to-side
	private boolean allowSensitivityThrottle;
	public DriveThread(RobotDrive inputRobotDrive)
	{
		drive = inputRobotDrive;
		drive.setSafetyEnabled(false);
		master = null;
		inputStick = null;
		snapAngle = 0.0;
		rotSensitivity = 0.5;
		strafeSensitivity = 0.5;
		allowSensitivityThrottle = false;
	}
	public void setMaster(Master m)
	{
		master = m;
	}
	public void setDriveStick(Joystick j)
	{
		inputStick = j;
	}
	/**
	 * Set the angle for the robot to snap to.
	 * For instance, if the snap angle is 45 degrees, the robot
	 * will only travel on whatever multiple of 45 degrees is 
	 * closest to the angle the joystick is pointing at.
	 */
	public void setSnapAngle(double inputSnapAngle)
	{
		snapAngle = inputSnapAngle;
	}
	/**
	 * Set the sensitivity of using the joystick to rotate the robot.
	 */
	public void setRotationSensitivity(double sensitivity)
	{
		if(sensitivity > 0.0 && sensitivity <= 1.0)
		{
			rotSensitivity = sensitivity;
		}
	}
	public void setStrafeSensitivity(double sensitivity)
	{
		if(sensitivity > 0.0 && sensitivity <= 1.0)
		{
			strafeSensitivity = sensitivity;
		}
	}
	public void start() 
	{
		System.out.println("Drive Thread started----------");
	}
	public void run()
	{
		System.out.println("drive thread run started");
		if(master != null)
		{
			while(master.operatorEnabled())
			{
				if(inputStick != null)
				{
					drive.mecanumDrive_Polar(
							inputStick.getMagnitude(), 
							getSnappedAngle(), 
							inputStick.getTwist()
							);
									
				}
			}
		}
	}
	private double getModifiedTwist()
	{
		if(allowSensitivityThrottle)
		{
			return inputStick.getTwist() * inputStick.getThrottle();
		}
		return inputStick.getTwist() * rotSensitivity;
	}
	private double getSnappedAngle()
	{
		if(snapAngle < 0.5)
			//don't bother snapping less than this because the difference is negligible
			//and small numbers cause problems with floating-point calculations
		{
			return inputStick.getDirectionDegrees();
		}
		double finalAngle = Math.round(inputStick.getDirectionDegrees()/snapAngle) * snapAngle;
		return finalAngle;
	}
}
