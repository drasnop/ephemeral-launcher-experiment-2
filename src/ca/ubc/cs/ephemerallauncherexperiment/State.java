package ca.ubc.cs.ephemerallauncherexperiment;

public class State {

	// things put here should change during the experiment
	
	public static int block=0; 		//  0 to CONDITONS, 0 is practice
	public static ExperimentParameters.ConditionEnum condition;
	public static int trial=1;		// in that condition
	
	public static int page=1;	// current page in the pager  //I doubt this is useful
	public static long startTime;
	
	public static String participantId;
	
	public static String stateCsvLog(){
		String log = Utils.appendWithComma(participantId, String.valueOf(block), condition.toString(), String.valueOf(trial), String.valueOf(startTime));
		return log;
	}
	
	public static void logTrial(long duration, int row, int column){
		// TODO   I think this function should belong to trial. Each trial logs its state and its results. It only
		// gets the state information from this class via stateCsvLog() for convenience
	};
}
