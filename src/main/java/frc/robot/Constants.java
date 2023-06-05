package frc.robot;

public class Constants {
    // Hold long you have to press down the trigger to launch
    public final static double COUNTDOWN_TIME = 5000;

    // How long it triggers for. I suggest starting it small like at 0.01.
    public final static double ACTIVATION_TIME = 500;

    public final static double POWERSELECTION_BOUNCE_TIME = 10000;

    /**
     * Starting from when you press and hold the trigger, it will wait for INPUT_DELAY seconds
     * and throttle the motors at PERCENT_OUTPUT for ACTIVATION_TIME seconds.
     */

    public final static int FRONT_LEFT_MOTOR = 11;
    public final static int FRONT_RIGHT_MOTOR = 13;
    public final static int BACK_LEFT_MOTOR = 12;
    public final static int BACK_RIGHT_MOTOR = 14;

    public final static int ASSISTANT_SWITCH = 0;
    public final static int USER_SWITCH = 1;
    public final static int COUNT_OUTPUT = 2;
    public final static int DISPLAY_COUNTDOWN_OUTPUT = 3;

    public final static int POWER_LEVEL_ANALOG = 3;

    public static final double MIN_POWER = 0.75;
}
