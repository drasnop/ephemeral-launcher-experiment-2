package ca.ubc.cs.ephemerallauncherexperiment;


public class ExperimentParameters {
	
	public static final int NUM_CONDITIONS=2;
	public static final int NUM_TRIALS=5;		//20
	public static final int NUM_PARTICIPANTS=10;
	
	public static final int zipfSize = 20;
	public static final int NUM_PAGES = 3;
	public static final int NUM_HIGHLIGHTED_ICONS = 9;
	
	public static final int TRIAL_TIMEOUT_MS = 10000;	//10000	
	public static final int TIMEOUT_CHECK_INTERVAL = 2000;
	
	public static final String LOG_FOLDER = "EXP1";
	
	public static final String EXPERIMENT_LOG_FILE_NAME = "ExperimentLog.log";
	
	public static final String ICON_SET = "IconSet 1";
	public static final String LABEL_SET = "LabelSet 1";
	
	public static enum ConditionEnum{
		CONTROL, TWIST, PULSEOUT, TRANSPARENCY, GREYBLUR
	}
	
	public static String csvFile(){
		return Utils.appendWithComma(String.valueOf(NUM_CONDITIONS), String.valueOf(NUM_TRIALS), String.valueOf(NUM_PAGES), String.valueOf(NUM_HIGHLIGHTED_ICONS), String.valueOf(TRIAL_TIMEOUT_MS));
	}
}
