package ca.ubc.cs.ephemerallauncherexperiment;

import android.graphics.Color;


public class ExperimentParameters {
	
	public static final int NUM_CONDITIONS=12;
	public static final int NUM_TRIALS=10;		//40
	public static final int NUM_PARTICIPANTS=12;

    public static final int NUM_ICONS_PER_PAGE=20;
	public static final int[] NUM_PAGES = {3,6};
    public static final int NUM_HIGHLIGHTED_ICONS_PER_PAGE = 3;
    public static final int MAX_NUM_POSITIONS = NUM_PAGES[1]*NUM_ICONS_PER_PAGE;

    public static final double[] ACCURACY = {.80,.95};

    public static final int zipfSize = 20;

	public static final int TRIAL_TIMEOUT_MS = 20000;	//10000	
	public static final int TIMEOUT_CHECK_INTERVAL = 500;
	
	public static final String LOG_FOLDER = "EXP1";
	
	public static final String EXPERIMENT_LOG_FILE_NAME = "ExperimentLog.csv";
	
	public static final String ICON_SET = "IconSet 1";
	public static final String LABEL_SET = "LabelSet 1";
	public static final String ICON_RESOURCE_ADDRESS_PREFIX = "res/drawable-hdpi/";

	
	public final static int SUCCESS_MESSAGE_DURATION_MS = 500;
	public final static int SUCCESS_MESSAGE_DELAY_MS = 500;
	public final static int FAILURE_MESSAGE_DURATION_MS = 500;
	public final static int FAILURE_MESSAGE_DELAY_MS = 1000;
	public final static int VIBRATION_DURATION_MS = 250;
	
	public final static int SUCCESS_MESSAGE_COLOR = Color.GREEN;
	public final static int FAILURE_MESSAGE_COLOR = Color.rgb(255, 50, 50);
	
	public final static float SUCCESS_MESSAGE_SIZE = 20f;
	public final static float FAILURE_MESSAGE_SIZE = 25f;
	
	public final static String SUCCESS_MESSAGE = "ca.ubc.cs.ephemerallauncherexperiment.SUCCESS_MESSAGE";
    // latin square
    // http://www.maths.qmul.ac.uk/~rab/DOEbook/doeweb6.pdf
    // http://statpages.org/latinsq.html and look for latinsq5.txt or .csv in Drive
    public static final int[][] LATINSQ = {
            {0, 1, 11, 2, 10, 3, 9, 4, 8, 5, 7, 6},
            {1, 2, 0, 3, 11, 4, 10, 5, 9, 6, 8, 7},
            {2, 3, 1, 4, 0, 5, 11, 6, 10, 7, 9, 8},
            {3, 4, 2, 5, 1, 6, 0, 7, 11, 8, 10, 9},
            {4, 5, 3, 6, 2, 7, 1, 8, 0, 9, 11, 10},
            {5, 6, 4, 7, 3, 8, 2, 9, 1, 10, 0, 11},
            {6, 7, 5, 8, 4, 9, 3, 10, 2, 11, 1, 0},
            {7, 8, 6, 9, 5, 10, 4, 11, 3, 0, 2, 1},
            {8, 9, 7, 10, 6, 11, 5, 0, 4, 1, 3, 2},
            {9, 10, 8, 11, 7, 0, 6, 1, 5, 2, 4, 3},
            {10, 11, 9, 0, 8, 1, 7, 2, 6, 3, 5, 4},
            {11, 0, 10, 1, 9, 2, 8, 3, 7, 4, 6, 5}
    };
    // effects 0-2,  num_pages 0-1, accuracy 0-1
    public static final int[][] CONDITIONS ={
            {0,0,0},
            {0,0,1},
            {0,1,0},
            {0,1,1},
            {1,0,0},
            {1,0,1},
            {1,1,0},
            {1,1,1},
            {2,0,0},
            {2,0,1},
            {2,1,0},
            {2,1,1}
    };

    public static enum EFFECTS {
		CONTROL, TWIST, PULSEOUT
	}
	
	public static String csvFile(){
        //TODO: update this to print all values of pages
		return Utils.appendWithComma(String.valueOf(NUM_CONDITIONS), String.valueOf(NUM_TRIALS), String.valueOf(ExperimentParameters.NUM_PAGES[0]), String.valueOf(ExperimentParameters.NUM_HIGHLIGHTED_ICONS_PER_PAGE), String.valueOf(TRIAL_TIMEOUT_MS));
	}
}
