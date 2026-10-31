// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.IntakeOutCommand;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  // Replace with CommandPS4Controller or CommandJoystick if needed
  //private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);
  // Joysticks
  private final SendableChooser<Command> autoChooser;
  public static Joystick rightJoystick = new Joystick(Constants.RIGHT_JOYSTICK_CHANNEL);
  public static Joystick leftJoystick = new Joystick(Constants.LEFT_JOYSTICK_CHANNEL);

  // Subsystem
  private static DrivetrainSubsystem m_drivetrainSubsystem = new DrivetrainSubsystem();
  private static IntakeSubsystem m_intakeSubsystem = new IntakeSubsystem();
  private static ElevatorSubsystem m_elevatorSubsystem = new ElevatorSubsystem();
  private static ExtenderSubsystem m_extenderSubsystem = new ExtenderSubsystem();

  //Cmds
  private static IntakeCommand m_intakeCommand = new IntakeCommand(m_intakeSubsystem);
  private static IntakeOutCommand m_intakeOutCommand = new IntakeOutCommand(m_intakeSubsystem);


  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

    m_elevatorSubsystem.zeroElevatorEncoder();
    m_extenderSubsystem.zeroExtenderEncoder();

    m_drivetrainSubsystem.setDefaultCommand(
        m_drivetrainSubsystem.run(m_drivetrainSubsystem::driveWithJoysticks));

    if (AutoBuilder.isConfigured()) {
      autoChooser = AutoBuilder.buildAutoChooser();
    } else {
      autoChooser = new SendableChooser<>();
      autoChooser.setDefaultOption("PathPlanner unavailable", Commands.none());
    }
    SmartDashboard.putData("Auto Chooser", autoChooser);

    

    // Configure the trigger bindings
    configureBindings();



  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    //Left Joystick
    Trigger elevatorManual = new Trigger(() -> Math.abs(leftJoystick.getY()) > 0.08);
    Trigger elevatorLow = new JoystickButton(leftJoystick, 7);
    Trigger elevatorMid = new JoystickButton(leftJoystick, 8);
    Trigger elevatorHigh = new JoystickButton(leftJoystick, 8);
    //Right Joystick
    Trigger intakeIn = new JoystickButton(rightJoystick, 1);
    Trigger intakeOut = new JoystickButton(rightJoystick, 2);
    Trigger swerveResetButton = new JoystickButton(rightJoystick, 9);

    swerveResetButton.onTrue(
        new SequentialCommandGroup(
            new InstantCommand(() -> m_drivetrainSubsystem.resetAngle(0)).withTimeout(0.01),
            new InstantCommand(() -> m_drivetrainSubsystem.zeroOdometry()).withTimeout(.01)));

    elevatorManual.whileTrue(
        new RunCommand(
            () -> m_elevatorSubsystem.manualControl(-leftJoystick.getY()), m_elevatorSubsystem));

    elevatorManual.onFalse(
        new InstantCommand(
            m_elevatorSubsystem::holdPosition, m_elevatorSubsystem));

    elevatorLow.onTrue(new InstantCommand(
      () -> m_elevatorSubsystem.moveToHeight(Constants.ELEVATOR_LOW), m_elevatorSubsystem));
    
    elevatorMid.onTrue(new InstantCommand(
      () -> m_elevatorSubsystem.moveToHeight(Constants.ELEVATOR_MID), m_elevatorSubsystem));

    elevatorHigh.onTrue(new InstantCommand(
      () -> m_elevatorSubsystem.moveToHeight(Constants.ELEVATOR_HIGH), m_elevatorSubsystem));

    intakeIn.whileTrue(m_intakeCommand);

    intakeOut.whileTrue(m_intakeOutCommand);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }
}
