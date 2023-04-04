package frc.robot.subsystems;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;

public class Launch extends CommandBase {
    Debouncer delayDebouncer = new Debouncer(Constants.INPUT_DELAY, DebounceType.kFalling);
    Debouncer activeDebouncer = new Debouncer(Constants.ACTIVATION_TIME, DebounceType.kRising);
    DigitalInput DI;

    Catapult catapult;

    public Launch(Catapult catapult, DigitalInput DI) {
        this.DI = DI;
        addRequirements(this.catapult = catapult);
        catapult.setDefaultCommand(this);
    }

    public boolean shouldLaunch() {
        boolean activated = !delayDebouncer.calculate(DI.get());
        boolean deactivate = !activeDebouncer.calculate(activated);
        // System.out.print(activated);
        // System.out.println(deactivate);
        return activated && deactivate;
    }

    @Override
    public void execute() {
        if(shouldLaunch())
            catapult.setMotors(Constants.PERCENT_OUTPUT);
        else
            catapult.setMotors(0);
    }
}
