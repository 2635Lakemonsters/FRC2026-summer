// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.configs.VoltageConfigs;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.units.measure.Voltage;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class UptakeSubsystem extends SubsystemBase {
  TalonFX m_uptakeMotor;
  private VoltageConfigs m_voltageConfig = new VoltageConfigs();

  /** Creates a new UptakeSubsystem. */
  public UptakeSubsystem() {
    TalonFX m_uptakeMotor = new TalonFX(Constants.UPTAKE_MOTOR_ID);
    m_uptakeMotor.getConfigurator().apply(m_voltageConfig);
  }

  public void uptakeUp() {
    m_uptakeMotor.setVoltage(Constants.UPTAKE_UP_VOLTAGE);
  }

  public void uptakeReverse() {
    m_uptakeMotor.setVoltage(Constants.UPTAKE_DOWN_VOLTAGE);
  }

  public void uptakeStop() {
    m_uptakeMotor.setVoltage(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
