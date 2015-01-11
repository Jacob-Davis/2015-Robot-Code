/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author Jacob-Davis
 */
public class LiftOperator extends RobotTemplate {
	
/*	public double desiredHeight; Not needed since I remembered to extend RobotTemplate
	public double liftHeight;
	public double GROUND_SENSOR_DISTANCE;
	public double FIRST_SENSOR_DISTANCE;
	public double SECOND_SENSOR_DISTANCE;
	public double THIRD_SENSOR_DISTANCE;
	public double FOURTH_SENSOR_DISTANCE;
	public double FIFTH_SENSOR_DISTANCE;
	
	Ultrasonic liftSensor = new Ultrasonic(6, 6);
	
	Jaguar lift = new Jaguar(5); */
	
 public void toGround(){
						desiredHeight = GROUND_SENSOR_DISTANCE;
						liftHeight = liftSensor.getRangeInches();
						lift.set(-0.5);
  }
 public void toFirst() {
						desiredHeight = FIRST_SENSOR_DISTANCE;
						liftHeight = liftSensor.getRangeInches();
						if(liftHeight < desiredHeight) {
						lift.set(0.5);
					} if(liftHeight > desiredHeight) {
						lift.set(-0.5);
					}
 }
 public void toSecond() {
						desiredHeight = SECOND_SENSOR_DISTANCE;
						liftHeight = liftSensor.getRangeInches();
						if(liftHeight < desiredHeight) {
						lift.set(0.5);
					} if(liftHeight > desiredHeight) {
						lift.set(-0.5);
					}
 }
 public void toThird() {
	 desiredHeight = THIRD_SENSOR_DISTANCE;
						liftHeight = liftSensor.getRangeInches();
						if(liftHeight < desiredHeight) {
						lift.set(0.5);
					} if(liftHeight > desiredHeight) {
						lift.set(-0.5);
					}
 }
 public void toFourth() {
						desiredHeight = FOURTH_SENSOR_DISTANCE;
						liftHeight = liftSensor.getRangeInches();
						if(liftHeight < desiredHeight) {
						lift.set(0.5);
					} if(liftHeight > desiredHeight) {
						lift.set(-0.5);
					}
 }
 public void toFifth() {
						desiredHeight = FIFTH_SENSOR_DISTANCE;
						liftHeight = liftSensor.getRangeInches();
						if(liftHeight < desiredHeight) {
						lift.set(0.5);
					} if(liftHeight > desiredHeight) {
						lift.set(-0.5);
		}
	}
}