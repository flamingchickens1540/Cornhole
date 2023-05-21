package frc.robot.subsystems;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.CustomDebouncer;

public class Launch extends CommandBase {
    CustomDebouncer delayDebouncer;
    Debouncer activeDebouncer = new Debouncer(Constants.ACTIVATION_TIME, DebounceType.kRising);
    DigitalInput assistantSwitch;
    DigitalInput mainSwitch;
    DigitalOutput countdownOutput;
    DigitalOutput powerDigitalOutput;

    AnalogPotentiometer powerAnalogInput;
    AnalogPotentiometer debounceAnalogInput;

    Catapult catapult;

    boolean wasTriggered = false;

    public Launch(Catapult catapult, DigitalInput assistantSwitch, DigitalInput mainSwitch, DigitalOutput countdownOutput, DigitalOutput powerDigitalOutput, AnalogPotentiometer powAnalogInput, AnalogPotentiometer debounceAnalogInput) {
        this.assistantSwitch = assistantSwitch;
        this.mainSwitch = mainSwitch;
        this.countdownOutput = countdownOutput;
        this.powerDigitalOutput = powerDigitalOutput;
        this.debounceAnalogInput = debounceAnalogInput;
        this.powerAnalogInput = powAnalogInput;
        this.delayDebouncer = new CustomDebouncer(debounceAnalogInput, Constants.INPUT_DELAY);
        
        addRequirements(this.catapult = catapult);
        catapult.setDefaultCommand(this);
    }

    public boolean triggered() {
        boolean assistant = !assistantSwitch.get();
        boolean main = !mainSwitch.get();

        powerDigitalOutput.set(main);

        if(assistant && main) {
            return wasTriggered = true;
        }
        
        return wasTriggered = wasTriggered && assistant;
    }

    public boolean shouldLaunch() {
        boolean trigger = !triggered();

        boolean activate = !delayDebouncer.calculate(trigger);

        countdownOutput.set(!trigger && !activate);

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
