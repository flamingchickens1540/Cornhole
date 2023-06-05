package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;

public class Launch extends CommandBase {
    private enum LaunchState {
        IDLE(true, true),
        SELECT(false, false),
        POWER_DISP(true, false),
        COUNTDOWN(true, false),
        LAUNCH(true, true);

        LaunchState(boolean count, boolean displayCountdown) {
            this.count = count;
            this.displayCountdown = displayCountdown;
        }

        boolean count;
        boolean displayCountdown;
    }

    DigitalInput assistantSwitch;
    DigitalInput userSwitch;
    DigitalOutput countOutput;
    DigitalOutput displayCountdownOutput;
    LaunchState currentState;
    long stateStart;
    double powerLevel;
    int bounceSign = 1;

    AnalogPotentiometer powerAnalogInput;

    Catapult catapult;

    boolean wasTriggered = false;

    public Launch(Catapult catapult, DigitalInput assistantSwitch, DigitalInput userSwitch, DigitalOutput displayCountdownOutput, DigitalOutput countOutput, AnalogPotentiometer powAnalogInput) {
        this.assistantSwitch = assistantSwitch;
        this.userSwitch = userSwitch;
        this.countOutput = countOutput;
        this.powerAnalogInput = powAnalogInput;
        this.displayCountdownOutput = displayCountdownOutput;

        setState(LaunchState.IDLE);

        addRequirements(this.catapult = catapult);
        catapult.setDefaultCommand(this);

        catapult.setMotors(0);
    }

    @Override
    public void execute() {
        boolean assistant = !assistantSwitch.get();
        boolean user = userSwitch.get();

        switch (currentState) {
            case IDLE -> {
                if(assistant && !user) {
                    setState(LaunchState.SELECT);
                }
            }
            case SELECT -> {         
                if(!assistant) {
                    setState(LaunchState.IDLE);
                }
                else if(user) {
                    double stateTime = (System.currentTimeMillis() - stateStart) / Constants.POWERSELECTION_BOUNCE_TIME % 2;

                    if(stateTime > 1) { // Adds bounce using a system similar to absolute valuing a signed int
                        powerLevel += 2 - stateTime;
                    }
                    
                    setState(LaunchState.POWER_DISP);
                }
            }
            case POWER_DISP -> {
                if (!assistant) {
                    setState(LaunchState.IDLE);
                }
                else if (System.currentTimeMillis() - stateStart > 500){
                    setState(LaunchState.COUNTDOWN);
                }
            }
            case COUNTDOWN -> {
                if (!assistant) {
                    setState(LaunchState.IDLE);
                    countOutput.set(false);
                }
                else if (System.currentTimeMillis() - stateStart > Constants.COUNTDOWN_TIME){
                    setState(LaunchState.LAUNCH);
                    catapult.setMotors(scalePower(Constants.MIN_POWER, 1)); // Power should not change during launch :)
                }
            }
            case LAUNCH -> {
                if (System.currentTimeMillis() - stateStart >= Constants.ACTIVATION_TIME) {
                    setState(LaunchState.IDLE);
                    catapult.setMotors(0);
                }
            }
        }

        SmartDashboard.putBoolean("cornhole/assistant", assistant);
        SmartDashboard.putBoolean("cornhole/user", user);
        SmartDashboard.putNumber("cornhole/powerLevel", powerLevel);
        SmartDashboard.putNumber("cornhole/analogPower", powerAnalogInput.get());
        SmartDashboard.putNumber("cornhole/testThing", 69420);
        SmartDashboard.putString("cornhole/state", currentState.toString());
    }

    void setState(LaunchState launchState) {
        this.currentState = launchState;
        this.stateStart = System.currentTimeMillis();

        this.countOutput.set(launchState.count);
        this.displayCountdownOutput.set(launchState.displayCountdown);
    }

    private double scalePower(double min, double max) {
        return (min+(max-min)*powerLevel)*powerAnalogInput.get();
    }
}
