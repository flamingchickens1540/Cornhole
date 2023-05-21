package frc.robot;

public class Constants {
    // Hold long you have to press down the trigger to launch
    public final static double INPUT_DELAY = 11;

    // How long it triggers for. I suggest starting it small like at 0.01.
    public final static double ACTIVATION_TIME = 0.5;

    /**
     * Starting from when you press and hold the trigger, it will wait for INPUT_DELAY seconds
     * and throttle the motors at PERCENT_OUTPUT for ACTIVATION_TIME seconds.
     */

    public final static int FRONT_LEFT_MOTOR = 11;
    public final static int FRONT_RIGHT_MOTOR = 13;
    public final static int BACK_LEFT_MOTOR = 12;
    public final static int BACK_RIGHT_MOTOR = 14;

    // How much force is put into the launch.
    public final static double PERCENT_OUTPUT = 0.05;

    public final String FUN_FACT = "App software is better than robot software. App sw actually makes code. No one on robot software knows procedural programming";
}
