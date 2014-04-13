package ca.ubc.cs.ephemerallauncherexperiment;

import java.io.File;

import android.content.Context;
import ca.ubc.cs.ephemerallauncher.LauncherParameters;

public class State {

	// things put here should change during the experiment
	
	public static String participantId;
	public static int participant;
	
	public static int block; 		//  0 to CONDITONS-1
	public static ExperimentParameters.ConditionEnum condition;	
	
	public static int trial;		// in that condition
	
	public static int page;		// current page in the pager  //I doubt this is useful //AP: otherwise I don't know how to get this value from the AnimatedGridView
	public static long startTime;
	
	public static int[] current_images_ID;		// from 1 to 60, 0 is irrelevant
	public static int[] current_images_gs_ID;	// from 1 to 60, 0 is irrelevant
	public static int[] current_labels_ID;		// from 1 to 60, 0 is irrelevant
	
	public static boolean timeout;
	public static boolean missed;
	public static boolean success;
	
	public static int targetIconPage;
	public static int targetIconRow;
	public static int targetIconColumn;
	
	public static String stateCsvLog(Context context){
		return Utils.appendWithComma(participantId, String.valueOf(block), condition.toString(), String.valueOf(trial), String.valueOf(page), String.valueOf(startTime), String.valueOf(targetIconPage), String.valueOf(targetIconRow), String.valueOf(targetIconColumn), context.getString(current_images_ID[trial]), context.getString(current_labels_ID[trial]));
	}
	
	public static File currentTrialsLogFile;		//the file contains per trial logs for a participant
	public static File currentEventsLogFile;		//the file contains per event (command) logs for a participant
	public static File currentExperimentLogFile;	//the file contains the general experiment logs
	public static File currentDistributionsLogFile; //the file contains all information about distributions
	
	
	public static void initExperiment(){
		block=0;
		participant=-1;
	}
	
	//KA: don't you think it's better to put this in condition rather than State, as we have initializeExperiment in Experiment, 
	//initializeTrial in trial and initDistributions in Distributions.
	//AP: No, because I intended this function to be initStateForCondition (only concerns the variables in State).
	//The initialization of the condition is currently done in Condition.onCreate, but we could make a separate function
	//So I've moved the change of ANIMATION in there.
	public static void initCondition() {
		assert(Distributions.conditions.length==(ExperimentParameters.NUM_CONDITIONS*(1+ExperimentParameters.NUM_CONDITIONS%2)));
		condition = ExperimentParameters.ConditionEnum.values()[Distributions.conditions[participant%Distributions.conditions.length][block]];
		trial=1;
		page=1;	// maybe useless	
		current_images_ID = new int[LauncherParameters.NUM_PAGES*LauncherParameters.NUM_ICONS_PER_PAGE+1];
		current_images_gs_ID = new int[LauncherParameters.NUM_PAGES*LauncherParameters.NUM_ICONS_PER_PAGE+1];
		current_labels_ID = new int[LauncherParameters.NUM_PAGES*LauncherParameters.NUM_ICONS_PER_PAGE+1];
	}

}
