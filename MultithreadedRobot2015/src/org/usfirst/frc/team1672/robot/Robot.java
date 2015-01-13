
package org.usfirst.frc.team1672.robot;


import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

/**
 * This is a demo program showing the use of the RobotDrive class.
 * The SampleRobot class is the base of a robot application that will automatically call your
 * Autonomous and OperatorControl methods at the right time as controlled by the switches on
 * the driver station or the field controls.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're inexperienced,
 * don't. Unless you know what you are doing, complex code will be much more difficult under
 * this system. Use IterativeRobot or Command-Based instead if you're new.
 */
public class Robot extends SampleRobot {
	//drivetrain
	RobotDrive chassis;
	
	//motors
	Talon frontLeft;
	Talon frontRight;
	Talon rearLeft;
	Talon rearRight;
	Jaguar lift;
	Jaguar test;
	
	//joysticks/inputs
    Joystick driveStick;
	Joystick liftStick;
	
	//GUI elements
	SmartDashboard dash;
	SendableChooser autoChooser;
	
	//sensors n' mutable sensor data
	Ultrasonic liftSensor;
	private double liftHeight;
	private double desiredHeight;
	
	//constants
	//button IDs
	public final int BTN_LIFT_GROUND = 1;
	public final int BTN_LIFT_ONE = 2; //Right stick only
	public final int BTN_LIFT_TWO = 3; //Right stick only
	public final int BTN_LIFT_THREE = 4;
	public final int BTN_LIFT_FOUR = 5;
	public final int BTN_LIFT_FIVE = 10;
	//lift levels
	public final double GROUND_SENSOR_DISTANCE = 0.1; //ground level
	public final double FIRST_SENSOR_DISTANCE = 12.1; //1st level
	public final double SECOND_SENSOR_DISTANCE = 24.2; //2nd level
	public final double THIRD_SENSOR_DISTANCE = 36.3; //3rd level
	public final double FOURTH_SENSOR_DISTANCE = 48.4; //4th level
	public final double FIFTH_SENSOR_DISTANCE = 56; //5th level (container)
	
	//thread definitions
	//cleaner to define threads here, instead of in operatorControl() ~andrew
	//NOTE: in java, member classes can see other parent class members
	class DriveThread extends Thread
	{
		public void start() {}
		public void run() 
		{ 
			//yes, we still need a while loop inside the thread's run() method.
			//if the run() method looped by itself it wouldn't know when to stop
			while(isEnabled() && isOperatorControl())
			{
				chassis.mecanumDrive_Polar(driveStick.getMagnitude(), driveStick.getDirectionDegrees(), driveStick.getTwist()); 
			}
		}
	}
	class LiftThread extends Thread
	{
		private boolean inputDetected; 
		public void start() { inputDetected = false; }
		public void run()
		{
			while(isEnabled() && isOperatorControl())
			{
				liftHeight = liftSensor.getRangeInches();
				//I do not at all endorse the amount of if-statements here ~andrew
				
				if(liftStick.getRawButton(BTN_LIFT_GROUND))
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
				
				while(liftHeight != desiredHeight && inputDetected == false)
				{
					if(liftHeight < desiredHeight)
						lift.set(0.5);
					if(liftHeight > desiredHeight)
						lift.set(-0.5);
					//stops lift motion if any input detected
					//this is real heavy-handed, i don't see why we can't just use the other way
					for(int i=1; i<=liftStick.getButtonCount();i++)
					{
						if(liftStick.getRawButton(i))
						{
							inputDetected = true;
							break;
						}
					}
					liftHeight = liftSensor.getRangeInches();
				}
				inputDetected = false;
				lift.set(0.0);
			} //<-----end thread run loop
		}
	}

    public Robot() {
        chassis = new RobotDrive(1, 2, 3, 4);
        
        frontLeft = new Talon(1);
        frontRight = new Talon(2);
        rearLeft = new Talon(3);
        rearRight = new Talon(4);
        
        lift = new Jaguar(5);
        lift = new Jaguar(11);
        
        driveStick = new Joystick(0);
        liftStick = new Joystick(1);
        
        dash = new SmartDashboard();
        autoChooser = new SendableChooser();
        
        liftSensor = new Ultrasonic(6, 6);
    }

    /**
     * Drive for 2 seconds then stop
     */
    public void autonomous() {
    	liftSensor.setAutomaticMode(true);
		liftSensor.setEnabled(true);
		chassis.setSafetyEnabled(false);
        
		chassis.mecanumDrive_Polar(0.5, 0.0, 0.0); //forward half-speed
		Timer.delay(2.0);
		chassis.mecanumDrive_Polar(0.0, 0.0, 0.0); //stop motors
    }

    /**
     * Tele-operation control period.
     */
    public void operatorControl() {
        chassis.setSafetyEnabled(true);
        liftSensor.setAutomaticMode(true);
        liftSensor.setEnabled(true);
        
        DriveThread driveThread = new DriveThread();
        LiftThread liftThread = new LiftThread();
        
        driveThread.start();
        liftThread.start();
        
        while(isEnabled() && isOperatorControl())
        {
        	//the threads will run as long as the above are true
        	//also, this ensures operatorControl() doesn't exit before threads stop
        	//if that happened, the garbage man would come and collect the threads hehehe
        }
    }

    /**
     * Runs during test mode
     */
    public void test() {
    	liftSensor.setAutomaticMode(true);
    	liftSensor.setEnabled(true);
    	double testHeight;
    	while(isTest() && isEnabled())
    	{
    		testHeight = liftSensor.getRangeInches();
    		System.out.println(testHeight + " inches");
    	}
    }
}