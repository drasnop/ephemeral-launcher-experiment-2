package ca.ubc.cs.ephemerallauncherexperiment;

import android.util.Log;
import android.util.Pair;
import ca.ubc.cs.ephemerallauncher.LauncherParameters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Distributions {

    public static final double zipfCoeff = 1;
    public static final int zipfSize = ExperimentParameters.zipfSize;

    protected static int[] zipfian = new int[zipfSize+1];
    public static int[] targets = new int[ExperimentParameters.NUM_TRIALS+1];
    public static int[] target_ranks = new int[ExperimentParameters.NUM_TRIALS+1];	// just used for logging
    public static int[] selected = new int[ExperimentParameters.NUM_TRIALS+1];		// just used for logging

    // We take the maximum number of icons needed for any condition; therefore for half the conditions the array will not be full
    public static int[][] highlighted = new int[ExperimentParameters.NUM_TRIALS+1][ExperimentParameters.NUM_PAGES[1]*ExperimentParameters.NUM_HIGHLIGHTED_ICONS_PER_PAGE];
    public static Integer[][] images_ID = new Integer[ExperimentParameters.NUM_CONDITIONS][ExperimentParameters.MAX_NUM_POSITIONS];
    /*public static Integer[][] images_gs_ID = new Integer[ExperimentParameters.NUM_CONDITIONS][ExperimentParameters.MAX_NUM_POSITIONS];*/
    public static Integer[][] labels_ID = new Integer[ExperimentParameters.NUM_CONDITIONS][ExperimentParameters.MAX_NUM_POSITIONS];

    public static double empiricalAccuracy;

    private static void iconDistributionInit() {
        ArrayList<Integer> allExperimentPositions = new ArrayList<Integer>();
        // TODO: this is too much: only half the trials have that many positions...
        for (int i = 0; i < ExperimentParameters.MAX_NUM_POSITIONS*ExperimentParameters.NUM_CONDITIONS; i++) {
            allExperimentPositions.add(i);
        }
        Collections.shuffle(allExperimentPositions);

        for (int i = 0; i < ExperimentParameters.NUM_CONDITIONS; i++){
            for (int j=0; j < ExperimentParameters.MAX_NUM_POSITIONS; j++){
                // TODO: remove modulo and select differently the icons
                images_ID[i][j] = LauncherParameters.images_ID[allExperimentPositions.get(i* ExperimentParameters.MAX_NUM_POSITIONS +j)%LauncherParameters.images_ID.length];
				/*images_gs_ID[i][j] = LauncherParameters.images_gs_ID[allExperimentPositions.get(i*num_positions+j)];*/
                labels_ID[i][j] = LauncherParameters.labels_ID[allExperimentPositions.get(i* ExperimentParameters.MAX_NUM_POSITIONS +j)%LauncherParameters.labels_ID.length];
            }
        }
    }


    public static void initForExperiment() {

        // Step 0:  Randomly select the icons and labels for the entire experiment

        iconDistributionInit();

        // Step 1: Generate Zipfian distribution of frequencies (the first cell of the array is not used)

        double denominator = 0;
        int sum_frequencies = 0;
        int frequency;
        for (int i = 1; i <= zipfSize; i++) {
            denominator += 1.0 / Math.pow(i, zipfCoeff);
        }
        for (int i = 1; i <= zipfSize; i++) {
            frequency=(int) Math.round(1.0/Math.pow(i, zipfCoeff)/denominator*ExperimentParameters.NUM_TRIALS);

            if(sum_frequencies >= ExperimentParameters.NUM_TRIALS){
                // Don't include item if we have already reached the limit
                frequency=0;
            }
            else if(frequency==0){
                // We don't have enough frequencies, so need to add more
                frequency=1;
                // Of course, this doesn't work if all the frequencies are already > 0
                // In that case, we're out of luck. The assert below will just fail.
            }
            zipfian[i]=frequency;
            sum_frequencies+=frequency;
        }
        assert(sum_frequencies==ExperimentParameters.NUM_TRIALS);
    }

    public static void initForCondition() {
        // Step 2: Pick zipfSize positions for the target icon, in [1..State.num_pages*20]

        ArrayList<Integer> allPositions = new ArrayList<Integer>();
        for (int i = 1; i <= State.num_positions; i++) {
            allPositions.add(i);
        }
        Collections.shuffle(allPositions);
        List<Integer> positions = allPositions.subList(0, zipfSize); // Extract from index 0 (included) to zipfSize (excluded)
        assert (positions.size() == zipfSize);

        // Step 3: Sample ExperimentParameters.NUM_TRIALS positions according to the Zipfian distribution

        ArrayList<Pair<Integer,Integer>> targetsList = new ArrayList<Pair<Integer,Integer>>();  // (position, zipfian rank)
        for(int i=1; i<=zipfSize; i++){
            for(int j=0; j< zipfian[i]; j++){
                targetsList.add(new Pair<Integer, Integer>(positions.get(i-1),i));
            }
        }
        assert(targetsList.size()==ExperimentParameters.NUM_TRIALS);
        Collections.shuffle(targetsList);

        List<Pair<Integer, Integer>> temp = targetsList.subList(0, ExperimentParameters.NUM_TRIALS);    // do we really need that temp one?
        assert(temp.size() == ExperimentParameters.NUM_TRIALS);
        Pair<Integer,Integer> target;
        for(int i=1; i<=ExperimentParameters.NUM_TRIALS; i++){
            target=temp.remove(0);
            targets[i]=target.first;
            target_ranks[i]=target.second;
        }

        // Step 4a: Generate highlighted icons sequence based on MFU

        for(int i=1; i<=ExperimentParameters.NUM_TRIALS; i++){
            for(int j=0; j<State.num_highlighted_icons; j++){
                highlighted[i][j]=positions.get(j);
            }
        }

        // Step 4b: Add the MRU icon if not already present

        for(int i=2; i<=ExperimentParameters.NUM_TRIALS; i++)
            highlightIconAtTrial(targets[i-1], i);

        // Step 5: Compute the accuracy

        int successes=computeSuccesses();
        empiricalAccuracy = ((double) successes)/ExperimentParameters.NUM_TRIALS;
        Log.v("Distributions","Empirical accuracy before adjusting = "+ empiricalAccuracy);

        // Step 6a: Adjust the accuracy up if necessary

        int min_successes= (int) Math.ceil(State.accuracy*ExperimentParameters.NUM_TRIALS);
        int to_adjust=min_successes-successes;

        if(to_adjust>0){ // successes < min_successes

            // Randomly select to_adjust trials
            ArrayList<Integer> trials = new ArrayList<Integer>();
            for(int i=1; i<=ExperimentParameters.NUM_TRIALS;i++)
                trials.add(i);
            Collections.shuffle(trials);
            for(Integer t:trials){
                if(to_adjust>0 && !isPredictionCorrect(t)){
                    adjustTrialSuccess(t);
                    to_adjust--;
                }
                else
                    randomlyChangeTrial(t);
            }
        }

        // Step 6b: Adjust the accuracy down if necessary

        int max_successes= (int) Math.floor(State.accuracy*ExperimentParameters.NUM_TRIALS);
        to_adjust=successes-max_successes;

        if(to_adjust>0){ // successes > max_successes

            // Randomly select to_adjust trials
            ArrayList<Integer> trials = new ArrayList<Integer>();
            for(int i=1; i<=ExperimentParameters.NUM_TRIALS;i++)
                trials.add(i);
            Collections.shuffle(trials);
            for(Integer t:trials){
                if(to_adjust>0 && isPredictionCorrect(t)){
                    adjustTrialFailure(t);
                    to_adjust--;
                }
                else
                    randomlyChangeTrial(t);
            }
        }

        // Step 7: Recompute the accuracy, just to be sure
        computeAccuracy();
        Log.v("Distributions","Empirical accuracy after adjusting = "+ empiricalAccuracy);
    }

    public static void computeAccuracy(){
        int successes=computeSuccesses();
        empiricalAccuracy = ((double) successes)/ExperimentParameters.NUM_TRIALS;
    }

    private static int computeSuccesses(){
        int successes=0;
        for(int i=1; i<=ExperimentParameters.NUM_TRIALS; i++){
            if(isPredictionCorrect(i))
                successes++;
        }
        return successes;
    }

    private static boolean isPredictionCorrect(int trial){
        return isAmongHighlighted(trial, targets[trial]);
    }

    private static boolean isAmongHighlighted(int trial, int icon){
        for(int i=0; i<State.num_highlighted_icons; i++){
            if(highlighted[trial][i]==icon)
                return true;
        }
        return false;
}

    public static void highlightIconAtTrial(int icon, int trial){
        // If the MRU icon is amongst the MFU, do nothing
        if(!isAmongHighlighted(trial, icon))
            highlighted[trial][State.num_highlighted_icons-1]=icon;
    }

    // Change one highlighted icon to make the prediction a success
    private static void adjustTrialSuccess(int trial){
        int icon=(int) Math.floor(Math.random()*State.num_highlighted_icons);		// int between 0 and HIGHLIGHTED-1
        highlighted[trial][icon]=targets[trial];
    }

    // Change one highlighted icon to make the prediction a failure
    private static void adjustTrialFailure(int trial){
        int icon = findTargetAmongHighlighted(trial);
        int replaceBy=selectIconNotHighlightedNotTarget(trial);
        highlighted[trial][icon]=replaceBy;
    }

    // Change one highlighted icon without modifying the correctness of the prediction
    private static void randomlyChangeTrial(int trial){
        int icon;
        int replaceBy=(int) Math.floor(Math.random()* State.num_positions);

        if(!isPredictionCorrect(trial))
            icon=(int) Math.floor(Math.random()*State.num_highlighted_icons);		// int between 0 and HIGHLIGHTED-1
        else
            icon=selectOneHighlightedExceptTarget(trial);

        highlighted[trial][icon]=replaceBy;
    }

    // Helper function
    private static int selectOneHighlightedExceptTarget(int trial){
        assert(isPredictionCorrect(trial));
        ArrayList<Integer> highlight= new ArrayList<Integer>();
        for(int i=0;i<State.num_highlighted_icons;i++){
            if(highlighted[trial][i] != targets[trial])
                highlight.add(i);
        }
        Collections.shuffle(highlight);
        return highlight.get(0);
    }

    private static int findTargetAmongHighlighted(int trial){
        assert(isPredictionCorrect(trial));
        for(int i=0;i<State.num_highlighted_icons;i++){
            if(highlighted[trial][i] == targets[trial])
                return i;
        }
        return -1;
    }

    private static int selectIconNotHighlightedNotTarget(int trial){
        assert(isPredictionCorrect(trial));
        ArrayList<Integer> allPositionsExceptHighlighted= new ArrayList<Integer>(); // also encompasses target since prediction is correct
        for(int i=0; i<State.num_positions;i++){
            if(!isAmongHighlighted(trial,i))
                allPositionsExceptHighlighted.add(i);
        }
        return allPositionsExceptHighlighted.get(0);
    }
}
