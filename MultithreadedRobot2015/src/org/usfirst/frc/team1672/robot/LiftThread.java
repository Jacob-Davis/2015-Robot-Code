package org.usfirst.frc.team1672.robot;


/*
 * class LiftThread:
 * A subclass of Thread that encapsulates the robot's interfacing with the lift.
 */
public class LiftThread extends Thread {
	public Robot robot;
	public LiftThread()
	{
		super("LiftThread");
	}
	public void start()
	{
		
	}
	public void run()
	{
		double desiredHeight;
		double liftHeight;
		while(robot.operatorIsEnabled())
		{
			desiredHeight = robot.getInputHeight();
			liftHeight = robot.getLiftHeight();
			if(liftHeight < desiredHeight)
			{
				robot.liftUp();
			}
			if(liftHeight > desiredHeight)
			{
				robot.liftDown();
			}
			if(liftHeight == desiredHeight)
			{
				robot.liftStop();
			}
			robot.liftManualControl();
		}
	}
}
