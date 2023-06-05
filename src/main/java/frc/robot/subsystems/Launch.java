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
        POWER_DISP,
        COUNTDOWN,
        LAUNCH;
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

        countOutput.set(false); 
        displayCountdownOutput.set(false);

        currentState = LaunchState.IDLE;
        stateStart = System.currentTimeMillis();

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
                countOutput.set(false); 
                displayCountdownOutput.set(false);
                powerLevel = 0.0;
                if(assistant && !user) {
                    setState(LaunchState.SELECT);
                    countOutput.set(true);
                }
            }
            case SELECT -> {     
                double stateTime = (System.currentTimeMillis() - stateStart) / Constants.POWERSELECTION_BOUNCE_TIME % 2;

                if(stateTime > 1) { // Adds bounce using a system similar to absolute valuing a signed int
                    powerLevel = 2 - stateTime;
                } else {
                    powerLevel = stateTime;
                }
                
                if(!assistant) {
                    setState(LaunchState.IDLE);
                }
                else if(user) {
                    setState(LaunchState.POWER_DISP);
                    displayCountdownOutput.set(true);
                }
            }
            case POWER_DISP -> {
                if (!assistant) {
                    setState(LaunchState.IDLE);
                }
                else if (System.currentTimeMillis() - stateStart > 500){
                    setState(LaunchState.COUNTDOWN);
                    countOutput.set(false);
                }
            }
            case COUNTDOWN -> {
                if (!assistant) {
                    setState(LaunchState.IDLE);
                }
                else if (System.currentTimeMillis() - stateStart > Constants.COUNTDOWN_TIME){
                    setState(LaunchState.LAUNCH);
                    catapult.setMotors(scalePower(Constants.MIN_POWER, 1)); // Power should not change during launch :)
                    displayCountdownOutput.set(false);
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
    }

    private double scalePower(double min, double max) {
        return min+(max-min)*powerLevel*powerAnalogInput.get();
    }
}
