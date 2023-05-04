package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.subsystems.Catapult;
import frc.robot.subsystems.Launch;

public class RobotContainer {
    DigitalInput DI1 = new DigitalInput(0);
    DigitalInput DI2 = new DigitalInput(1);

    Catapult catapult;
    public Launch launch;

    public RobotContainer() {
        catapult = new Catapult();
        launch = new Launch(catapult, DI1, DI2);
    }
}
