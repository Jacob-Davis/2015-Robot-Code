package org.usfirst.frc.team1672.robot;

public class LiftThread extends Thread {
	public Robot robot;
	public void start()
	{
		
	}
	public void run()
	{
		double desiredHeight;
		while(robot.isEnabled())
		{
			desiredHeight = robot.getDesiredHeight();
			if(robot.getLiftHeight() < desiredHeight)
			{
				robot.liftUp();
			}
			if(robot.getLiftHeight() > desiredHeight)
			{
				robot.liftDown();
			}
			if(robot.getLiftHeight() == desiredHeight)
			{
				robot.liftStop();
			}
		}
	}
}
