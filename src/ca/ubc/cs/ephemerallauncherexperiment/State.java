package ca.ubc.cs.ephemerallauncherexperiment;

import java.io.File;
import java.util.ArrayList;

import ca.ubc.cs.ephemerallauncher.LauncherParameters;

public class State {

	// things put here should change during the experiment
	
	public static int block=0; 		//  0 to CONDITONS, 0 is practice
	public static ExperimentParameters.ConditionEnum condition = ExperimentParameters.ConditionEnum.PRACTICE;
	public static ArrayList<ExperimentParameters.ConditionEnum> listOfConditions;
	public static int trial=1;		// in that condition
	
	public static int page=1;	// current page in the pager  //I doubt this is useful //AP: otherwise I don't know how to get this value from the AnimatedGridView
	public static long startTime;
	
	public static int[] current_images_ID = new int[LauncherParameters.NUM_PAGES*20+1];	// from 1 to 60, 0 is irrelevant
	// TODO add current_images_names, so we can log the name of the target image after each trial
	
	public static String participantId = "test_participant";
	
	public static boolean timeout = false;
	
	public static String stateCsvLog(){
		String log = Utils.appendWithComma(participantId, String.valueOf(block), condition.toString(), String.valueOf(trial), String.valueOf(startTime));
		return log;
	}
	
	public static File currentTrialsLogFile;
	
	public static void logTrial(long duration, int row, int column){
		// TODO   I think this function should belong to trial. Each trial logs its state and its results. It only
		// gets the state information from this class via stateCsvLog() for convenience
		
		// Antoine: No, it's not possible because Trial is already destroyed (we're in the Pager, and I felt it would be easier to have this function here, because ost of the parameters are here)
		// Kamyar:  That's not a problem because Trial can do the logging before being destroyed. See, logically every row of log is (State Information, Trial Result). If we do it in State, then we have to send
		// the trial results to State.java to do the final logging which does not make sense.
		//OOPS! By Trial I meant Pager all this time.
		//I implemented it in Pager but if you think it's better here we can easily move it
		//AP: by Pager you mean AnimatedGridView, right?
		//KA: right now it's in Pager
	};

}
