package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import frc.robot.subsystems.Catapult;
import frc.robot.subsystems.Launch;

public class RobotContainer {
    DigitalInput assistantSwitch = new DigitalInput(0);
    DigitalInput mainSwitch = new DigitalInput(1);
    DigitalOutput countdownOutput = new DigitalOutput(2);

    Catapult catapult;
    public Launch launch;

    public RobotContainer() {
        catapult = new Catapult();
        launch = new Launch(catapult, assistantSwitch, mainSwitch, countdownOutput);
    }
}
