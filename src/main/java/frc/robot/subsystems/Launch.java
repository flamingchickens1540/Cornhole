package frc.robot.subsystems;

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

    DigitalInput assistantSwitch;
    DigitalInput mainSwitch;
    DigitalOutput countdownOutput;
    DigitalOutput powerSelectionOutput;
    LaunchState currentState;
    long stateStart;
    double powerLevel;

    AnalogPotentiometer powerAnalogInput;

    Catapult catapult;

    boolean wasTriggered = false;

    public Launch(Catapult catapult, DigitalInput assistantSwitch, DigitalInput mainSwitch, DigitalOutput countdownOutput, DigitalOutput powerSelectionOutput, AnalogPotentiometer powAnalogInput) {
        this.assistantSwitch = assistantSwitch;
        this.mainSwitch = mainSwitch;
        this.countdownOutput = countdownOutput;
        this.powerAnalogInput = powAnalogInput;
        this.powerSelectionOutput = powerSelectionOutput;

        powerSelectionOutput.set(false);
        countdownOutput.set(false);

        currentState = LaunchState.IDLE;
        stateStart = System.currentTimeMillis();

        addRequirements(this.catapult = catapult);
        catapult.setDefaultCommand(this);
    }

    @Override
    public void execute() {
        boolean assistant = !assistantSwitch.get();
        boolean user = mainSwitch.get(); // check

        switch (currentState) {
            case IDLE -> {
                catapult.setMotors(0);
                powerLevel = 0.0;
                if(assistant && !user) {
                    setState(LaunchState.SELECT);
                    powerSelectionOutput.set(true);
                }
            }
            case SELECT -> {
                if(!assistant) {
                    setState(LaunchState.IDLE);
                    break;
                }
                if(user) {
                    double stateTime = (stateStart - System.currentTimeMillis()) / Constants.POWERSELECTION_BOUNCE_TIME % 2;

                    if(stateTime > 1) { // Adds bounce 
                        powerLevel = stateTime-2;
                    } else {
                        powerLevel = -stateTime;
                    }

                    setState(LaunchState.COUNTDOWN);

                    countdownOutput.set(true);
                }
            }
            case COUNTDOWN -> {
                if (!assistant) {
                    setState(LaunchState.IDLE);
                    break;
                }
                if (System.currentTimeMillis() - stateStart > Constants.COUNTDOWN_TIME){
                    setState(LaunchState.LAUNCH);
                    powerSelectionOutput.set(false); // makes display easier to code to use outputs like so
                    catapult.setMotors(1); // Power should not change during launch :)
                }
            }
            case LAUNCH -> {
                if (System.currentTimeMillis() - stateStart >= Constants.ACTIVATION_TIME) {
                    setState(LaunchState.IDLE);
                    countdownOutput.set(false);
                }
            }
        }

        SmartDashboard.putBoolean("cornhole/assistant", assistant);
        SmartDashboard.putBoolean("cornhole/user", user);
        SmartDashboard.putNumber("cornhole/powerLevel", powerLevel);
        SmartDashboard.putNumber("cornhole/analogPower", powerAnalogInput.get());
    }

    void setState(LaunchState launchState) {
        this.currentState = launchState;
        this.stateStart = System.currentTimeMillis();
    }
}
