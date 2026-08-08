// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

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
  public static final int VECTOR_WHEEL_ID = 13;
  public static final int VECTOR_WHEEL_IN_VOLTAGE = -1;
  public static final int VECTOR_WHEEL_OUT_VOLTAGE = 1;
  public static final int UPTAKE_MOTOR_ID = 0;
  public static final int UPTAKE_UP_VOLTAGE = -3;
  public static final int UPTAKE_DOWN_VOLTAGE = 3;
  public static final int ROLLER_MOTOR_ID = 12;
  public static final int ROLLER_REVERSE_VOLTAGE = -2;
  public static final int ROLLER_VOLTAGE = 2;



  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }
}
