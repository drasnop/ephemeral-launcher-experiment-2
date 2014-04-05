package ca.ubc.cs.ephemerallauncherexperiment;


public class ExperimentParameters {

	// things in here should not change
	
	public static final int NUM_CONDITIONS=4;
	public static final int NUM_TRIALS=5;
	
	public static final int TRIAL_TIMEOUT_MS = 20000;
	
	public static enum ConditionEnum{
		PRACTICE, TWIST, PULSEOUT, TRANSPARENCY
	}
}
