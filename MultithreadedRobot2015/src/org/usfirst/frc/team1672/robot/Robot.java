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
public class Robot extends SampleRobot implements LiftInterface {
	/*
	 * I just put ALL of the lift's function into a separate file. This cleans
	 * up the code a little bit. ~Andrew 1/21/15
	 */
	
	
	LiftThread lift;
	
	Joystick driveStick = new Joystick(0);
	Joystick liftStick = new Joystick(1);
	 
	public static Gyro roboGyro = new Gyro(10);
	double robotDegrees = roboGyro.getAngle();
	 
	AutoLibrary autoLib = new AutoLibrary();
	SmartDashboard dash = new SmartDashboard();
	SendableChooser autoChooser = new SendableChooser();
	String chosenAuto;
	 
	AxisCamera cam1 = new AxisCamera("10.16.72.2");
	public static AxisCamera.Resolution k640x360;
	
	private final int PORT_FL = 0;
	private final int PORT_RL = 1;
	private final int PORT_FR = 2;
	private final int PORT_RR = 3;
	private final int PORT_LIFT1 = 4;
	private final int PORT_LIFT2 = 5;
	private final int PORT_USONIC = 6;

	RobotDrive chassis = new RobotDrive(PORT_FL, PORT_RL, PORT_FR, PORT_RR);
	 
	Talon frontLeft = new Talon(PORT_FL);
	Talon frontRight = new Talon(PORT_RL);
	Talon rearLeft = new Talon(PORT_FR);
	Talon rearRight = new Talon(PORT_RR);
	
	RobotDrive liftDriver = new RobotDrive(PORT_LIFT1,PORT_LIFT2);
	Jaguar lift1 = new Jaguar(PORT_LIFT1);
	Jaguar lift2 = new Jaguar(PORT_LIFT2);
	
	Ultrasonic liftSensor = new Ultrasonic(PORT_USONIC, PORT_USONIC);
	public static double liftHeight; //in inches | totes = 12.1 in, containers = 29 in
	public static double desiredHeight;
	 
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
	 
	boolean isLiftReady = false;
	boolean stopLoop = false;
	boolean inputDetected = false;
	 
	public void robotInit() {
		lift = new LiftThread();
		lift.robot = this;
		 
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
	
		driveThread.start();
		lift.start();
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
        	lift.join();
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
    
    //----------interface methods---------------------------------//
    public double getInputHeight()
    {
    	if(liftStick.getTrigger())
		{
			desiredHeight = GROUND_SENSOR_DISTANCE;
		}
		if(liftStick.getRawButton(BTN_LIFT_ONE))
		{
			desiredHeight = FIRST_SENSOR_DISTANCE;
		}
		if(liftStick.getRawButton(BTN_LIFT_TWO))
		{
			desiredHeight = SECOND_SENSOR_DISTANCE;
		}
		if(liftStick.getRawButton(BTN_LIFT_THREE))
		{
			desiredHeight = THIRD_SENSOR_DISTANCE;
		}
		if(liftStick.getRawButton(BTN_LIFT_FOUR))
		{
			desiredHeight = FOURTH_SENSOR_DISTANCE;
		}
		if(liftStick.getRawButton(BTN_LIFT_FIVE))
		{
			desiredHeight = FIFTH_SENSOR_DISTANCE;
		}
    	return desiredHeight;
    }
    public void liftUp()
    {
    	liftDriver.arcadeDrive(0.5, 0.5);
    }
    public void liftDown()
    {
    	liftDriver.arcadeDrive(-0.5, -0.5);
    }
    public void liftStop()
    {
    	liftDriver.arcadeDrive(0.0, 0.0);
    }
    public void liftManualControl()
    {
    	liftDriver.arcadeDrive(liftStick.getY(), liftStick.getY());
    }
    public double getLiftHeight()
    {
    	return liftSensor.getRangeInches();
    }
    public boolean operatorIsEnabled()
    {
    	return isEnabled() && isOperatorControl();
    }
   
}


