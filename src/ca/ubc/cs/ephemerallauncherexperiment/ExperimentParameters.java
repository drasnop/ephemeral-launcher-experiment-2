package ca.ubc.cs.ephemerallauncherexperiment;

import android.graphics.Color;

public class ExperimentParameters {

	public static final int NUM_CONDITIONS=12;
    public static final int NUM_PARTICIPANTS=12;
    public static final int NUM_TRIALS=40;		//40
    public static final int NUM_PRACTICE_TRIALS=2;

    public static final double[] ACCURACY = {.80,.95};
    public static final int zipfSize = 15;      // because it works out well for 40 trial
    public static final int[] NUM_RANDOMLY_HIGHLIGHTED_ICONS = {2,4};   // depending on # pages

    public static final int[] NUM_MRU_HIGHLIGHTED_ICONS = {1,2};        // depending on # pages
    public static final int NUM_ICONS_PER_PAGE=20;
    public static final int[] NUM_PAGES = {3,6};
    public static final int NUM_HIGHLIGHTED_ICONS_PER_PAGE = 3;

    public static final int MAX_NUM_POSITIONS = NUM_PAGES[1]*NUM_ICONS_PER_PAGE;

    public static final int MAX_NUM_HIGHLIGHTED_ICONS = NUM_PAGES[1]*NUM_HIGHLIGHTED_ICONS_PER_PAGE;

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

    // Each participant is assigned an order of conditions
    // Block on accuracy first, then block on num_pages, then latin square for the 3 effects
    // http://www.maths.qmul.ac.uk/~rab/DOEbook/doeweb6.pdf
    // latin square http://statpages.org/latinsq.html
    public static final int[][] ORDERS = {
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11},
            {1, 2, 0, 4, 5, 3, 7, 8, 6, 10, 11, 9},
            {2, 0, 1, 5, 3, 4, 8, 6, 7, 11, 9, 10},
            {3, 4, 5, 0, 1, 2, 9, 10, 11, 6, 7, 8},
            {4, 5, 3, 1, 2, 0, 10, 11, 9, 7, 8, 6},
            {5, 3, 4, 2, 0, 1, 11, 9, 10, 8, 6, 7},
            {6, 7, 8, 9, 10, 11, 0, 1, 2, 3, 4, 5},
            {7, 8, 6, 10, 11, 9, 1, 2, 0, 4, 5, 3},
            {8, 6, 7, 11, 9, 10, 2, 0, 1, 5, 3, 4},
            {9, 10, 11, 6, 7, 8, 3, 4, 5, 0, 1, 2},
            {10, 11, 9, 7, 8, 6, 4, 5, 3, 1, 2, 0},
            {11, 9, 10, 8, 6, 7, 5, 3, 4, 2, 0, 1}
    };

    // accuracy 0-1, num_pages 0-1, effects 0-2
    public static final int[][] CONDITIONS ={
            {0,0,0},
            {0,0,1},
            {0,0,2},
            {0,1,0},
            {0,1,1},
            {0,1,2},
            {1,0,0},
            {1,0,1},
            {1,0,2},
            {1,1,0},
            {1,1,1},
            {1,1,2}
    };

    public static enum EFFECTS {
		CONTROL, TWIST, PULSEOUT
    }

    public static String experimentToString(){
        return Utils.appendWithComma(String.valueOf(NUM_CONDITIONS), String.valueOf(NUM_TRIALS),
                String.valueOf(NUM_PAGES[0]), String.valueOf(NUM_PAGES[1]), String.valueOf(ACCURACY[0]), String.valueOf(ACCURACY[1]),
                String.valueOf(NUM_HIGHLIGHTED_ICONS_PER_PAGE), String.valueOf(TRIAL_TIMEOUT_MS));
    }

}
