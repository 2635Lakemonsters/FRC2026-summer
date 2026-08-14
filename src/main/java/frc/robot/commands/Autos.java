// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import frc.robot.subsystems.ActuatorSubsystem;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.IntakeAngleSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ObjectTrackerSubsystem;
import frc.robot.subsystems.RollerSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.UptakeSubsystem;
import frc.robot.subsystems.VectorWheelSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

public class Autos extends Command {
  DrivetrainSubsystem m_driveTrainSubsystem;
  ObjectTrackerSubsystem m_objectTrackerSubsystem;
  RollerSubsystem m_rollerSubsystem;
  UptakeSubsystem m_uptakeSubsystem;
  VectorWheelSubsystem m_vectorWheelSubsystem;
  ShooterSubsystem m_shooterSubsystem;
  TurretSubsystem m_turretSubsystem;
  IntakeAngleSubsystem m_intakeAngleSubsystem;
  ActuatorSubsystem m_actuatorSubsystem;
  IntakeSubsystem m_intakeSubsystem;
  /** Example static factory for an autonomous command. */
  

  private Autos(
    DrivetrainSubsystem driveTrainSubsystem,
    ObjectTrackerSubsystem objectTrackerSubsystem,
    RollerSubsystem rollerSubsystem,
    UptakeSubsystem uptakeSubsystem,
    VectorWheelSubsystem vectorWheelSubsystem,
    ShooterSubsystem shooterSubsystem,
    TurretSubsystem turretSubsystem,
    IntakeAngleSubsystem intakeAngleSubsystem,
    ActuatorSubsystem actuatorSubsystem,
    IntakeSubsystem intakeSubsystem) 
    
    {
    m_driveTrainSubsystem = driveTrainSubsystem;
    m_objectTrackerSubsystem = objectTrackerSubsystem;
    m_rollerSubsystem = rollerSubsystem;
    m_uptakeSubsystem = uptakeSubsystem;
    m_vectorWheelSubsystem = vectorWheelSubsystem;
    m_shooterSubsystem = shooterSubsystem;
    m_turretSubsystem = turretSubsystem;
    m_intakeAngleSubsystem = intakeAngleSubsystem;
    m_actuatorSubsystem = actuatorSubsystem;
    m_intakeSubsystem = intakeSubsystem;
  }

 
  public Command goStraight() {
    // sequential does the commands one by one
    return new SequentialCommandGroup(
        new InstantCommand(() -> m_driveTrainSubsystem.stopMotors()).withTimeout(0.1), // stop the motors for 0.1s
        new InstantCommand(() -> m_driveTrainSubsystem.setFollowJoystick(false))
            .withTimeout(0.1), // make sure we aren't moving based on joystick input
        new PidAutoCommand(),
        new InstantCommand(() -> m_driveTrainSubsystem.setFollowJoystick(true))
            .withTimeout(0.1), // set back to using joystick input to prepare for teleop
        new InstantCommand(() -> m_driveTrainSubsystem.stopMotors()), // stop the motors
        new InstantCommand(() -> m_driveTrainSubsystem.resetAngle(180)), // reset the angle to 180 deg
        new InstantCommand(() -> m_driveTrainSubsystem.zeroOdometry())); // zero the odometry
  }



}
