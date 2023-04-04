package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Catapult extends SubsystemBase {
    TalonSRX flMotor = new TalonSRX(Constants.FRONT_LEFT_MOTOR);
    TalonSRX frMotor = new TalonSRX(Constants.FRONT_RIGHT_MOTOR);
    TalonSRX blMotor = new TalonSRX(Constants.BACK_LEFT_MOTOR);
    TalonSRX brMotor = new TalonSRX(Constants.BACK_RIGHT_MOTOR);

    public Catapult() {
        blMotor.setInverted(true);
        flMotor.setInverted(true);
    }

    public void setMotors(double output) {
        flMotor.set(ControlMode.PercentOutput, output);
        frMotor.set(ControlMode.PercentOutput, output);
        blMotor.set(ControlMode.PercentOutput, output);
        brMotor.set(ControlMode.PercentOutput, output);
    }
}
