package ca.ubc.cs.ephemerallauncherexperiment;

import java.io.*;

// things in this class should change during the experiment
public class State implements Externalizable{
	
	public static String participantId;
	public static int participant;
	
	public static int block; 		//  0 to CONDITIONS-1
    public static int trial;		// in that condition

    public static int condition;    //  0 to CONDITION-1, index in the ExperimentParameters.CONDITIONS array
	public static ExperimentParameters.EFFECTS effect;
    public static int num_pages;
    public static double accuracy;

    public static int num_randomly_highlighted_icons;
    public static int num_mru_highlighted_icons;
    public static int trial_timeout_ms;

    public static int num_positions(){
        return num_pages*ExperimentParameters.NUM_ICONS_PER_PAGE;
    }

    public static int num_highlighted_icons(){
        return num_pages*ExperimentParameters.NUM_HIGHLIGHTED_ICONS_PER_PAGE;
    }

    public static int index_num_pages(){
        return ExperimentParameters.CONDITIONS[condition][1];
    }

    //////////////   utilities for logging   //////////////////////

	public static int page;		                // current page in the pager  //I doubt this is useful //AP: otherwise I don't know how to get this value from the AnimatedGridView
    public static int num_pages_visited;        // from 1 to infinity, since the beginning of the trial = number of swipes + 1

    public static String pages_times;                 // string of pairs (page#, time spent on it)
    public static File currentTrialsLogFile;		// the file contains per trial logs for a participant
    public static File currentEventsLogFile;		// the file contains per event (command) logs for a participant
    public static File currentExperimentLogFile;	// the file contains the general experiment logs
    public static File currentDistributionsLogFile; // the file contains all information about distributions

	public static boolean timeout;
	public static boolean missed;
	public static boolean success;
	
	public static int targetIconPage;
	public static int targetIconRow;
	public static int targetIconColumn;

    public static void initExperiment(){
		block=0;
		participant=-1;
	}

    // Happens BEFORE Distributions.initForCondition();
	public static void initStateForCondition() {

        condition= ExperimentParameters.ORDERS[participant% ExperimentParameters.ORDERS.length][block];
        accuracy = ExperimentParameters.ACCURACY[ExperimentParameters.CONDITIONS[condition][0]];
        num_pages = ExperimentParameters.NUM_PAGES[ExperimentParameters.CONDITIONS[condition][1]];
        effect = ExperimentParameters.EFFECTS.values()[ExperimentParameters.CONDITIONS[condition][2]];

        num_randomly_highlighted_icons = ExperimentParameters.NUM_RANDOMLY_HIGHLIGHTED_ICONS[ExperimentParameters.CONDITIONS[condition][1]];
        num_mru_highlighted_icons = ExperimentParameters.NUM_MRU_HIGHLIGHTED_ICONS[ExperimentParameters.CONDITIONS[condition][1]];

        trial=0;
    }


    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        participantId = (String) in.readObject();
        participant = in.readInt();

        block = in.readInt();
        trial = in.readInt();

        condition = in.readInt();
        effect = (ExperimentParameters.EFFECTS) in.readObject();
        num_pages = in.readInt();
        accuracy = in.readDouble();

        num_randomly_highlighted_icons = in.readInt();
        num_mru_highlighted_icons = in.readInt();
        trial_timeout_ms = in.readInt();

        num_pages_visited = in.readInt();
        pages_times = (String) in.readObject();
        currentTrialsLogFile = (File) in.readObject();
        currentEventsLogFile = (File) in.readObject();
        currentExperimentLogFile = (File) in.readObject();
        currentDistributionsLogFile = (File) in.readObject();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(participantId);
        out.writeInt(participant);

        out.writeInt(block);
        out.writeInt(trial);

        out.writeInt(condition);
        out.writeObject(effect);
        out.writeInt(num_pages);
        out.writeDouble(accuracy);

        out.writeInt(num_randomly_highlighted_icons);
        out.writeInt(num_mru_highlighted_icons);
        out.writeInt(trial_timeout_ms);

        out.writeInt(num_pages_visited);
        out.writeObject(pages_times);
        out.writeObject(currentTrialsLogFile);
        out.writeObject(currentEventsLogFile);
        out.writeObject(currentExperimentLogFile);
        out.writeObject(currentDistributionsLogFile);
    }
}
