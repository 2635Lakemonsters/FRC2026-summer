// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.



/* Figure out different way to get position, current has a little bit of error.*/


package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class ElevatorSubsystem extends SubsystemBase {

  private final TalonFX m_elevatorMotor;

  private final MotionMagicVoltage m_motionMagic = new MotionMagicVoltage(0);

  private final VoltageOut m_voltageRequest = new VoltageOut(0);

  private double m_targetHeight = 0;

  private double m_targetRotations = 0;

  public ElevatorSubsystem() {

    m_elevatorMotor = new TalonFX(Constants.ELEVATOR_MOTOR_ID);

    TalonFXConfiguration config = new TalonFXConfiguration();

    config.Feedback.SensorToMechanismRatio = Constants.ELEVATOR_GEAR_RATIO;  // Convert motor rotations into spool rotations.

    config.Slot0.kP = Constants.kPElevatorPID;
    config.Slot0.kI = Constants.kIElevatorPID;
    config.Slot0.kD = Constants.kDElevatorPID;

    // Motion Magic configuration //TEST//
    config.MotionMagic.MotionMagicCruiseVelocity = Constants.MAGIC_MOTION_CRUISE_VELOCITY;

    config.MotionMagic.MotionMagicAcceleration = Constants.MAGIC_MOTION_ACCELERATION;

    // Lower value equals smoother s-curve
    config.MotionMagic.MotionMagicJerk = Constants.MAGIC_MOTION_JERK;

      // Brake mode
    config.MotorOutput.NeutralMode = NeutralModeValue.Brake;

        // Apply configuration
    m_elevatorMotor.getConfigurator().apply(config);
  }

  public void zeroElevatorEncoder() {

    m_elevatorMotor.setPosition(0);

    m_targetRotations = 0;
  }

  public double getSpoolRotations() {

    return m_elevatorMotor.getPosition().getValueAsDouble();
  }
  
  public double getHeight() {

    return getSpoolRotations() * Constants.SPOOL_CIRCUMFERENCE;

  }
    

  public void moveToHeight(double inches) {

        // Limit requested position
    inches = Math.max(Constants.ELEVATOR_MIN_HEIGHT, Math.min(Constants.ELEVATOR_MAX_HEIGHT, inches));

    m_targetHeight = inches;

    // Convert inches into spool rotations
    double spoolRotations = (inches) / (Constants.SPOOL_CIRCUMFERENCE);

    // Send Motion Magic request
    m_elevatorMotor.setControl(m_motionMagic.withPosition(spoolRotations));

  }

  public void manualControl(double speed) {  

    double height = getHeight();

        // Prevent movement above maximum height
    if (height >= Constants.ELEVATOR_MAX_HEIGHT-1 && speed > 0) {speed = 0;}
        // Prevent movement below minimum height
    if (height <= Constants.ELEVATOR_MIN_HEIGHT-1 && speed < 0) {speed = 0;}

        // Limit joystick input
    speed = Math.max(-1.0, Math.min(1.0, speed));

        // Convert joystick input into voltage
    m_elevatorMotor.setControl(m_voltageRequest.withOutput(speed * Constants.ELEVATOR_MANUAL_VOLTAGE));

  }

  public void holdPosition() {

    double currentRotations = getSpoolRotations();

    m_targetRotations = currentRotations;

    m_targetHeight = getHeight();

    m_elevatorMotor.setControl(m_motionMagic.withPosition(currentRotations)
    );
}

  public void elevatorStop() {
    m_elevatorMotor.setVoltage(0);


  }

}
