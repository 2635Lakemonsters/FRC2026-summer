// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.IntakeAngleCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.IntakeOutCommand;
import frc.robot.commands.UptakeCommand;
import frc.robot.commands.UptakeReverseCommand;
import frc.robot.commands.VectorWheelCommand;
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
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;
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
      //new CommandXboxController(OperatorConstants.kDriverControllerPort);
  // Joysticks
  private final SendableChooser<Command> autoChooser = new SendableChooser<>();
  public static Joystick rightJoystick = new Joystick(Constants.RIGHT_JOYSTICK_CHANNEL);
  public static Joystick leftJoystick = new Joystick(Constants.LEFT_JOYSTICK_CHANNEL);

  // Subsystem
  private static ObjectTrackerSubsystem m_objectTrackerSubsystem =
      new ObjectTrackerSubsystem("shripFront");
  private static ShooterSubsystem m_shooterSubsystem = new ShooterSubsystem(m_objectTrackerSubsystem);
  private static ActuatorSubsystem m_actuatorSubsystem = new ActuatorSubsystem();
  private static TurretSubsystem m_turretSubsystem = new TurretSubsystem();
  private static DrivetrainSubsystem m_drivetrainSubsystem = new DrivetrainSubsystem();
  private static IntakeSubsystem m_intakeSubsystem = new IntakeSubsystem();
  private static RollerSubsystem m_rollerSubsystem = new RollerSubsystem();
  private static UptakeSubsystem m_uptakeSubsystem = new UptakeSubsystem();
  private static VectorWheelSubsystem m_vectorWheelSubsystem = new VectorWheelSubsystem();
  private static IntakeAngleSubsystem m_intakeAngleSubsystem = new IntakeAngleSubsystem();


  //Cmds
  private static VectorWheelCommand m_vectorWheelCommand = new VectorWheelCommand(m_vectorWheelSubsystem);
  private static IntakeCommand m_intakeCommand =
      new IntakeCommand(m_intakeSubsystem, m_intakeAngleSubsystem);
  private static IntakeOutCommand m_intakeOutCommand =
      new IntakeOutCommand(m_intakeSubsystem, m_intakeAngleSubsystem);
  private static UptakeCommand m_uptakeCommand = new UptakeCommand(m_uptakeSubsystem);
  private static UptakeReverseCommand m_uptakeReverseCommand =
      new UptakeReverseCommand(m_uptakeSubsystem);


  private static Autos m_autos = 
      new Autos(
          m_drivetrainSubsystem,
          m_objectTrackerSubsystem,
          m_rollerSubsystem,
          m_uptakeSubsystem,
          m_vectorWheelSubsystem,
          m_shooterSubsystem,
          m_turretSubsystem,
          m_intakeAngleSubsystem,
          m_actuatorSubsystem,
          m_intakeSubsystem);
  

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
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
    Trigger shoot = new JoystickButton(leftJoystick, 1);

    //Right Joystick
    Trigger intakeIn = new JoystickButton(rightJoystick, 1);
    Trigger intakeOut = new JoystickButton(rightJoystick, 4);
    
    



    intakeIn.whileTrue(m_intakeCommand);

    intakeOut.whileTrue(m_intakeOutCommand);

  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return m_autos.goStraight();
  }
}
