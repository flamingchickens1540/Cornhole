package frc.robot;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import frc.robot.subsystems.Catapult;
import frc.robot.subsystems.Launch;

public class RobotContainer {
    DigitalInput assistantSwitch = new DigitalInput(Constants.ASSISTANT_SWITCH);
    DigitalInput userSwitch = new DigitalInput(Constants.USER_SWITCH);
    DigitalOutput countOutput = new DigitalOutput(Constants.COUNT_OUTPUT);
    DigitalOutput displayCountdownOutput = new DigitalOutput(Constants.DISPLAY_COUNTDOWN_OUTPUT);

    AnalogPotentiometer powerLevelAnalog = new AnalogPotentiometer(Constants.POWER_LEVEL_ANALOG, 1, 0);

    Catapult catapult;
    Launch launch;

    public RobotContainer() {
        catapult = new Catapult();
        launch = new Launch(catapult, assistantSwitch, userSwitch, countOutput, displayCountdownOutput, powerLevelAnalog);
    }
}
