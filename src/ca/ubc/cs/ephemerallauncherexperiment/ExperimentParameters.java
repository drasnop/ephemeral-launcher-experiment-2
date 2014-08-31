package ca.ubc.cs.ephemerallauncherexperiment;

import android.graphics.Color;


public class ExperimentParameters {
	
	public static final int NUM_CONDITIONS=12;
	public static final int NUM_TRIALS=5;		//20
	public static final int NUM_PARTICIPANTS=12;
	
	public static final int zipfSize = 20;
	public static final int[] NUM_PAGES = {3,5};
    public static final double[][] ACCURACY = {{.75,.90},{.80,.95}};
	public static final int NUM_HIGHLIGHTED_ICONS_PER_PAGE = 3;


	public static final double MIN_ACCURACY = .80;
	
	public static final int TRIAL_TIMEOUT_MS = 20000;	//10000	
	public static final int TIMEOUT_CHECK_INTERVAL = 500;
	
	public static final String LOG_FOLDER = "EXP1";
	
	public static final String EXPERIMENT_LOG_FILE_NAME = "ExperimentLog.csv";
	
	public static final String ICON_SET = "IconSet 1";
	public static final String LABEL_SET = "LabelSet 1";
	public static final String ICON_RESOURCE_ADDRESS_PREFIX = "res/drawable-hdpi/";

	
	public final static int SUCCESS_MESSAGE_DURATION_MS = 500;
	public final static int SUCCESS_MESSAGE_DELAY_MS = 500;
	public final static int FAILURE_MESSAGE_DURATION_MS = 500;
	public final static int FAILURE_MESSAGE_DELAY_MS = 1000;
	public final static int VIBRATION_DURATION_MS = 250;
	
	public final static int SUCCESS_MESSAGE_COLOR = Color.GREEN;
	public final static int FAILURE_MESSAGE_COLOR = Color.rgb(255, 50, 50);
	
	public final static float SUCCESS_MESSAGE_SIZE = 20f;
	public final static float FAILURE_MESSAGE_SIZE = 25f;
	
	public final static String SUCCESS_MESSAGE = "ca.ubc.cs.ephemerallauncherexperiment.SUCCESS_MESSAGE";
	
	public static enum EFFECTS {
		CONTROL, TWIST, PULSEOUT
	}
	
	public static String csvFile(){
        //TODO: update this to print all values of pages
		return Utils.appendWithComma(String.valueOf(NUM_CONDITIONS), String.valueOf(NUM_TRIALS), String.valueOf(ExperimentParameters.NUM_PAGES[0]), String.valueOf(ExperimentParameters.NUM_HIGHLIGHTED_ICONS_PER_PAGE), String.valueOf(TRIAL_TIMEOUT_MS));
	}
}
