package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.motorcontrol.PWMVictorSPX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


public class Robot extends TimedRobot {

  private WPI_TalonSRX shooter = new WPI_TalonSRX (0);
  private WPI_TalonSRX index = new WPI_TalonSRX (2);
  private PWMVictorSPX intake = new PWMVictorSPX(11);
  private PWMVictorSPX FL = new PWMVictorSPX(13);
  private PWMVictorSPX FR = new PWMVictorSPX(14);
  private PWMVictorSPX BL = new PWMVictorSPX(1);
  private PWMVictorSPX BR = new PWMVictorSPX(0);

  private Joystick joy1 = new Joystick(0);
  private Joystick xbox = new Joystick(1);

  private Joystick joy2 = new Joystick(2);
  

  @Override
  public void robotInit() {
    var visionThread = new Thread(() -> AprilTagsPipeline.aprilTagVisionProc());
    visionThread.setDaemon(true);
    visionThread.start();
    
    /*

    while (AprilTagsPipeline.aprilTagCenterX() != AprilTagsPipeline.frameCenterX()) {
      double aprilTagCenterX = AprilTagsPipeline.aprilTagCenterX();
      double frameCenterX = AprilTagsPipeline.frameCenterX();
      SmartDashboard.putNumber("Frame Center X", AprilTagsPipeline.frameCenterX());
      SmartDashboard.putNumber("AprilTag Center X", AprilTagsPipeline.aprilTagCenterX());

      Timer.delay(10);

      while (aprilTagCenterX - frameCenterX > 3) {
        FL.set(0.2);
        BL.set(0.2);
        FR.set(-0.2);
        BR.set(-0.2);
        aprilTagCenterX = AprilTagsPipeline.aprilTagCenterX();
        frameCenterX = AprilTagsPipeline.frameCenterX();
      }

      while (aprilTagCenterX - frameCenterX < -3) {
        FL.set(-0.2);
        BL.set(-0.2);
        FR.set(0.2);
        BR.set(0.2);
        aprilTagCenterX = AprilTagsPipeline.aprilTagCenterX();
        frameCenterX = AprilTagsPipeline.frameCenterX();
      }
    }

    */
  }

  @Override
  public void robotPeriodic() {}

  @Override
  public void autonomousInit() {}

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void teleopInit() {}

  @Override
  public void teleopPeriodic(){
    double idealWidth = 45.5;
    double frameCenterX = AprilTagsPipeline.frameCenterX();

    // Sets the shooter speed
    double shooterspeed = joy1.getRawAxis(3);
    // double shooterspeed = joy2.getRawAxis(3);
    shooter.set(shooterspeed);
    // Sets the indexer speed (plus shaped part that spins to agitate the balls and feed them to the shooter)
    double indexspeed = -joy1.getRawAxis(1);
    index.set(indexspeed);
    // Sets the intake speed (can be positive or negative)
    double intakespeed = joy1.getRawAxis(1);
    intake.set(intakespeed);

    // Driving controller axis values to set drive, strafe, and rotate
    double drive = -xbox.getRawAxis(1);
    double strafe = xbox.getRawAxis(0);
    double rotation = xbox.getRawAxis(4);

    // These are the lines that deal with the driving and mecanums lol
    FL.set(drive + strafe + rotation);
    FR.set(-drive + strafe + rotation);
    BL.set(drive - strafe + rotation);
    BR.set(-drive - strafe + rotation);

    if (Math.abs(AprilTagsPipeline.aprilTagCenterX() - frameCenterX) > 70) {
      SmartDashboard.putNumber("Frame Center X", AprilTagsPipeline.frameCenterX());
      SmartDashboard.putNumber("AprilTag Center X", AprilTagsPipeline.aprilTagCenterX());

      // Timer.delay(1);

      while (AprilTagsPipeline.aprilTagCenterX() - frameCenterX > 70) {
        double power = (AprilTagsPipeline.aprilTagCenterX() - frameCenterX) * -1 / 200.0;

        if (Math.abs(power) > 0.7) {
          power *= 0.8; 
        }

        FL.set(power);
        BL.set(power);
        FR.set(power);
        BR.set(power);

        SmartDashboard.putNumber("Frame Center X", AprilTagsPipeline.frameCenterX());
        SmartDashboard.putNumber("Power", power);
        SmartDashboard.putNumber("AprilTag Center X", AprilTagsPipeline.aprilTagCenterX());
        SmartDashboard.putNumber("Difference", AprilTagsPipeline.aprilTagCenterX() - frameCenterX);
        SmartDashboard.putString("Position", "right");
        SmartDashboard.putNumber("Width of Tag", AprilTagsPipeline.aprilTagWidth());

        shooter.set(joy1.getRawAxis(3));
        index.set(-joy1.getRawAxis(1));
      }

      while (AprilTagsPipeline.aprilTagCenterX() - frameCenterX < -70) {
        double power = (AprilTagsPipeline.aprilTagCenterX() - frameCenterX) * -1 / 200.0;

        if (Math.abs(power) > 0.7) {
          power *= 0.8; 
        }

        FL.set(power);
        BL.set(power);
        FR.set(power);
        BR.set(power);

        SmartDashboard.putNumber("Frame Center X", AprilTagsPipeline.frameCenterX());
        SmartDashboard.putNumber("Power", power);
        SmartDashboard.putNumber("AprilTag Center X", AprilTagsPipeline.aprilTagCenterX());
        SmartDashboard.putNumber("Difference", AprilTagsPipeline.aprilTagCenterX() - frameCenterX);
        SmartDashboard.putString("Position", "left");
        SmartDashboard.putNumber("Width of Tag", AprilTagsPipeline.aprilTagWidth());

        shooter.set(joy1.getRawAxis(3));
        index.set(-joy1.getRawAxis(1));
      }
    }

    FL.set(0);
    BL.set(0);
    FR.set(0);
    BR.set(0);

  }

  @Override
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

  @Override
  public void simulationInit() {}

  @Override
  public void simulationPeriodic() {}
}
