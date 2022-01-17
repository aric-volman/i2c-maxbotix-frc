// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Constants;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.I2C;

public class MaxbotixUltrasonicSensor extends SubsystemBase {
  
  private final I2C.Port i2cPort;
  private I2C i2cController;

  private byte inputArray[] = { 0x00, 0x00 };
  private int distance;

  /** Creates a new MaxbotixUltrasonicSensor. */
  public MaxbotixUltrasonicSensor() {
    i2cPort = I2C.Port.kOnboard;

    i2cController = new I2C(i2cPort, Constants.I2CAddress);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    requestDistanceValue();
    // Mfr. recommends 80 to 100 ms delay between request and read
    Timer.delay(0.1);
    readDistanceValue();

    SmartDashboard.putNumber("Range", distance);

  }

  public void requestDistanceValue() {
    i2cController.write(0, 81);
  }

  public void readDistanceValue() {
    i2cController.read(0, 2, inputArray);
    /*
    Concatenate the highbyte (index 0) and lowbyte (index 1) 
    Concatenated bytes are in units of centimeters,
    multiplied by 10 to get meters

    Roughly based off of the official Arduino library
    */
    distance = (inputArray[1] + inputArray[0]*256)*10;
  }

}
