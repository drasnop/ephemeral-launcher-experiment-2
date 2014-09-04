package ca.ubc.cs.ephemerallauncherexperiment;

// things in this class should change during the experiment
public class State {
	
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
    public static int trial_timeout_ms = 20000;

    public static int num_positions(){
        return num_pages*ExperimentParameters.NUM_ICONS_PER_PAGE;
    }

    public static int num_highlighted_icons(){
        return num_pages*ExperimentParameters.NUM_HIGHLIGHTED_ICONS_PER_PAGE;
    }

    //////////////   utilities for logging   //////////////////////

	public static int page;		                // current page in the pager  //I doubt this is useful //AP: otherwise I don't know how to get this value from the AnimatedGridView
    public static int num_pages_visited;        // from 1 to infinity, since the beginning of the trial = number of swipes + 1
	
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


}
