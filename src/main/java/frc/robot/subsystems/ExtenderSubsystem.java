// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ExtenderSubsystem extends SubsystemBase {
  private final TalonFX m_extenderMotor;

  private final MotionMagicVoltage m_motionMagic = new MotionMagicVoltage(0);

  private final VoltageOut m_voltageRequest = new VoltageOut(0);

  public ExtenderSubsystem() {
    m_extenderMotor = new TalonFX(Constants.EXTENDER_MOTOR_ID);

    TalonFXConfiguration config = new TalonFXConfiguration();

    config.Feedback.SensorToMechanismRatio = Constants.EXTENDER_GEAR_RATIO;  // Convert motor rotations into spool rotations.

    config.Slot0.kP = Constants.kPExtenderPID;
    config.Slot0.kI = Constants.kIExtenderPID;
    config.Slot0.kD = Constants.kDExtenderPID;

    // Motion Magic configuration //TEST//
    config.MotionMagic.MotionMagicCruiseVelocity = Constants.MAGIC_MOTION_CRUISE_VELOCITY;

    config.MotionMagic.MotionMagicAcceleration = Constants.MAGIC_MOTION_ACCELERATION;

    // Lower value equals smoother s-curve
    config.MotionMagic.MotionMagicJerk = Constants.MAGIC_MOTION_JERK;

      // Brake mode
    config.MotorOutput.NeutralMode = NeutralModeValue.Brake;

        // Apply configuration
    m_extenderMotor.getConfigurator().apply(config);

  }

  public void zeroExtenderEncoder() {

    m_ExtenderMotor.setPosition(0);

    m_targetDegrees = 0;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
