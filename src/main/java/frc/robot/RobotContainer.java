package frc.robot;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import frc.robot.subsystems.Catapult;
import frc.robot.subsystems.Launch;

public class RobotContainer {
    DigitalInput assistantSwitch = new DigitalInput(0);
    DigitalInput mainSwitch = new DigitalInput(1);
    DigitalOutput countdownOutput = new DigitalOutput(2);
    DigitalOutput powerDigitalOutput = new DigitalOutput(3);
    AnalogPotentiometer powerLevel = new AnalogPotentiometer(4, 1, 0);
    AnalogPotentiometer deAnalogInput = new AnalogPotentiometer(5, 1, 0);

    Catapult catapult;
    public Launch launch;

    public RobotContainer() {
        catapult = new Catapult();
        launch = new Launch(catapult, assistantSwitch, mainSwitch, countdownOutput, powerDigitalOutput, powerLevel, deAnalogInput);
    }
}
