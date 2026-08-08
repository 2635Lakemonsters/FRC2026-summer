// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.VoltageConfigs;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class VectorWheelSubsystem extends SubsystemBase {
  TalonFX m_vectorWheelMotor;
  private VoltageConfigs m_voltageConfig = new VoltageConfigs();

  /** Creates a new VectorWheelSubsystem. */
  public VectorWheelSubsystem() {
    m_vectorWheelMotor = new TalonFX(Constants.VECTOR_WHEEL_ID);
    m_vectorWheelMotor.getConfigurator().apply(m_voltageConfig);
  }

  public void vectorWheelIn() {
    m_vectorWheelMotor.setVoltage(Constants.VECTOR_WHEEL_IN_VOLTAGE);
  }

  public void vectorWheelOut() {
    m_vectorWheelMotor.setVoltage(Constants.VECTOR_WHEEL_OUT_VOLTAGE);
  }

  public void vectorWheelStop() {
    m_vectorWheelMotor.setVoltage(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
