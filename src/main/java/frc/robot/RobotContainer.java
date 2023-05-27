package frc.robot;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import frc.robot.subsystems.Catapult;
import frc.robot.subsystems.Launch;

public class RobotContainer {
    DigitalInput assistantSwitch = new DigitalInput(0);
    DigitalInput mainSwitch = new DigitalInput(1);
    DigitalOutput countdownDigitalOutput = new DigitalOutput(2);
    AnalogPotentiometer powerLevelAnalog = new AnalogPotentiometer(3, 1, 0);

    Catapult catapult;
    Launch launch;

    public RobotContainer() {
        catapult = new Catapult();
        launch = new Launch(catapult, assistantSwitch, mainSwitch, countdownDigitalOutput, powerLevelAnalog);
    }
}
