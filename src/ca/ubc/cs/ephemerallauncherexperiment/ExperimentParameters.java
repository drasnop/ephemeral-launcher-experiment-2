package ca.ubc.cs.ephemerallauncherexperiment;

import android.graphics.Color;


public class ExperimentParameters {
	
	public static final int NUM_CONDITIONS=5;
	public static final int NUM_TRIALS=2;		//20
	public static final int NUM_PARTICIPANTS=20;
	
	public static final int zipfSize = 20;
	public static final int NUM_PAGES = 3;
	public static final int NUM_HIGHLIGHTED_ICONS = 9;
	
	public static final double MIN_ACCURACY = .90;
	
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
	
	public static enum ConditionEnum{
		CONTROL, TWIST, PULSEOUT, TRANSPARENCY, GREYBLUR
	}
	
	public static String csvFile(){
		return Utils.appendWithComma(String.valueOf(NUM_CONDITIONS), String.valueOf(NUM_TRIALS), String.valueOf(NUM_PAGES), String.valueOf(NUM_HIGHLIGHTED_ICONS), String.valueOf(TRIAL_TIMEOUT_MS));
	}
}
