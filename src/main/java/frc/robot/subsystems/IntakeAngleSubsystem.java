// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.VoltageConfigs;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeAngleSubsystem extends SubsystemBase {
  TalonFX m_intakeAngleMotor;
  VoltageConfigs m_config;
  double m_targetPos;
  PIDController m_pidController = new PIDController(0.0, 0.00, 0);
  double ff = 0;
  double initialPos;
  /** Creates a new IntakeAngleSubsystem. */
  public IntakeAngleSubsystem() {
    TalonFX m_intakeAngleMotor = new TalonFX(Constants.INTAKE_ANGLE_MOTOR_ID);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
