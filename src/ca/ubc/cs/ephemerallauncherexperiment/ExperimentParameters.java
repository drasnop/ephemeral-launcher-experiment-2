package ca.ubc.cs.ephemerallauncherexperiment;


public class ExperimentParameters {
	
	public static final int NUM_CONDITIONS=2;
	public static final int NUM_TRIALS=5;//20
	public static final int zipfSize = 20;
	
	public static final int NUM_PAGES = 3;
	public static final int NUM_HIGHLIGHTED_ICONS = 9;
	
	public static final int TRIAL_TIMEOUT_MS = 10000;//10000	
	public static final int TIMEOUT_CHECK_INTERVAL = 2000;
	
	public static final String LOG_FOLDER = "EXP1";
	
	public static final String EXPERIMENT_LOG_FILE_NAME = "ExperimentLog.log";
	
	public static enum ConditionEnum{
		PRACTICE, CONTROL, TWIST, PULSEOUT, TRANSPARENCY
		
		
	}
}
