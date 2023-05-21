package frc.robot;

import edu.wpi.first.math.MathSharedStore;
import edu.wpi.first.wpilibj.AnalogPotentiometer;

public class CustomDebouncer {
    AnalogPotentiometer debounceAnalogInput;

    double maxDebounceTime;

    double endTime;

    public CustomDebouncer(AnalogPotentiometer debounceAnalogInput, double maxDebounceTime) {
        this.debounceAnalogInput = debounceAnalogInput;
        this.maxDebounceTime = maxDebounceTime;
    }

    public void tick(boolean input) {
        if(!input)
            endTime = MathSharedStore.getTimestamp() + debounceAnalogInput.get() * maxDebounceTime;
    }

    public boolean calculate(boolean input) {
        tick(input);
        return MathSharedStore.getTimestamp() > endTime; // I don't know if this is what I measure analog with
    }
}
