// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.RobotController;
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

  public SwerveModule(
      int driveMotorChannel,
      int turningMotorChannel,
      int analogEncoderPort,
      double turningMotorOffsetRadians,
      double driveMotorGain) {

    m_driveMotor = new TalonFX(driveMotorChannel);
    m_turningMotor = new TalonFX(turningMotorChannel);

    m_driveMotor.setNeutralMode(NeutralModeValue.Brake);
    m_turningMotor.setNeutralMode(NeutralModeValue.Brake);

    this.turningMotorOffsetRadians = turningMotorOffsetRadians;

    m_driveMotorGain = driveMotorGain;

    m_turningEncoderInput = new AnalogInput(analogEncoderPort);

    m_turningPIDController.enableContinuousInput(-Math.PI, Math.PI);
  }

  public SwerveModulePosition getPosition() {
    return new SwerveModulePosition(
        m_driveMotor.getPosition().getValueAsDouble() * Constants.kDriveEncoderDistancePerPulse,
        new Rotation2d(getTurningEncoderRadians()));
  }

  public double getTurningEncoderRadians() {
    double angle =
        (1.0 - (m_turningEncoderInput.getVoltage() / RobotController.getVoltage5V()))
                * 2.0
                * Math.PI
            + turningMotorOffsetRadians;
    angle %= 2.0 * Math.PI;
    if (angle < 0.0) {
      angle += 2.0 * Math.PI;
    }

    return angle;
  }

  public double getTurningEncoderVoltage() {
    return m_turningEncoderInput.getVoltage();
  }

  /**
   * Returns the current state of the module.
   *
   * @return The current state of the module.
   */
  public SwerveModuleState getState() {
    return new SwerveModuleState(
        getVelocity(), new Rotation2d(getTurningEncoderRadians()));
  }

  /** Returns the drive-wheel velocity in meters per second. */
  public double getVelocity() {
    return m_driveMotor.getVelocity().getValueAsDouble()
        * Constants.kDriveEncoderDistancePerPulse;
  }

  public void stop() {
    m_driveMotor.set(0);
    m_turningMotor.set(0);
  }

  /**
   * Sets the desired state for the module.
   *
   * @param desiredState 
   */
  public void setDesiredState(SwerveModuleState desiredState) {
    SwerveModuleState state = desiredState;

    if (Math.abs(state.speedMetersPerSecond) < 0.001) {
      stop();
      return;
    }

    state.optimize(new Rotation2d(getTurningEncoderRadians()));

    
    final double driveOutput =
        m_drivePIDController.calculate(getVelocity(), state.speedMetersPerSecond);

    final double driveFeedForward = state.speedMetersPerSecond / Constants.kMaxSpeedMetersPerSecond;

    final var turnOutput =
        m_turningPIDController.calculate(getTurningEncoderRadians(), state.angle.getRadians());

    
    m_driveMotor.set(
        MathUtil.clamp(
            (driveOutput + driveFeedForward) * m_driveMotorGain,
            -1.0, 
            1.0) 
        );
    m_turningMotor.set(turnOutput);
  }
}
