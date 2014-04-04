package ca.ubc.cs.ephemerallauncherexperiment;

public class State {

	// things put here should change during the experiment
	
	public static int block=0; 		//  0 to CONDITONS, 0 is practice
	public static Condition condition;
	public static int trial=1;		// in that condition
	
	public static int page=1;	// current page in the pager
	public static long startTime;
}
