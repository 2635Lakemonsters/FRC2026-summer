// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.VoltageConfigs;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IntakeAngleSubsystem extends SubsystemBase {
  TalonFX m_intakeAngleMotor;
  VoltageConfigs m_voltageConfig;
  double m_targetPose;
  PIDController m_pidController = new PIDController(0.0, 0.00, 0);
  double ff = 0;
  double initialPose;
  boolean pidMode = false;
  /** Creates a new IntakeAngleSubsystem. */
  public IntakeAngleSubsystem() {
    TalonFX m_intakeAngleMotor = new TalonFX(Constants.INTAKE_ANGLE_MOTOR_ID);
    m_intakeAngleMotor.setNeutralMode(NeutralModeValue.Brake);

    m_voltageConfig = new VoltageConfigs();
    m_voltageConfig.SupplyVoltageTimeConstant = Constants.VOLTAGE_TIME_CONSTANT;
    m_intakeAngleMotor.getConfigurator().apply(m_voltageConfig);
    initialPose = m_intakeAngleMotor.getPosition().getValueAsDouble();
    m_targetPose = getAngle();
  }

  public double getAngle() {
    return -1
        * (m_intakeAngleMotor.getPosition().getValueAsDouble() - initialPose)
        / (6.578128)
        * 360;
  }

  public void changePidMode(boolean newPid) {
    pidMode = newPid;
  }

  public void resetEncoder() {
    m_intakeAngleMotor.setPosition(0);
  }
  
  public void setInitialPos() {
    initialPose = m_intakeAngleMotor.getPosition().getValueAsDouble();
  }

  public void intakeAngleDown() {
    m_intakeAngleMotor.setVoltage(Constants.INTAKE_ANGLE_DOWN_VOLTAGE); 
  }

  public void intakeAngleUp() {
    m_intakeAngleMotor.setVoltage(Constants.INTAKE_ANGLE_UP_VOLTAGE);
  }
  
  public void intakeHalfUp() {
    m_intakeAngleMotor.setVoltage(Constants.INTAKE_HALF_UP_VOLTAGE);
  }

  public void setVolts(double volts) {
    m_intakeAngleMotor.setVoltage(volts);
  }

  public void intakeAngleFeedForward() {
    m_intakeAngleMotor.setVoltage(Constants.INTAKE_ANGLE_FF); 
  }

  public void intakeAngleStop() {
    m_intakeAngleMotor.setVoltage(0); // -
  }

  public void setTargetPos(double target) {
    m_targetPose = target;
  }

  @Override
  public void periodic() {

  if (pidMode) {
    ff = -2.3;
    double fb = m_pidController.calculate(getAngle(), m_targetPose);
    setVolts(ff * Math.abs(Math.cos(getAngle() * Math.PI / 180)) - fb);
  }
    // This method will be called once per scheduler run
  }
}
