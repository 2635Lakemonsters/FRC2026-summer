// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.util.Units;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  
  //Intake Constants
  public static final int INTAKE_MOTOR_ID = 22;
  public static final int INTAKE_IN_VOLTAGE = 10;
  public static final int INTAKE_OUT_VOLTAGE = -3;
  public static final int INTAKE_ANGLE_MOTOR_ID = 11;
  public static final int VOLTAGE_TIME_CONSTANT = 10;
  public static final double INTAKE_ANGLE_FF = -0.25;
  public static final double INTAKE_ANGLE_DOWN_VOLTAGE = 0.5;
  public static final double INTAKE_ANGLE_UP_VOLTAGE = -2.5;
  public static final double INTAKE_HALF_UP_VOLTAGE = -1.5;

  //Transport Constants
  public static final int ELEVATOR_MOTOR_ID = 0;
  public static final double ELEVATOR_DOWN_VOLTAGE = -3;
  public static final double ELEVATOR_UP_VOLTAGE = 3;
  
  //Swerve Module
  public static final double kPModuleTurningController = 0.7; // 0.5
  public static final double kPModuleDriveController = 0; // added random value for test
  public static final double kDriveEncoderDistancePerPulse =
      0.0001 / 0.002706682950506; // TODO: Need to TEST
  public static final double kMaxSpeedMetersPerSecond = 6.0; // TODO: Need to Test
  
  // joystick channels
  public static final int RIGHT_JOYSTICK_CHANNEL = 1;
  public static final int LEFT_JOYSTICK_CHANNEL = 0;

  // TODO: 'EncoderDistancePerPulse' should be calculated based on the gearing and wheel diameter
  public static final double kWheelDiameterMeters =
      Units.inchesToMeters(3.5); // TODO: check actual measurement

  public static final double maxModuleLinearSpeed = 1.75; // Irrelevant used for createPath
  public static final double maxModuleLinearAccelaration = 8;
  public static final double kMaxModuleAngularSpeedRadiansPerSecond = 3 * Math.PI;
  public static final double kMaxModuleAngularAccelerationRadiansPerSecondSquared = 12 * Math.PI;

  public static final double DRIVETRAIN_WHEELBASE_WIDTH =
      Units.inchesToMeters(19.5); // TODO； check for correct measurements
  public static final double DRIVETRAIN_WHEELBASE_LENGTH =
      Units.inchesToMeters(24.75); // TODO: check for correct measurements

  // Module locations in the WPILib coordinate system (+x forward, +y left).
  // Keep these values synchronized with deploy/pathplanner/settings.json.
  public static final double DRIVETRAIN_MODULE_X = 0.314;
  public static final double DRIVETRAIN_MODULE_Y = 0.247;

  // SWERVE MODULE STATES
  public static final int FRONT_LEFT_MODULE_STATE_INDEX = 1;
  public static final int FRONT_RIGHT_MODULE_STATE_INDEX = 0;
  public static final int BACK_LEFT_MODULE_STATE_INDEX = 3;
  public static final int BACK_RIGHT_MODULE_STATE_INDEX = 2;

   // ANGLE OFFSETS
  public static final double FRONT_LEFT_ANGLE_OFFSET =
      Math.toRadians(-119.8 + 180);
  public static final double FRONT_RIGHT_ANGLE_OFFSET =
      Math.toRadians(-50.3);
  public static final double BACK_LEFT_ANGLE_OFFSET =
      Math.toRadians(-354.2 + 180);
  public static final double BACK_RIGHT_ANGLE_OFFSET =
      Math.toRadians(-152.7);

  // FRONT LEFT
  public static final int DRIVETRAIN_FRONT_LEFT_ANGLE_MOTOR = 1; // 1
  public static final int DRIVETRAIN_FRONT_LEFT_ANGLE_ENCODER = 1; // 1
  public static final int DRIVETRAIN_FRONT_LEFT_DRIVE_MOTOR = 2; // 2

  // FRONT RIGHT
  public static final int DRIVETRAIN_FRONT_RIGHT_ANGLE_MOTOR = 9; // 7
  public static final int DRIVETRAIN_FRONT_RIGHT_ANGLE_ENCODER = 3; // 0
  public static final int DRIVETRAIN_FRONT_RIGHT_DRIVE_MOTOR = 10; // 8

  // BACK LEFT
  public static final int DRIVETRAIN_BACK_LEFT_ANGLE_MOTOR = 3; // 3
  public static final int DRIVETRAIN_BACK_LEFT_ANGLE_ENCODER = 0; // 3
  public static final int DRIVETRAIN_BACK_LEFT_CANCODER = 14; // CAN ID, must be unique on bus
  public static final int DRIVETRAIN_BACK_LEFT_DRIVE_MOTOR = 4; // 10

  // BACK RIGHT
  public static final int DRIVETRAIN_BACK_RIGHT_ANGLE_MOTOR = 8; // 5
  public static final int DRIVETRAIN_BACK_RIGHT_ANGLE_ENCODER = 2; // 2
  public static final int DRIVETRAIN_BACK_RIGHT_DRIVE_MOTOR = 7; // 6

  // HAT CONSTANTS
  public static final double HAT_POWER_MOVE = 0.1;
  public static final double HAT_POWER_ROTATE = 0.3;

  public static final int HAT_POV_MOVE_LEFT = 270;
  public static final int HAT_POV_MOVE_RIGHT = 90;
  public static final int HAT_POV_MOVE_FORWARD = 0;
  public static final int HAT_POV_MOVE_BACK = 180;
  public static final int HAT_POV_0 = 0; // Left hat up
  public static final int HAT_POV_180 = 180; // Left hat down
  public static final int HAT_POV_ROTATE_LEFT = 270;
  public static final int HAT_POV_ROTATE_RIGHT = 90;
  public static final int UPTAKE_MOTOR_ID = 0;
  public static final int UPTAKE_UP_VOLTAGE = -3;
  public static final int UPTAKE_DOWN_VOLTAGE = 3;

    // OBJECT TRACKER SUBSYSTEM
  public static final double CAMERA_TILT =
      26.5; // previous was 37, 30 is for testing post bunnybots
  public static final double[] CAMERA_OFFSET = {3, 13}; // offset = [x, y], In inches
  // VISION AUTO COMMAND
  // TODO: VisionAutoData assumes only a single camera
  public static final int CAM_X_OFFSET = 0;
  public static final int CAM_Y_OFFSET = 0;
  public static final int CAM_ANGLE_OFFSET = 0;

  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }
}
