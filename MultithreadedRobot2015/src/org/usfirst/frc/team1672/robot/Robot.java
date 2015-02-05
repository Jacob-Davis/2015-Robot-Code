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
 * 
 * 
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends SampleRobot implements Master {
	LiftThread lift;
	DriveThread manualDrive;
	
	Joystick driveStick = new Joystick(0);
	Joystick liftStick = new Joystick(1);
	 
	//public Gyro roboGyro = new Gyro(10);
	//double robotDegrees;
	 
	AutoLibrary autoLib = new AutoLibrary();
	SmartDashboard dash = new SmartDashboard();
	SendableChooser autoChooser = new SendableChooser();
	String chosenAuto;
	
	//AxisCamera cam1 = new AxisCamera("10.16.72.2");
	//public AxisCamera.Resolution k640x360;
	
	
	/*-------------------------------------------------------------------------------------------*/
	/*Please, put all PWM ports used here so we never accidentally allocate 1 port more than once*/
	/*-------------------------------------------------------------------------------------------*/
	private final int PORT_FL = 0;
	private final int PORT_RL = 1;
	private final int PORT_FR = 2;
	private final int PORT_RR = 3;
	private final int PORT_LIFT1 = 4;
	private final int PORT_LIFT2 = 5;
	//ultrasonic digital channels
	private final int PORT_USONIC = 1;
	
	/*--------------------------------------------------------------------------*/
	/* 						Other constants										*/
	/*--------------------------------------------------------------------------*/
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

	Talon frontLeft = new Talon(PORT_FL);
	Talon frontRight = new Talon(PORT_FR);
	Talon rearLeft = new Talon(PORT_RL);
	Talon rearRight = new Talon(PORT_RR);
	RobotDrive chassis = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
	
	Jaguar lift1 = new Jaguar(PORT_LIFT1);
	Jaguar lift2 = new Jaguar(PORT_LIFT2);
	RobotDrive liftDriver = new RobotDrive(lift1, lift2);
	
	MaxbotixUltrasonic liftSensor = new MaxbotixUltrasonic(PORT_USONIC);
	public double liftHeight; //in inches | totes = 12.1 in, containers = 29 in
	public double desiredHeight;
	
	boolean isLiftReady = false;
	boolean stopLoop = false;
	boolean inputDetected = false;
	
	public Robot()
	{
		chassis.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
    	chassis.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
    	
    	manualDrive = new DriveThread(chassis);
    	
		//liftSensor.setAutomaticMode(true);
		//liftSensor.setEnabled(true);
		liftDriver.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
		lift = new LiftThread(lift1, lift2);
		lift.setMaster(this);
		lift.setJoystick(liftStick);
		lift.setUltrasonic(liftSensor);
		
		autoChooser.addDefault("Right", "autoRight");
		autoChooser.addObject("Middle", "autoMiddle");
		autoChooser.addObject("Left", "autoLeft");
		SmartDashboard.putData("Autonomous Code Chooser", autoChooser);
		
		System.out.println("Robot construction successful!");
	}
	 
	public void robotInit() {}
	
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
    	
    	//robotDegrees = roboGyro.getAngle(); //TODO: put this in a sensor thread
    	
    	
    	chassis.setSafetyEnabled(false);
		//liftSensor.setAutomaticMode(true);
		//liftSensor.setEnabled(true);
	
		//manualDrive.start();
    	System.out.println("OPERATOR CONTROL STARTED--------------------------");
    	System.out.println("Robot ma:" + this);
		Thread liftThread = new Thread(lift);
		Thread driveThread = new Thread(manualDrive);
		liftThread.start();
        try
        {
        	//Thread.join() makes it so that operatorControl()
        	// continues to run as long as the threads it is joined to are running.
        	// This makes a while loop in operatorControl() unnecessary
        	// as long as there are while loops in the joined threads.
        	//manualDrive.join();
        	liftThread.join();
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
			System.out.println(testHeight + " inches" + "|| voltage:" + liftSensor.getVoltage());
		}
	}
    
    //----------interface methods---------------------------------//
    public void driveManualControl()
    {
    	chassis.mecanumDrive_Polar(driveStick.getMagnitude(), driveStick.getDirectionDegrees(), driveStick.getTwist());
    }
    public boolean operatorEnabled()
    {
    	return isEnabled() && isOperatorControl();
    }
    public boolean isAutonomousControl()
    {
    	return isEnabled() && isAutonomous();
    }
}


