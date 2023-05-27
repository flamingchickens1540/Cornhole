package frc.robot.subsystems;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;

public class Launch extends CommandBase {
    Debouncer delayDebouncer = new Debouncer(Constants.INPUT_DELAY, DebounceType.kFalling);
    Debouncer activeDebouncer = new Debouncer(Constants.ACTIVATION_TIME, DebounceType.kRising);
    DigitalInput assistantSwitch;
    DigitalInput mainSwitch;
    DigitalOutput countdownDigitalOutput;

    AnalogPotentiometer powerAnalogInput;

    Catapult catapult;

    boolean wasTriggered = false;

    public Launch(Catapult catapult, DigitalInput assistantSwitch, DigitalInput mainSwitch, DigitalOutput countdownOutput, AnalogPotentiometer powAnalogInput) {
        this.assistantSwitch = assistantSwitch;
        this.mainSwitch = mainSwitch;
        this.countdownDigitalOutput = countdownOutput;
        this.powerAnalogInput = powAnalogInput;
        
        addRequirements(this.catapult = catapult);
        catapult.setDefaultCommand(this);
    }

    public boolean triggered() {
        boolean assistant = assistantSwitch.get();
        boolean main = !mainSwitch.get();

        if(assistant && main) {
            return wasTriggered = true;
        }
        
        return wasTriggered = wasTriggered && assistant;
    }

    public boolean shouldLaunch() {
        boolean trigger = triggered();

        boolean activate = !delayDebouncer.calculate(!trigger);

        countdownDigitalOutput.set(!trigger && !activate);

        boolean deactivate = !activeDebouncer.calculate(activate);
        
        return activate && deactivate;
    }

    @Override
    public void execute() {
        boolean shouldLaunch = shouldLaunch();

        if(shouldLaunch)
            catapult.setMotors(powerAnalogInput.get());
        else
            catapult.setMotors(0);
    }
}
