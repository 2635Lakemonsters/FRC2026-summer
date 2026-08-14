// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.VoltageConfigs;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ShooterSubsystem extends SubsystemBase {
  ObjectTrackerSubsystem m_ObjectTrackerSubsystem;

  TalonFX m_shooterMotorLeft;
  TalonFX m_shooterMotorRight;

  VoltageConfigs m_leftConfig;
  VoltageConfigs m_rightConfig;

  TalonFX motor = new TalonFX(Constants.SHOOTER_MOTOR_ID);
  VelocityVoltage velocityRequest = new VelocityVoltage(0);
  Slot0Configs slot0Configs = new Slot0Configs();

  Joystick joystick = new Joystick(0);
  double power = 7;
  double savePower;
  double magnitude;
  double desiredVelocity = 60;
  double joystickDeltaPower = 0;





  /** Creates a new ShooterSubsystem. */

  public ShooterSubsystem(ObjectTrackerSubsystem m_objectTrackerSubsystem) {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
