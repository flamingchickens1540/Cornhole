package frc.robot.subsystems;

import edu.wpi.first.math.filter.Debouncer;
import edu.wpi.first.math.filter.Debouncer.DebounceType;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;

public class Launch extends CommandBase {
    private enum LaunchState {
        IDLE,
        SELECT,
        COUNTDOWN,
        LAUNCH
    }
    Debouncer delayDebouncer = new Debouncer(Constants.INPUT_DELAY, DebounceType.kFalling);
    Debouncer activeDebouncer = new Debouncer(Constants.ACTIVATION_TIME, DebounceType.kRising);
    DigitalInput assistantSwitch;
    DigitalInput mainSwitch;
    DigitalOutput countdownDigitalOutput;
    LaunchState currentState;
    long countdownStart;
    long launchStart;
    long powerStart;
    double powerLevel;
    int bounceSign = 1;

    AnalogPotentiometer powerAnalogInput;

    Catapult catapult;

    boolean wasTriggered = false;

    public Launch(Catapult catapult, DigitalInput assistantSwitch, DigitalInput mainSwitch, DigitalOutput countdownOutput, AnalogPotentiometer powAnalogInput) {
        this.assistantSwitch = assistantSwitch;
        this.mainSwitch = mainSwitch;
        this.countdownDigitalOutput = countdownOutput;
        this.powerAnalogInput = powAnalogInput;
        currentState = LaunchState.IDLE;

        addRequirements(this.catapult = catapult);
        catapult.setDefaultCommand(this);
    }

    public boolean triggered() {
        boolean assistant = assistantSwitch.get();
        boolean main = !mainSwitch.get();

//        return assistant && main;
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
        boolean assistant = !assistantSwitch.get();
        boolean user = mainSwitch.get();
        SmartDashboard.putBoolean("cornhole/assistant", assistant);
        SmartDashboard.putBoolean("cornhole/user", user);
//        boolean shouldLaunch = shouldLaunch();
//
//        if(shouldLaunch)
//            catapult.setMotors(powerAnalogInput.get());
//        else
//            catapult.setMotors(0);
        switch (currentState) {
            case IDLE -> {
                catapult.setMotors(0);
                powerLevel = 0.1;
                if(assistant && !user) {
                    currentState = LaunchState.SELECT;
                    powerStart = System.currentTimeMillis();
                }
            }
            case SELECT -> {
                catapult.setMotors(0);
                if(!assistant) currentState = LaunchState.IDLE;
                if(System.currentTimeMillis() - powerStart >= 1000){
                    powerStart = System.currentTimeMillis();
                    powerLevel += 0.1 * bounceSign;
                    if(powerLevel > 1) {
                        powerLevel = 1;
                        bounceSign = -1;
                    }
                    if(powerLevel < 0.1){
                        powerLevel = 0.1;
                        bounceSign = 1;
                    }
                }
                else if(assistant && user) {
                    currentState = LaunchState.COUNTDOWN;
                    countdownStart = System.currentTimeMillis();
                }
            }
            case COUNTDOWN -> {
                catapult.setMotors(0);
                if (!assistant) currentState = LaunchState.IDLE;
                else if (System.currentTimeMillis() - countdownStart >= 5000){
                    currentState = LaunchState.LAUNCH;
                    launchStart = System.currentTimeMillis();
                    catapult.setMotors(powerAnalogInput.get() * powerLevel);
                }
            }
            case LAUNCH -> {
                if (System.currentTimeMillis() - launchStart >= Constants.ACTIVATION_TIME * 1000) {
                    currentState = LaunchState.IDLE;
                }
            }
        }


        SmartDashboard.putNumber("cornhole/powerLevel", powerLevel);
        SmartDashboard.putNumber("cornhole/analogPower", powerAnalogInput.get());
    }
}
