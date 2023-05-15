package frc.robot.subsystems;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;

public class Launch extends CommandBase {
    Debouncer delayDebouncer = new Debouncer(Constants.INPUT_DELAY, DebounceType.kFalling);
    Debouncer activeDebouncer = new Debouncer(Constants.ACTIVATION_TIME, DebounceType.kRising);
    DigitalInput assistantSwitch;
    DigitalInput mainSwitch;
    DigitalOutput countdownOutput;

    Catapult catapult;

    boolean wasTriggered = false;

    public Launch(Catapult catapult, DigitalInput assistantSwitch, DigitalInput mainSwitch, DigitalOutput countdownOutput) {
        this.assistantSwitch = assistantSwitch;
        this.mainSwitch = mainSwitch;
        this.countdownOutput = countdownOutput;
        
        addRequirements(this.catapult = catapult);
        catapult.setDefaultCommand(this);
    }

    public boolean triggered() {
        boolean assistant = assistantSwitch.get();
        boolean main = mainSwitch.get();

        if(assistant && main) {
            return wasTriggered = false;
        }

        if(wasTriggered || !assistant) {
            return wasTriggered = true; 
        }

        if(!assistant && !main) {
            return wasTriggered = true; 
        }

        return wasTriggered = false;
    }

    public boolean shouldLaunch() {
        boolean trigger = triggered();

        boolean activate = !delayDebouncer.calculate(trigger);

        countdownOutput.set(!trigger && !activate);

        boolean deactivate = !activeDebouncer.calculate(activate);
        // System.out.print(activated);
        // System.out.println(deactivate);
        return activate && deactivate;
    }

    @Override
    public void execute() {
        boolean shouldLaunch = shouldLaunch();

        if(shouldLaunch)
            catapult.setMotors(Constants.PERCENT_OUTPUT);
        else
            catapult.setMotors(0);
    }
}
