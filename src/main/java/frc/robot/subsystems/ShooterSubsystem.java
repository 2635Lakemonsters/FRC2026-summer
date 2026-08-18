// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.VoltageConfigs;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ShooterSubsystem extends SubsystemBase {
  ObjectTrackerSubsystem m_ObjectTrackerSubsystem;

  TalonFX m_shooterMotorLeft;
  TalonFX m_shooterMotorRight;

  VoltageConfigs m_leftConfig;
  VoltageConfigs m_rightConfig;

  VelocityVoltage velocityRequest = new VelocityVoltage(0);
  Slot0Configs slot0Configs = new Slot0Configs();

  Joystick joystick = new Joystick(0);
  double power = 7;
  double savePower;
  double magnitude;
  double desiredVelocity = 60;
  double joystickDeltaPower = 0;


  public ShooterSubsystem(ObjectTrackerSubsystem m_objectTrackerSubsystem) {
    CurrentLimitsConfigs currentLimits =
        new CurrentLimitsConfigs()
            .withStatorCurrentLimit(60)
            .withStatorCurrentLimitEnable(true)
            .withSupplyCurrentLimit(60)
            .withSupplyCurrentLimitEnable(true);
    
    
    m_leftConfig = new VoltageConfigs();
    m_rightConfig = new VoltageConfigs();
    m_rightConfig.SupplyVoltageTimeConstant = 10;
    m_leftConfig.SupplyVoltageTimeConstant = 10;

    m_ObjectTrackerSubsystem = m_objectTrackerSubsystem;
    m_shooterMotorLeft = new TalonFX(Constants.SHOOTER_MOTOR_ID_LEFT);
    m_shooterMotorRight = new TalonFX(Constants.SHOOTER_MOTOR_ID_RIGHT);

    m_shooterMotorLeft.getConfigurator().apply(m_leftConfig);
    m_shooterMotorLeft.getConfigurator().apply(currentLimits);
    m_shooterMotorRight.getConfigurator().apply(m_rightConfig);
    m_shooterMotorRight.getConfigurator().apply(currentLimits);

    slot0Configs.kG = 0;

    setPIDSV(.4, 
    desiredVelocity, 
    desiredVelocity, 
    desiredVelocity, 
    desiredVelocity);
  }
    
  public void setPIDSV(double p, double i, double d, double s, double v) {
      slot0Configs.kP = p;
      slot0Configs.kI = i;
      slot0Configs.kD = d;
      slot0Configs.kS = s;
      slot0Configs.kV = v;

      m_shooterMotorLeft.getConfigurator().apply(slot0Configs);
      m_shooterMotorRight.getConfigurator().apply(slot0Configs);
  }

  public void deltaShooterVoltage(double deltaPower) {
    joystickDeltaPower += deltaPower;
  }

  public void setdeltaShooterVoltage(double voltage) { 
    joystickDeltaPower = voltage;
  }

  public void velocityController(double velocity) { // in revolutions/sec
    double tempVelocity = velocity; // SmartDashboard.getNumber("velocity shooter rps", 1);
    m_shooterMotorLeft.setControl(velocityRequest.withVelocity(tempVelocity));
    m_shooterMotorRight.setControl(velocityRequest.withVelocity(-tempVelocity));
  }

  public void shoot() {
     Pose2d target = m_ObjectTrackerSubsystem.getNearestAprilTagDistTurret();

    magnitude = Math.sqrt(target.getX() * target.getX() + target.getY() * target.getY());
    if (magnitude != 0) {

      desiredVelocity =
          0.139446 * magnitude * magnitude * magnitude
              + -2.62406 * magnitude * magnitude
              + 16.19286 * magnitude
              + 36.23951;
    }
    
    velocityController(desiredVelocity);
  }

  

  public void shooterStop() {
    m_shooterMotorLeft.setVoltage(0);
    m_shooterMotorRight.setVoltage(0);
  }
    
      @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
