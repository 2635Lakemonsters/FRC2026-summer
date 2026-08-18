// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ActuatorSubsystem extends SubsystemBase {
  Servo linearActuatorRight;
  Servo linearActuatorLeft;

  double desiredPosition;
  ObjectTrackerSubsystem m_objectTrackerSubsystem;
  boolean autoControl = true;


  /** Creates a new ActuatorSubsystem. */
  public ActuatorSubsystem(ObjectTrackerSubsystem m_ObjectTrackerSubsystem) {
    linearActuatorLeft = new Servo(Constants.LEFT_ACTUATOR_ID);
    linearActuatorRight = new Servo(Constants.RIGHT_ACTUATOR_ID)
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
