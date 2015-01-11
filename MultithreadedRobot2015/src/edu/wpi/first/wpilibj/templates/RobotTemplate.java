/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;

//import edu.wpi.first.wpilibj.buttons.JoystickButton; Not needed (using raw input)


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class RobotTemplate extends SimpleRobot {
    /**
     * This function is called once each time the robot enters autonomous mode.
     */
	
		 Joystick driveStick = new Joystick(0);
		 Joystick liftStick = new Joystick(1);
		 
		 /*trigger is ground level each stage is a different set height for the containers to stack. Still needs calculations
		 JoystickButton button2 = new JoystickButton(liftStick, 2);
		 JoystickButton button3 = new JoystickButton(liftStick, 3);
		 JoystickButton button4 = new JoystickButton(liftStick, 2);
		 JoystickButton button5 = new JoystickButton(liftStick, 5); Not needed (using raw input) */
	
		 RobotDrive chassis = new RobotDrive(1, 2, 3, 4);
		 
		 Talon frontLeft = new Talon(1);
		 Talon frontRight = new Talon(2);
		 Talon rearLeft = new Talon(3);
		 Talon rearRight = new Talon(4);
		 
		 Jaguar lift = new Jaguar(5);
		 Jaguar test = new Jaguar(11);
		 
		 public final double GROUND_SENSOR_DISTANCE = 0.1; //ground level
		 public final double FIRST_SENSOR_DISTANCE = 12.1; //1st level
		 public final double SECOND_SENSOR_DISTANCE = 24.2; //2nd level
		 public final double THIRD_SENSOR_DISTANCE = 36.3; //3rd level
		 public final double FOURTH_SENSOR_DISTANCE = 48.4; //4th level
		 public final double FIFTH_SENSOR_DISTANCE = 56; //5th level (container)
		 
		 Ultrasonic liftSensor = new Ultrasonic(6, 6);
		 public static double liftHeight; //in inches | totes = 12.1 in, containers = 29 in
		 public static double desiredHeight;
		 
		 boolean isLiftReady = false;
	
    public void autonomous() {
				liftSensor.setAutomaticMode(true);
				liftSensor.setEnabled(true);
				
        chassis.setSafetyEnabled(false);
        chassis.mecanumDrive_Polar(0.5, 0.0, 0.0);
        Timer.delay(2.0);
        chassis.mecanumDrive_Polar(0.0, 0.0, 0.0);
    }

    /**
     * This function is called once each time the robot enters operator control.
     */
    public void operatorControl() {
			liftSensor.setAutomaticMode(true);
			liftSensor.setEnabled(true);
			
			Thread driveThread = new Thread() {
				public void run() {
					chassis.mecanumDrive_Polar(driveStick.getMagnitude(), driveStick.getDirectionDegrees(), driveStick.getTwist());
				}
			};
			Thread liftThread = new Thread() {
				public void run() {
					Joystick liftStick = new Joystick(1);
					if(liftStick.getRawButton(1)) {
							liftHeight = liftSensor.getRangeInches();
							while(liftHeight != GROUND_SENSOR_DISTANCE){
								lift.set(-0.5); //Change this value to get the ground instance
								liftHeight = liftSensor.getRangeInches();
							}
							lift.stopMotor();
							}
						if(liftStick.getRawButton(2)) {
							liftHeight = liftSensor.getRangeInches();
							if(liftHeight > FIRST_SENSOR_DISTANCE) {
								while(liftHeight != FIRST_SENSOR_DISTANCE){
									lift.set(-0.5); //Change this value to get the ground instance
									liftHeight = liftSensor.getRangeInches();
								}
								lift.stopMotor();
							}
							if(liftHeight < FIRST_SENSOR_DISTANCE) {
								while(liftHeight != FIRST_SENSOR_DISTANCE){
									lift.set(0.5); //Change this value to get the ground instance
									liftHeight = liftSensor.getRangeInches();
								}
								lift.stopMotor();
							}
							if(liftHeight == FIRST_SENSOR_DISTANCE) {
								System.out.println("Already at this Height!");
							}
							lift.stopMotor();
						}
						if(liftStick.getRawButton(3)) {
							liftHeight = liftSensor.getRangeInches();
							if(liftHeight > SECOND_SENSOR_DISTANCE) {
								while(liftHeight != SECOND_SENSOR_DISTANCE){
									lift.set(-0.5); //Change this value to get the ground instance
									liftHeight = liftSensor.getRangeInches();
								}
								lift.stopMotor();
							}
							if(liftHeight < SECOND_SENSOR_DISTANCE) {
								while(liftHeight != SECOND_SENSOR_DISTANCE){
									lift.set(0.5); //Change this value to get the ground instance
									liftHeight = liftSensor.getRangeInches();
								}
								lift.stopMotor();
							}
							if(liftHeight == SECOND_SENSOR_DISTANCE) {
								System.out.println("Already at this Height!");
							}
							lift.stopMotor();
						}
						if(liftStick.getRawButton(4)) {
							liftHeight = liftSensor.getRangeInches();
							if(liftHeight > THIRD_SENSOR_DISTANCE) {
								while(liftHeight != THIRD_SENSOR_DISTANCE){
									lift.set(-0.5); //Change this value to get the ground instance
									liftHeight = liftSensor.getRangeInches();
								}
								lift.stopMotor();
							}
							if(liftHeight < THIRD_SENSOR_DISTANCE) {
								while(liftHeight != THIRD_SENSOR_DISTANCE){
									lift.set(0.5); //Change this value to get the ground instance
									liftHeight = liftSensor.getRangeInches();
								}
								lift.stopMotor();
							}
							if(liftHeight == THIRD_SENSOR_DISTANCE) {
								System.out.println("Already at this Height!");
							}
							lift.stopMotor();
						}
						if(liftStick.getRawButton(5)) {
							liftHeight = liftSensor.getRangeInches();
							if(liftHeight > FOURTH_SENSOR_DISTANCE) {
								while(liftHeight != FOURTH_SENSOR_DISTANCE){
									lift.set(-0.5); //Change this value to get the ground instance
									liftHeight = liftSensor.getRangeInches();
								}
								lift.stopMotor();
							}
							if(liftHeight < FOURTH_SENSOR_DISTANCE) {
								while(liftHeight != FOURTH_SENSOR_DISTANCE){
									lift.set(0.5); //Change this value to get the ground instance
									liftHeight = liftSensor.getRangeInches();
								}
								lift.stopMotor();
							}
							if(liftHeight == FOURTH_SENSOR_DISTANCE) {
								System.out.println("Already at this Height!");
							}
							lift.stopMotor();
						}
						if(liftStick.getRawButton(10)) { //Raw Button 10 is on the right lower side of the joystick
							liftHeight = liftSensor.getRangeInches();
								while(liftHeight != FIFTH_SENSOR_DISTANCE){
									lift.set(-0.5); //Change this value to get the ground instance
									liftHeight = liftSensor.getRangeInches();
								}
								lift.stopMotor();
						}
					}
				};
			
			
			chassis.setSafetyEnabled(true);
        while (isOperatorControl() && isEnabled()) {
							driveThread.start();
							liftThread.start();
					}
				}
						
						/*	if(liftStick.getTrigger() && !isLiftReady) {
						desiredHeight = GROUND_SENSOR_DISTANCE;
						liftHeight = liftSensor.getRangeInches();
						lift.set(-0.5);
						}
						if(liftStick.getRawButton(2) && !isLiftReady) {
						desiredHeight = FIRST_SENSOR_DISTANCE;
						liftHeight = liftSensor.getRangeInches();
						}
						if(liftHeight < desiredHeight) {
						lift.set(0.5);
						} if(liftHeight > desiredHeight) {
						lift.set(-0.5);
						}	if(liftStick.getRawButton(2) && !isLiftReady) {
						desiredHeight = FIRST_SENSOR_DISTANCE;
						liftHeight = liftSensor.getRangeInches();
						}
						if(liftHeight < desiredHeight) {
						lift.set(0.5);
						} if(liftHeight > desiredHeight) {
						lift.set(-0.5);
						}
						if(liftStick.getRawButton(3) && !isLiftReady) {
						desiredHeight = SECOND_SENSOR_DISTANCE;
						liftHeight = liftSensor.getRangeInches();
						}
						if(liftHeight < desiredHeight) {
						lift.set(0.5);
						} if(liftHeight > desiredHeight) {
						lift.set(-0.5);
						}
						if(liftStick.getRawButton(4) && !isLiftReady) {
						desiredHeight = THIRD_SENSOR_DISTANCE;
						liftHeight = liftSensor.getRangeInches();
						}
						if(liftHeight < desiredHeight) {
						lift.set(0.5);
						} if(liftHeight > desiredHeight) {
						lift.set(-0.5);
						}
						if(liftStick.getRawButton(5) && !isLiftReady) {
						desiredHeight = FOURTH_SENSOR_DISTANCE;
						liftHeight = liftSensor.getRangeInches();
						}
						if(liftHeight < desiredHeight) {
						lift.set(0.5);
						} if(liftHeight > desiredHeight) {
						lift.set(-0.5);
						}
						if(liftStick.getRawButton(10) && !isLiftReady) {
						desiredHeight = FIFTH_SENSOR_DISTANCE;
						liftHeight = liftSensor.getRangeInches();
						}
						if(liftHeight < desiredHeight) {
						lift.set(0.5);
						} if(liftHeight > desiredHeight) {
						lift.set(-0.5);
						}
						if(liftHeight == desiredHeight) {
						lift.stopMotor();
						isLiftReady = true;
						}
						isLiftReady = false;
						chassis.mecanumDrive_Polar(driveStick.getMagnitude(), driveStick.getDirectionDegrees(), driveStick.getTwist()); */
					
  /**
     * This function is called once each time the robot enters test mode.
    */
    public void test() {
				liftSensor.setAutomaticMode(true);
				liftSensor.setEnabled(true);
				double testHeight;
				while (isTest() && isEnabled()) {
					testHeight = liftSensor.getRangeInches();
					System.out.println(testHeight + " inches");
		}
	}
}