/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team1672.robot;

import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.vision.AxisCamera;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.Gyro;

//import edu.wpi.first.wpilibj.buttons.JoystickButton; Not needed (using raw input)


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends SampleRobot {
	
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
	
		 Joystick driveStick = new Joystick(0);
		 Joystick liftStick = new Joystick(1);
		 
		 public static Gyro roboGyro = new Gyro(10);
		 double robotDegrees = roboGyro.getAngle();
		 
		 SmartDashboard dash = new SmartDashboard();
		 SendableChooser autoChooser = new SendableChooser();
		 String chosenAuto;
		 
		 AxisCamera cam1 = new AxisCamera("10.16.72.2");
		 
		 public static AxisCamera.Resolution k640x360;
	
		 RobotDrive chassis = new RobotDrive(0, 1, 2, 3);
		 
		 Talon frontLeft = new Talon(0);
		 Talon frontRight = new Talon(1);
		 Talon rearLeft = new Talon(2);
		 Talon rearRight = new Talon(3);
		 
		 /*
		  *Do NOT extend LiftOperator from Robot. This creates an infinite inclusion loop
		  * where LiftOperator includes itself from Robot, which includes itself again, and again,
		  * etc. Also, extending classes does not give them access to the base class's
		  * variables. It gives them COPIES of the base class variables.
		  * I've made LiftOperator a member class of Robot, which gives the same functionality.
		  * ~Andrew, 1/19/2015
		  */
		 class LiftOperator
		 {
			 public void getLiftInput() {
				 if(liftStick.getTrigger() || liftStick.getRawButton(2) || liftStick.getRawButton(3) || liftStick.getRawButton(4) || liftStick.getRawButton(5) || liftStick.getRawButton(10)) {
					stopLoop = true;		
					lift1.stopMotor();
					lift2.stopMotor();
				}
			 }
			 public void toGround() {
				desiredHeight = GROUND_SENSOR_DISTANCE;
				liftHeight = liftSensor.getRangeInches();
				if(liftHeight > desiredHeight) {
					liftDriver.arcadeDrive(-0.5, -0.5);
				}
			  }
			 public void toFirst() {
				desiredHeight = FIRST_SENSOR_DISTANCE;
				liftHeight = liftSensor.getRangeInches();
				if(liftHeight < desiredHeight) {
				liftDriver.arcadeDrive(0.5, 0.5);
				} 
				if(liftHeight > desiredHeight) {
				liftDriver.arcadeDrive(-0.5, -0.5);
				}
			 }
			 public void toSecond() {
				desiredHeight = SECOND_SENSOR_DISTANCE;
				liftHeight = liftSensor.getRangeInches();
				if(liftHeight < desiredHeight) {
				liftDriver.arcadeDrive(0.5, 0.5);
				} 
				if(liftHeight > desiredHeight) {
				liftDriver.arcadeDrive(-0.5, -0.5);
				}
			 }
			 public void toThird() {
				desiredHeight = THIRD_SENSOR_DISTANCE;
				liftHeight = liftSensor.getRangeInches();
				if(liftHeight < desiredHeight) {
				liftDriver.arcadeDrive(0.5, 0.5);
				} 
				if(liftHeight > desiredHeight) {
				liftDriver.arcadeDrive(-0.5, -0.5);
				}
			 }
			 public void toFourth() {
				desiredHeight = FOURTH_SENSOR_DISTANCE;
				liftHeight = liftSensor.getRangeInches();
				if(liftHeight < desiredHeight) {
				liftDriver.arcadeDrive(0.5, 0.5);
				} 
				if(liftHeight > desiredHeight) {
				liftDriver.arcadeDrive(-0.5, -0.5);
				}
			 }
			 public void toFifth() {
				desiredHeight = FIFTH_SENSOR_DISTANCE;
				liftHeight = liftSensor.getRangeInches();
				if(liftHeight < desiredHeight) {
				liftDriver.arcadeDrive(0.5, 0.5);
				} 
				if(liftHeight > desiredHeight) {
				liftDriver.arcadeDrive(-0.5, -0.5);
				}
			}
		 }
		 
		 LiftOperator lifter = new LiftOperator();
		 AutoLibrary autoLib = new AutoLibrary();
		 
		 RobotDrive liftDriver = new RobotDrive(5,6);
		 
		 Jaguar lift1 = new Jaguar(5);
		 Jaguar lift2 = new Jaguar(6);
		 
		 public final double GROUND_SENSOR_DISTANCE = 0.1; //ground level
		 public final double FIRST_SENSOR_DISTANCE = 12.1; //1st level
		 public final double SECOND_SENSOR_DISTANCE = 24.2; //2nd level
		 public final double THIRD_SENSOR_DISTANCE = 36.3; //3rd level
		 public final double FOURTH_SENSOR_DISTANCE = 48.4; //4th level
		 public final double FIFTH_SENSOR_DISTANCE = 56; //5th level (container)
		 
		 Ultrasonic liftSensor = new Ultrasonic(7, 7);
		 public static double liftHeight; //in inches | totes = 12.1 in, containers = 29 in
		 public static double desiredHeight;
		 
		 boolean isLiftReady = false;
		 boolean stopLoop = false;
		 
		 public void robotInit() {
		    	chassis.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
		    	chassis.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
		    	
				liftSensor.setAutomaticMode(true);
				liftSensor.setEnabled(true);
				
				liftDriver.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
				
				autoChooser.addDefault("Right", "autoRight");
				autoChooser.addObject("Middle", "autoMiddle");
				autoChooser.addObject("Left", "autoLeft");
				SmartDashboard.putData("Autonomous Code Chooser", autoChooser);
		 }
		 
		 
	 
	 //SampleRobot does not support an autonomousInit() method -
	 //this method is used by IterativeRobot only.
	//I've put this code at the beginning of autonomous(), which does the same thing
	 /*public void autonomousInit() {
		 
		 chosenAuto = (String) autoChooser.getSelected();
	 }*/
	
    public void autonomous() {
    	chosenAuto = (String) autoChooser.getSelected();
    	chassis.setSafetyEnabled(false);
    	
    	switch(chosenAuto)
    	{
	    	case("autoRight"):
	    	{
	    		autoLib.runAutoRight();
	    		break;
	    	}
	    	case("autoMiddle"):
	    	{
	    		autoLib.runAutoMiddle();
	    		break;
	    	}
	    	default:
	    	{
	    		autoLib.runAutoLeft();
	    		break;
	    	}
    	}
    }//<---end autonomous() method

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
		liftSensor.setAutomaticMode(true);
		liftSensor.setEnabled(true);

		Thread driveThread = new Thread() {
			public void start() {
			}
			public void run() {
				while(isOperatorControl() && isEnabled())
				{
					chassis.mecanumDrive_Polar(driveStick.getMagnitude(), driveStick.getDirectionDegrees(), driveStick.getTwist());

				}
				
			}
		};
		
		Thread liftThread = new Thread() {
			private Joystick liftStick;
			public void start(){
				liftStick = new Joystick(1);
			}
			public void run() {
				while(isOperatorControl() && isEnabled())
				{
					if(liftStick.getTrigger()){
						lifter.toGround();
						while(liftHeight >= (desiredHeight + 2) || liftHeight <= (desiredHeight - 2) && stopLoop == false) {
								lifter.toGround();
								lifter.getLiftInput();
							}
						stopLoop = false;
						}
					if(liftStick.getRawButton(2)) {
						lifter.toFirst();
						while(liftHeight >= (desiredHeight + 2) || liftHeight <= (desiredHeight - 2) && stopLoop == false) {
								lifter.toFirst();
								lifter.getLiftInput();
							}
						stopLoop = false;
						}
						if(liftStick.getRawButton(3)) {
						lifter.toSecond();
						while(liftHeight >= (desiredHeight + 2) || liftHeight <= (desiredHeight - 2) && stopLoop == false) {
								lifter.toSecond();
								lifter.getLiftInput();
							}
						stopLoop = false;
						}
						if(liftStick.getRawButton(4)) {
						lifter.toThird();
						while(liftHeight >= (desiredHeight + 2) || liftHeight <= (desiredHeight - 2) && stopLoop == false) {
								lifter.toThird();
								lifter.getLiftInput();
							}
						stopLoop = false;
						}
						if(liftStick.getRawButton(5)) {
						lifter.toFourth();
						while(liftHeight >= (desiredHeight + 2) || liftHeight <= (desiredHeight - 2) && stopLoop == false) {
								lifter.toFourth();
								lifter.getLiftInput();
							}
						stopLoop = false;
						}
						if(liftStick.getRawButton(10)) {
						lifter.toFifth();
						while(liftHeight >= (desiredHeight + 2) || liftHeight <= (desiredHeight - 2) && stopLoop == false) {
								lifter.toFifth();
								lifter.getLiftInput();
							}
						stopLoop = false;
						}
				}
				
			} //<--end run method
		};
		
		Thread liftAxisThread = new Thread() {
			public void start() {
				}
				public void run() {
					while(isOperatorControl() && isEnabled())
					{
						liftDriver.arcadeDrive(liftStick.getY(), liftStick.getY());
					}
			}
		};
	
		driveThread.start();
		liftThread.start();
		liftAxisThread.start();
		chassis.setSafetyEnabled(true);
			
		
        try
        {
        	/*
        	 * Thread.join() makes it so that the main method, which is operatorControl()
        	 * continues to run as long as the threads it is joined to are running.
        	 * This makes a while loop in operatorControl() unnecessary,
        	 * as long as there are while loops in the joined threads, which I already
        	 * added.
        	 * Try and catch are required by java here.
        	 * ~Andrew, 1/19/2013
        	 */
        	driveThread.join();
        	liftThread.join();
        	liftAxisThread.join();
        }
        catch(InterruptedException ie)
        {
        	System.err.println("Failure joining threads due to InterruptedException!");
        }
	}
										
  /**
     * This function is called once each time the robot enters test mode.
    */
    public void test() {
			
				double testHeight;
				while (isTest() && isEnabled()) {
					testHeight = liftSensor.getRangeInches();
					System.out.println(testHeight + " inches");
		}
	}
}