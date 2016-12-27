package main;

import java.util.Arrays;

public class ProgramArgs {

    private static boolean testing;

    public enum UISetting {
        UNSET, COMMANDLINE, GUI
    }

    public enum SongConfigSourceSetting {
        FROM_FILE, RANDOM, FROM_COMMANDLINE, UNSET, SIMPLE, TEST, DISTRIBUTIONAL
    }

    public static SongConfigSourceSetting configurationSetting = SongConfigSourceSetting.UNSET;
    public static UISetting userInterfaceSetting = UISetting.UNSET;

    public static void loadProgramArgs(String[] args) {
        if (args.length == 0)
            loadDefaultProgramArgs();
        else
            throw new UnsupportedOperationException("Illegal arguments passed to program: " + Arrays.toString(args));
    }

    private static void loadDefaultProgramArgs() {
//		configurationSetting = SongConfigSourceSetting.TEST;
        configurationSetting = SongConfigSourceSetting.DISTRIBUTIONAL;
        userInterfaceSetting = UISetting.COMMANDLINE;
    }

    public static boolean isTesting() {
        return testing;
    }

    public static void setTesting(boolean testing_in) {
        testing = testing_in;
    }
}























//TODO: oooh, enums within a class?



























































