// RobotBuilder Version: 4.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.
package frc.robot.subsystems;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.classes.SPIKE293Utils;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;

import static frc.robot.Constants.LauncherConstants.*;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

/**
 *
 */
public class Launcher extends SubsystemBase {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANT
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private WPI_TalonFX m_launcherMotor;
    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    private double m_targetRpm;

    /**
    *
    */
    public Launcher() {
        m_launcherMotor = new WPI_TalonFX(LAUNCHER_CAN_ID);     // needs to change

        m_launcherMotor.clearStickyFaults();
        m_launcherMotor.configFactoryDefault();
        m_launcherMotor.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);
        m_launcherMotor.config_kF(PID_SLOT_ID, KF, PID_CONFIG_TIMEOUT_MS);
        m_launcherMotor.config_kP(PID_SLOT_ID, KP, PID_CONFIG_TIMEOUT_MS);
        m_launcherMotor.config_kI(PID_SLOT_ID, KI, PID_CONFIG_TIMEOUT_MS);
        m_launcherMotor.config_kD(PID_SLOT_ID, KD, PID_CONFIG_TIMEOUT_MS);
        m_launcherMotor.config_IntegralZone(PID_SLOT_ID, I_ZONE, PID_CONFIG_TIMEOUT_MS);
        m_launcherMotor.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 90, 1, 0.001));
        m_launcherMotor.setInverted(false);
        m_launcherMotor.enableVoltageCompensation(true);
        m_launcherMotor.configVoltageCompSaturation(VOLTAGE_SATURATION);
        
        m_targetRpm = 0.0d;
        
        SmartDashboard.putNumber("Launcher Target RPM", m_targetRpm);
        SmartDashboard.putNumber("Current RPM Shooter", m_launcherMotor.getSelectedSensorVelocity());

    }

    @Override
    public void periodic() {
        // Get target RPM from SmartDashboard
        m_targetRpm = SmartDashboard.getNumber("Launcher Target RPM", 0.0d);
        SmartDashboard.putBoolean("Is ready", isReady());

        // Set the launcher wheel to the target RPM
        setRpm(m_targetRpm);
        SmartDashboard.putNumber("Current RPM Shooter", SPIKE293Utils.convertControllerVelocityToRPM(m_launcherMotor.getSelectedSensorVelocity()));
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    // Sets launcher RMP to set RPM , used to turn on the launcher
    public void setRpm(double rpm) {
        double velocity;

        // Save new target RPM
        m_targetRpm = rpm;

        // Push new RPM target to Dashboad
        SmartDashboard.putNumber("Launcher Target RPM", m_targetRpm);

        // Convert RPM to velocity
        velocity = SPIKE293Utils.convertRPMToControllerVelocity(m_targetRpm);

        if (m_targetRpm > MAX_SHOOTER_RPM){
            m_targetRpm = MAX_SHOOTER_RPM;
            System.out.println("Shooter has been limited to max RPM: " + MAX_SHOOTER_RPM);
        }

        // Set new velocity
        m_launcherMotor.set(ControlMode.Velocity, velocity);
    }

    // Turns off the launcher wheel
    public void stop() {
        m_targetRpm = 0.0;
        SmartDashboard.putNumber("Launcher Target RPM", m_targetRpm);
        m_launcherMotor.disable();
    }

    // Gets the current RPMs
    public double getCurrentRpm() {
        return SPIKE293Utils.convertControllerVelocityToRPM(m_launcherMotor.getSelectedSensorVelocity(0));
    }

    // Determines if the launcher wheel is up to speed
    public boolean isReady() {
        boolean isReady = false;
        double currentRPM = getCurrentRpm();

        // Don't check if launcher is ready if the target RPM is 0!
        if ((0 != m_targetRpm) && ((Math.abs(currentRPM - m_targetRpm)) <= (TARGET_RPM_READY_THRESHOLD))) {
            isReady = true;
        }

        return isReady;
    }

}