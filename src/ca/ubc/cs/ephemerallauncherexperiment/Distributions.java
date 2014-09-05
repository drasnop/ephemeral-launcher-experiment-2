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

    public static ArrayList<Integer> allAvailableIcons = new ArrayList<Integer>();
    public static List<Integer> targets_list = new ArrayList<Integer>();

    // We take the maximum number of icons needed for any condition; therefore for half the conditions the array will not be full
    public static int[][] highlighted = new int[ExperimentParameters.NUM_TRIALS+1][ExperimentParameters.MAX_NUM_HIGHLIGHTED_ICONS];
    public static Integer[] images_ID = new Integer[ExperimentParameters.MAX_NUM_POSITIONS+1];
    /*public static Integer[] images_gs_ID = new Integer[ExperimentParameters.MAX_NUM_POSITIONS+1];*/
    public static Integer[] labels_ID = new Integer[ExperimentParameters.MAX_NUM_POSITIONS+1];

    public static Integer image_ID_practice;
    public static Integer label_ID_practice;
    public static Integer image_ID_stored;
    public static Integer label_ID_stored;

    public static double empiricalAccuracy;

    // Actual number of targets, which can be smaller than zipfSize
    public static int numNonZeroInZipfian(){
        int count=0;
        for(Integer f : zipfian){
            if(f>0)
                count++;
        }
        return count;
    }

    // Reusing icons except for the ones that were targets
    private static void initIconDistributionForExperiment() {

        // Verify that we have enough icons (+1 is for practice trial)
        assert(ExperimentParameters.MAX_NUM_POSITIONS+(ExperimentParameters.NUM_CONDITIONS-1)*numNonZeroInZipfian() + 1 <= 301);

        // Load all available icons
        for (int i = 0; i < ExperimentParameters.MAX_NUM_POSITIONS+(ExperimentParameters.NUM_CONDITIONS-1)*numNonZeroInZipfian(); i++) {
            allAvailableIcons.add(i);
        }

        // Select the target icons that will be used for the practice trials throughout the experiment
        int icon = allAvailableIcons.remove(0);
        image_ID_practice = LauncherParameters.images_ID[icon];
        label_ID_practice = LauncherParameters.labels_ID[icon];
    }


    public static void initForExperiment() {

        // Step 0: Generate Zipfian distribution of frequencies (the first cell of the array is not used)

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

        // Step 1:  Prepare the random selection of icons and labels for the entire experiment

        initIconDistributionForExperiment();
    }

    // Happens AFTER State.initForCondition();
    public static void initForCondition() {

        // Step 2: Pick zipfSize positions for the target icon, in [1..State.num_pages*20]

        ArrayList<Integer> allPositions = new ArrayList<Integer>();
        for (int i = 1; i <= State.num_positions(); i++) {
            allPositions.add(i);
        }
        Collections.shuffle(allPositions);
        // TODO replace zipfSize by numNonZeroZipfian (or try to use only one list)
        List<Integer> positionsOfTargets = allPositions.subList(0, zipfSize);   // Extract from index 0 (included) to zipfSize (excluded)
        targets_list = positionsOfTargets.subList(0,numNonZeroInZipfian());     // save these for later use
        assert (positionsOfTargets.size() == zipfSize);

        // Step 3: Sample ExperimentParameters.NUM_TRIALS positions according to the Zipfian distribution

        ArrayList<Pair<Integer,Integer>> targetsList = new ArrayList<Pair<Integer,Integer>>();  // (position, zipfian rank)
        for(int i=1; i<=zipfSize; i++){
            for(int j=0; j< zipfian[i]; j++){
                targetsList.add(new Pair<Integer, Integer>(positionsOfTargets.get(i-1),i));
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

        // Step 4a: Prepare highlighted icons array

        for(int i=1; i<=ExperimentParameters.NUM_TRIALS; i++) {
            for (int j = 0; j < ExperimentParameters.MAX_NUM_HIGHLIGHTED_ICONS; j++)
                highlighted[i][j] = -1;
        }

        // Step 4b: Generate highlighted icons sequence based on MFU

        for(int i=1; i<=ExperimentParameters.NUM_TRIALS; i++){
            // Fill up with MFU first
            for(int j=0; j<Math.min(State.num_highlighted_icons(),positionsOfTargets.size()); j++)
                highlighted[i][j]=positionsOfTargets.get(j);

            // If there are more highlighted icons than targets, they will be filled up by random highlighting
            assert(State.num_highlighted_icons()-positionsOfTargets.size()<State.num_randomly_highlighted_icons);
        }

        // Step 4c: Add random highlighted icons

        for(int i=1; i<=ExperimentParameters.NUM_TRIALS; i++){
            for(int j=1; j<=State.num_randomly_highlighted_icons; j++){
                highlighted[i][State.num_highlighted_icons()-j]=selectIconNotHighlightedNotTarget(i);   // start at the end, to remove the least MFU
            }
        }

        // Step 4d: Add the MRU icon(s) if not already present (highlights at least 1 mru)

        int offset=ExperimentParameters.NUM_RANDOMLY_HIGHLIGHTED_ICONS[State.index_num_pages()];
        for(int m=1; m<ExperimentParameters.NUM_MRU_HIGHLIGHTED_ICONS[State.index_num_pages()]; m++){
            for(int i=1+m; i<=ExperimentParameters.NUM_TRIALS; i++) {
                highlightIconAtTrial(targets[i - m - offset], i);    // additional offset because we don't want to override the random icons
            }
        }

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
            }
        }

        // Step 7: Recompute the accuracy, just to be sure
        computeAccuracy();
        Log.v("Distributions","Empirical accuracy after adjusting = "+ empiricalAccuracy);

        // Step 8: Generate target and highlighted icons for the practice trial
        initPracticeTrial();

        // Step 9: Select the other icons (=pictures+labels) to be displayed
        initIconDistributionForCondition();
    }

    private static void initPracticeTrial() {
        ArrayList<Integer> allPositions = new ArrayList<Integer>();
        for (int i = 1; i <= State.num_positions(); i++) {
            allPositions.add(i);
        }
        Collections.shuffle(allPositions);

        targets[0]=allPositions.get(0);

        for (int i = 0; i < State.num_highlighted_icons(); i++) {
            highlighted[0][i]=allPositions.get(i);
        }
    }

    // Happens at the end of the initiation process
    private static void initIconDistributionForCondition() {
        // Step a: shuffle the remaining icons
        Collections.shuffle(allAvailableIcons);

        // Step b: assign images to position, while removing the target icons from the allAvailable set
        int icon;
        int offset=0;
        for(int p=1; p<=State.num_positions();p++){
            if(targets_list.contains(p)){
                icon=allAvailableIcons.remove(p-offset); // because we have removed offset icons from the list in between
                offset++;
            }
            else{
                icon=allAvailableIcons.get(p - offset);   // because we have removed offset icons from the list in between
            }
            images_ID[p] = LauncherParameters.images_ID[icon];
            /*images_gs_ID[p] = LauncherParameters.images_gs_ID[positions];*/
            labels_ID[p] = LauncherParameters.labels_ID[icon];
        }

        // Step c: temporarily swap an icon with the practice target icon
        image_ID_stored = images_ID[targets[0]];
        images_ID[targets[0]] = image_ID_practice;

        label_ID_stored = labels_ID[targets[0]];
        labels_ID[targets[0]] = label_ID_practice;
    }

    public static void unSwapIconsAfterPractice(){
        images_ID[targets[0]] = image_ID_stored;
        labels_ID[targets[0]] = label_ID_stored;
    }

    ///////////////////    Helper functions    //////////////////////////////////


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

    private static boolean isAmongHighlighted(int trial, int position){
        for(int i=0; i<State.num_highlighted_icons(); i++){
            if(highlighted[trial][i]==position)     // highlighted may still be incomplete (= full of -1)
                return true;
        }
        return false;
    }

    public static boolean isHighlighted(int position){
        for(Integer icon:Distributions.highlighted[State.trial]){
            if(position == icon)
                return true;
        }
        return false;
    }

    private static boolean isTarget(int trial, int position){
        return targets[trial]==position;
    }

    public static void highlightIconAtTrial(int icon, int trial){
        // If the MRU icon is amongst the MFU, do nothing
        if(!isAmongHighlighted(trial, icon))
            highlighted[trial][State.num_highlighted_icons()-1]=icon;
    }

    // Change last highlighted icon to make the prediction a success
    private static void adjustTrialSuccess(int trial){
        highlighted[trial][State.num_highlighted_icons()-1]=targets[trial];
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
        int replaceBy=(int) Math.floor(Math.random()* State.num_positions());

        if(!isPredictionCorrect(trial))
            icon=(int) Math.floor(Math.random()*State.num_highlighted_icons());		// int between 0 and HIGHLIGHTED-1
        else
            icon=selectOneHighlightedExceptTarget(trial);

        highlighted[trial][icon]=replaceBy;
    }

    // Helper function
    private static int selectOneHighlightedExceptTarget(int trial){
        assert(isPredictionCorrect(trial));
        ArrayList<Integer> highlight= new ArrayList<Integer>();
        for(int i=0;i<State.num_highlighted_icons();i++){
            if(highlighted[trial][i] != targets[trial])
                highlight.add(i);
        }
        Collections.shuffle(highlight);
        return highlight.get(0);
    }

    private static int findTargetAmongHighlighted(int trial){
        assert(isPredictionCorrect(trial));
        for(int i=0;i<State.num_highlighted_icons();i++){
            if(highlighted[trial][i] == targets[trial])
                return i;
        }
        return -1;
    }

    private static int selectIconNotHighlightedNotTarget(int trial){
        ArrayList<Integer> allPositionsExceptHighlightedAndTarget= new ArrayList<Integer>();
        for(int p=1; p<=State.num_positions();p++){
            if(!isAmongHighlighted(trial,p) && !isTarget(trial,p))
                allPositionsExceptHighlightedAndTarget.add(p);
        }
        Collections.shuffle(allPositionsExceptHighlightedAndTarget);
        return allPositionsExceptHighlightedAndTarget.get(0);
    }

}
