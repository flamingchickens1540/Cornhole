package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import frc.robot.subsystems.Catapult;
import frc.robot.subsystems.Launch;

public class RobotContainer {
    DigitalInput DI = new DigitalInput(0);

    Catapult catapult;
    Launch launch;

    public RobotContainer() {
        catapult = new Catapult();

        launch = new Launch(catapult, DI);
    }
}
