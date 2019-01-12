package org.usfirst.frc.team5420.robot;


import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 *
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 *
 * WARNING: While it may look like a good choice to use for your code if you're
 * inexperienced, don't. Unless you know what you are doing, complex code will
 * be much more difficult under this system. Use IterativeRobot or Command-Based
 * instead if you're new.
 */
public class Robot extends TimedRobot {
	
	public static Talon FL = new Talon(4);
	public static Talon FR = new Talon(1);
	public static Talon BL = new Talon(3);
	public static Talon BR = new Talon(2);
	public static SpeedControllerGroup m_left = new SpeedControllerGroup(FL, BL);
	public static SpeedControllerGroup m_right = new SpeedControllerGroup(FR, BR);
	public static DifferentialDrive m_drive = new DifferentialDrive(m_left, m_right);
	
	Timer timer = new Timer();
	public static Solenoid solenoid0;
	public static Solenoid solenoid1;
	public static Encoder encoder1;
	public static Compressor compressor0;
	public static Joystick joystick0;
	public static Joystick joystick1;
	public static DigitalInput UpperLimit;
	public static DigitalInput LowerLimit;
	public static VictorSP LiftMotor;
	public static DigitalInput StopButton;
	
	public void robotInit() {
		encoder1 = new Encoder(6,7,false,Encoder.EncodingType.k4X);
		compressor0 = new Compressor(0);
		joystick0 = new Joystick(0);
		joystick1 = new Joystick(1);
		solenoid0 = new Solenoid(2);
		solenoid1 = new Solenoid(3);
		UpperLimit = new DigitalInput(0);
		LowerLimit = new DigitalInput(9);
		LiftMotor = new VictorSP(6); 
		StopButton = new DigitalInput(4);
		
		
		m_drive.setDeadband(0.04);
	}
	
	private void putToConsole(String value){
		System.out.println(value);
	}

	
	@Override
	public void teleopInit() {
		System.out.println("Running Tele-op!");
	}
	
	public void teleopPeriodic() {
		
		// This will disable all robot Actions when pushed.
		if( StopButton.get() ) {	
			m_drive.arcadeDrive(-(joystick0.getRawAxis(1))*0.6 , joystick0.getRawAxis(4)*0.8 );
			
			// Controls Lift 
			double yValue = joystick1.getRawAxis(1);
			if (yValue < 0 && UpperLimit.get() == false) { 
				putToConsole("Upper Limit has been triggered");
				LiftMotor.set(yValue); //sets value anyway?
			}
			
			else if (yValue > 0 && LowerLimit.get() == false) {
				putToConsole("Lower Limit has been triggered");
				LiftMotor.set(yValue);
			}
			else {
				putToConsole("Set to Zero has been triggered");
				LiftMotor.set(0);
			}
			
			//Claw Control (Normally Open)
			if(joystick1.getRawButton(1)) {
				putToConsole("Claw Close Triggered");
				solenoid0.set(true);
				solenoid1.set(false);
			}
			else {
				putToConsole("Claw Open Triggered");
				solenoid0.set(false);
				solenoid1.set(true);
			}
		}
		else {
			putToConsole("Stop Motors by Human Input");
			m_drive.stopMotor();
		}
		
	}
}
