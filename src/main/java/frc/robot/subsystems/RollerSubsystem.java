// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.VoltageConfigs;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class RollerSubsystem extends SubsystemBase {
  TalonFX m_rollerMotor;

  private VoltageConfigs m_voltageConfig = new VoltageConfigs();
  /** Creates a new RollerSubsystem. */
  public RollerSubsystem() {
    m_rollerMotor = new TalonFX(Constants.ROLLER_MOTOR_ID);
    m_voltageConfig.SupplyVoltageTimeConstant = Constants.VOLTAGE_TIME_CONSTANT;
    m_rollerMotor.getConfigurator().apply(m_voltageConfig);
  }

  public void rollerForward() {
    m_rollerMotor.setVoltage(Constants.ROLLER_VOLTAGE);
  }

  public void rollerReverse() {
    m_rollerMotor.setVoltage(Constants.ROLLER_REVERSE_VOLTAGE);
  }


  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
