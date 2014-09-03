package ca.ubc.cs.ephemerallauncherexperiment;

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

    //////////////   utilities for logging   //////////////////////

	public static int page;		                // current page in the pager  //I doubt this is useful //AP: otherwise I don't know how to get this value from the AnimatedGridView
    public static int num_pages_visited;        // from 1 to infinity, since the beginning of the trial = number of swipes + 1

    public static int[] current_images_ID;		// from 1 to 60, 0 is irrelevant
	/*public static int[] current_images_gs_ID;	// from 1 to 60, 0 is irrelevant*/
	public static int[] current_labels_ID;		// from 1 to 60, 0 is irrelevant
	
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

	public static void initStateForCondition() {

        condition= ExperimentParameters.LATINSQ[participant% ExperimentParameters.LATINSQ.length][block];
        effect = ExperimentParameters.EFFECTS.values()[ExperimentParameters.CONDITIONS[condition][0]];
        num_pages = ExperimentParameters.NUM_PAGES[ExperimentParameters.CONDITIONS[condition][1]];
        num_positions = num_pages * ExperimentParameters.NUM_ICONS_PER_PAGE;
        accuracy = ExperimentParameters.ACCURACY[ExperimentParameters.CONDITIONS[condition][2]];
        num_highlighted_icons = num_pages*ExperimentParameters.NUM_HIGHLIGHTED_ICONS_PER_PAGE;

        trial=1;
        page=1;
        num_pages_visited=1;

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
