package ca.ubc.cs.ephemerallauncherexperiment;

import android.content.Context;

import java.io.File;

// things in this class should change during the experiment
public class State {
	
	public static String participantId;
	public static int participant;
	
	public static int block; 		//  0 to CONDITIONS-1
    public static int trial;		// in that condition

    public static int condition;    //  0 to CONDITION-1
	public static ExperimentParameters.EFFECTS effect;
    public static int num_pages;
    public static int num_positions;
    public static double accuracy;
    public static int num_highlighted_icons;

    //////////////   utilities  //////////////////////

	public static int page;		// current page in the pager  //I doubt this is useful //AP: otherwise I don't know how to get this value from the AnimatedGridView
	public static long startTime;
	
	public static int[] current_images_ID;		// from 1 to 60, 0 is irrelevant
	/*public static int[] current_images_gs_ID;	// from 1 to 60, 0 is irrelevant*/
	public static int[] current_labels_ID;		// from 1 to 60, 0 is irrelevant
	
	public static boolean timeout;
	public static boolean missed;
	public static boolean success;
	
	public static int targetIconPage;
	public static int targetIconRow;
	public static int targetIconColumn;
	
	public static String stateCsvLog(Context context){
		return Utils.appendWithComma(participantId, String.valueOf(block), effect.toString(), String.valueOf(trial), String.valueOf(page), String.valueOf(startTime), String.valueOf(targetIconPage), String.valueOf(targetIconRow), String.valueOf(targetIconColumn), Utils.extractIconName(context.getString(current_images_ID[Distributions.targets[trial]]), ExperimentParameters.ICON_RESOURCE_ADDRESS_PREFIX), context.getString(current_labels_ID[Distributions.targets[trial]]));
	}
	
	public static File currentTrialsLogFile;		//the file contains per trial logs for a participant
	public static File currentEventsLogFile;		//the file contains per event (command) logs for a participant
	public static File currentExperimentLogFile;	//the file contains the general experiment logs
	public static File currentDistributionsLogFile; //the file contains all information about distributions
	
	
	public static void initExperiment(){
		block=0;
		participant=-1;
	}

	public static void initStateForCondition() {

        condition= ExperimentParameters.LATINSQ[participant% ExperimentParameters.LATINSQ.length][block];
        effect = ExperimentParameters.EFFECTS.values()[ExperimentParameters.CONDITIONS[condition][0]];
        num_pages = ExperimentParameters.NUM_PAGES[ExperimentParameters.CONDITIONS[condition][1]];
        num_positions = num_pages * ExperimentParameters.NUM_ICONS_PER_PAGE;
        accuracy = ExperimentParameters.ACCURACY[ExperimentParameters.CONDITIONS[condition][2]];
        num_highlighted_icons = num_pages*ExperimentParameters.NUM_HIGHLIGHTED_ICONS_PER_PAGE;

        trial=1;
        page=1;	// maybe useless

		current_images_ID = new int[State.num_pages*ExperimentParameters.NUM_ICONS_PER_PAGE+1];
		/*current_images_gs_ID = new int[State.num_pages*ExperimentParameters.NUM_ICONS_PER_PAGE+1];*/
		current_labels_ID = new int[State.num_pages*ExperimentParameters.NUM_ICONS_PER_PAGE+1];

        for(int pos=1; pos<= num_positions; pos++){
            State.current_images_ID[pos]=Distributions.images_ID[State.block][pos-1];
			/*State.current_images_gs_ID[pos]=Distributions.images_gs_ID[State.block][pos-1];*/
        }

        for(int pos=1; pos <= num_positions; pos++){
            State.current_labels_ID[pos]=Distributions.labels_ID[State.block][pos-1];
        }
    }
}
