// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.AnalogInput;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class SwerveModule extends SubsystemBase {
  /** Creates a new SwerveModule. */
  private final TalonFX m_driveMotor;
  private final TalonFX m_turningMotor;

  private final AnalogInput m_turningEncoderInput;
  private double turningMotorOffsetRadians;
  private final PIDController m_drivePIDController = new PIDController(Constants.kPModuleDriveController, 0, 0);
  private final PIDController m_turningPIDController = new PIDController(Constants.kPModuleTurningController, 0, 0.0001);

  private double m_driveMotorGain;

  /**
   * Constructs a SwerveModule.
   *
   * @param driveMotorChannel ID for the drive motor.
   * @param turningMotorChannel ID for the turning motor.
   * @param analogEncoderPort Analog input port for the turning encoder.
   * @param turningMotorOffsetRadians Offset to add to the turning encoder reading to align with the
   *     module's zero position.
   * @param driveMotorGain Gain to apply to the drive motor output for tuning.
   */
  
  public SwerveModule() {}

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
