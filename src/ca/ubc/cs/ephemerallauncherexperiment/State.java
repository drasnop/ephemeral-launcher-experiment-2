package ca.ubc.cs.ephemerallauncherexperiment;

import java.io.File;
import java.io.Serializable;

// things in this class should change during the experiment
public class State implements Serializable{
	
	public String participantId;
	public int participant;
	
	public int block; 		//  0 to CONDITIONS-1
    public int trial;		// in that condition

    public int condition;    //  0 to CONDITION-1, index in the ExperimentParameters.CONDITIONS array
	public ExperimentParameters.EFFECTS effect;
    public int num_pages;
    public double accuracy;

    public int num_randomly_highlighted_icons;
    public int num_mru_highlighted_icons;
    public int trial_timeout_ms;

    public int num_positions(){
        return num_pages*ExperimentParameters.NUM_ICONS_PER_PAGE;
    }

    public int num_highlighted_icons(){
        return num_pages*ExperimentParameters.NUM_HIGHLIGHTED_ICONS_PER_PAGE;
    }

    public int index_num_pages(){
        return ExperimentParameters.CONDITIONS[condition][1];
    }

    //////////////   utilities for logging   //////////////////////

	public int page;		                // current page in the pager  //I doubt this is useful //AP: otherwise I don't know how to get this value from the AnimatedGridView
    public int num_pages_visited;        // from 1 to infinity, since the beginning of the trial = number of swipes + 1

    public String pages_times;                 // string of pairs (page#, time spent on it)
    public File currentTrialsLogFile;		// the file contains per trial logs for a participant
    public File currentEventsLogFile;		// the file contains per event (command) logs for a participant
    public File currentExperimentLogFile;	// the file contains the general experiment logs
    public File currentDistributionsLogFile; // the file contains all information about distributions

	public boolean timeout;
	public boolean missed;
	public boolean success;
	
	public int targetIconPage;
	public int targetIconRow;
	public int targetIconColumn;

    public void initExperiment(){
		block=0;
		participant=-1;
	}

    // Happens BEFORE ExperimentParameters.distributions.initForCondition();
	public void initStateForCondition() {

        condition= ExperimentParameters.ORDERS[participant% ExperimentParameters.ORDERS.length][block];
        accuracy = ExperimentParameters.ACCURACY[ExperimentParameters.CONDITIONS[condition][0]];
        num_pages = ExperimentParameters.NUM_PAGES[ExperimentParameters.CONDITIONS[condition][1]];
        effect = ExperimentParameters.EFFECTS.values()[ExperimentParameters.CONDITIONS[condition][2]];

        num_randomly_highlighted_icons = ExperimentParameters.NUM_RANDOMLY_HIGHLIGHTED_ICONS[ExperimentParameters.CONDITIONS[condition][1]];
        num_mru_highlighted_icons = ExperimentParameters.NUM_MRU_HIGHLIGHTED_ICONS[ExperimentParameters.CONDITIONS[condition][1]];

        trial=0;
    }

}
