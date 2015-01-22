package org.usfirst.frc.team1672.robot;

public interface LiftInterface {
	public double getInputHeight();
	public void liftUp();
	public void liftDown();
	public void liftStop();
	public void liftManualControl();
}
