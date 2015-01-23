package org.usfirst.frc.team1672.robot;

public class DriveThread extends Thread {
	private Robot robot;
	public DriveThread(Robot inputRobot)
	{
		super("DriveThread");
		robot = inputRobot;
	}
	public void start() {}
	public void run()
	{
		while(robot.operatorIsEnabled())
		{
			robot.driveManualControl();
		}
	}
}
