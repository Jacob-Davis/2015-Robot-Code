package org.usfirst.frc.team1672.robot;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Timer;

public class AutoLibrary {
	public static double s_desiredLiftHeight = 0.1;
	
	RobotDrive chassis;
	RobotDrive lift;
	Jaguar liftMotor;
	MaxbotixUltrasonic liftSensor;
	
	public AutoLibrary(RobotDrive chassisDrive, RobotDrive liftDrive, int PORT_USONIC)
	{
		chassis = chassisDrive;
		lift = liftDrive;
		liftMotor = null;
		liftSensor = new MaxbotixUltrasonic(PORT_USONIC);
	}
	public AutoLibrary(RobotDrive chassisDrive, Jaguar lift1, int PORT_USONIC)
	{
		chassis = chassisDrive;
		liftMotor = lift1;
		liftSensor = new MaxbotixUltrasonic(PORT_USONIC);
	}
	
	/**
	 * Makes the robot go forward for 5 seconds and then stop. No lifting.
	 */
	public void goForwardNoLift()
	{
		driveForwardFor(5.0);
	}

	public void runAutoRight() {
		//autonomous for far right side from driver station goes here
		driveForwardFor(1.0); //drive to the tote
		
		strafeRightFor(5.0); //get to the autozone
		
	}
	
	public void runAutoMiddle() {
	//autonomous for middle goes here
		
	}
	
	public void runAutoLeft() {
	//autonomous for far left side from driver station goes here
		
	}
	
	public void driveForwardFor(double seconds)
	{
		chassis.mecanumDrive_Polar(1.0, 0.0, 0.0);
		Timer.delay(seconds);
		chassis.mecanumDrive_Polar(0.0, 0.0, 0.0);
	}
	public void strafeRightFor(double seconds)
	{
		chassis.mecanumDrive_Cartesian(1.0, 0.0, 0.0, 0.0);
		Timer.delay(seconds);
		chassis.mecanumDrive_Cartesian(0.0, 0.0, 0.0, 0.0);
	}
	public void strafeLeftFor(double seconds)
	{
		chassis.mecanumDrive_Cartesian(-1.0, 0.0, 0.0, 0.0);
		Timer.delay(seconds);
		chassis.mecanumDrive_Cartesian(0.0, 0.0, 0.0, 0.0);
	}
	
	/**
	 * This method is not simultaneous with driving. Drive will not resume until
	 * lift has reached the desired height.
	 */
	public void liftToTop()
	{
		if(lift != null)
		{
			double liftHeight = liftSensor.getRangeInches();
			if(lift!=null) {lift.arcadeDrive(0.5, 0.0);}
			while(true)
			{
				if(liftHeight < 50.0)
				{
					if(lift!=null){lift.arcadeDrive(0.5, 0.0);}
					else{liftMotor.set(0.5);}
				}
				if(liftHeight > 50.0)
				{
					if(lift!=null){lift.arcadeDrive(-0.5, 0.0);}
					else{liftMotor.set(-0.5);}
				}
				if(Math.abs(liftHeight - 50.0) <= 0.25)
				{
					if(lift!=null){lift.arcadeDrive(0.0, 0.0);}
					else{liftMotor.set(0.0);}
					break;
				}
			}
		}
	}
}
