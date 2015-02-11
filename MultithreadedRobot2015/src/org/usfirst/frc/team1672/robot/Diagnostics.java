package org.usfirst.frc.team1672.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;

public class Diagnostics implements Runnable {
	SmartDashboard display;
	Accelerometer accel;
	Master master;
	public Diagnostics(Accelerometer a)
	{
		accel = a;
		display = null;
		master = null;
	}
	public void setDashboard(SmartDashboard sd)
	{
		display = sd;
	}
	public void setMaster(Master m)
	{
		master = m;
	}
	public void run()
	{
		System.out.println("Diagnostics display run started");
		if(master != null)
		{
			while(master.operatorEnabled())
			{
				if(display != null)
				{
					
				}
			}
		}
		
	}
}
