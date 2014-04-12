package ca.ubc.cs.ephemerallauncherexperiment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.util.Log;
import ca.ubc.cs.ephemerallauncher.LauncherParameters;

public class Distributions {

	public static final double zipfCoeff = 1;
	public static final int zipfSize = ExperimentParameters.zipfSize;
	public static int NUM_POSITIONS = LauncherParameters.NUM_PAGES * LauncherParameters.NUM_ICONS_PER_PAGE;

	// latin square
	// http://www.maths.qmul.ac.uk/~rab/DOEbook/doeweb6.pdf
	// http://statpages.org/latinsq.html and look for latinsq5.txt or .csv in Drive
	public static int[][] conditions = {
		{0,1,4,2,3},
		{1,2,0,3,4},
		{2,3,1,4,0},
		{3,4,2,0,1},
		{4,0,3,1,2},
		{3,2,4,1,0},
		{4,3,0,2,1},
		{0,4,1,3,2},
		{1,0,2,4,3},
		{2,1,3,0,4}
	};
	
	private static int[] zipf = new int[zipfSize+1];
	public static int[] targets = new int[ExperimentParameters.NUM_TRIALS+1]; 
	public static int[][] highlighted = new int[ExperimentParameters.NUM_TRIALS+1][LauncherParameters.NUM_HIGHLIGHTED_ICONS];
	
	public static Integer[][] images_ID = new Integer[ExperimentParameters.NUM_CONDITIONS][NUM_POSITIONS];
	public static Integer[][] labels_ID = new Integer[ExperimentParameters.NUM_CONDITIONS][NUM_POSITIONS];

	public static double accuracy;
	
	private static void iconDistributionInit() {
		ArrayList<Integer> allExperimentPositions = new ArrayList<Integer>();
		for (int i = 0; i < NUM_POSITIONS*ExperimentParameters.NUM_CONDITIONS; i++) {
			allExperimentPositions.add(i);
		}
		Collections.shuffle(allExperimentPositions);
		
		for (int i = 0; i < ExperimentParameters.NUM_CONDITIONS; i++)
			for (int j=0; j < NUM_POSITIONS; j++)
				images_ID[i][j] = LauncherParameters.images_ID[allExperimentPositions.get(i*NUM_POSITIONS+j)];
		
	}
	
	private static void labelDistributionInit(){
		ArrayList<Integer> allExperimentPositions = new ArrayList<Integer>();
		for (int i = 0; i < NUM_POSITIONS*ExperimentParameters.NUM_CONDITIONS; i++) {
			allExperimentPositions.add(i);
		}
		Collections.shuffle(allExperimentPositions);
		
		for (int i = 0; i < ExperimentParameters.NUM_CONDITIONS; i++)
			for (int j=0; j < NUM_POSITIONS; j++)
				labels_ID[i][j] = LauncherParameters.labels_ID[allExperimentPositions.get(i*NUM_POSITIONS+j)];
	}
	
	
	public static void init() {

		// Step 0:  Randomly select the icons and labels for the entire experiment
		
		iconDistributionInit();
		labelDistributionInit();
		
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
			zipf[i]=frequency;
			sum_frequencies+=frequency;
		}
		assert(sum_frequencies==ExperimentParameters.NUM_TRIALS);
		
		// Step 2: Pick zipfSize positions for the target icon, in [1..LauncherParameters.NUM_PAGES*20]
		
		ArrayList<Integer> allPositions = new ArrayList<Integer>();
		for (int i = 1; i <= NUM_POSITIONS; i++) {
			allPositions.add(i);
		}
		Collections.shuffle(allPositions);
		List<Integer> positions = allPositions.subList(0, zipfSize); // Extract from index 0 (included) to zipfSize (excluded)
		assert (positions.size() == zipfSize);

		// Step 3: Sample ExperimentParameters.NUM_TRIALS positions according to  the Zipfian distribution
		
		ArrayList<Integer> targetsList = new ArrayList<Integer>();
		for(int i=1; i<=zipfSize; i++){
			for(int j=0; j<zipf[i]; j++){
				targetsList.add(positions.get(i-1));
			}
		}
		assert(targetsList.size()==ExperimentParameters.NUM_TRIALS);
		Collections.shuffle(targetsList);
		
		List<Integer> temp = targetsList.subList(0, ExperimentParameters.NUM_TRIALS);
		assert(temp.size() == ExperimentParameters.NUM_TRIALS);
		for(int i=1; i<=ExperimentParameters.NUM_TRIALS; i++){
			targets[i]=temp.remove(0);
		}
		
		// Step 4a: Generate highlighted icons sequence based on MFU
		
		for(int i=1; i<=ExperimentParameters.NUM_TRIALS; i++){
			for(int j=0; j<LauncherParameters.NUM_HIGHLIGHTED_ICONS; j++){
				highlighted[i][j]=positions.get(j);
			}
		}
		
		// Step 4b: Add the MRU icon if not already present
		
		for(int i=2; i<=ExperimentParameters.NUM_TRIALS; i++){
			// If the MRU icon is amongst the MFU, do nothing 
			if(!isAmongHighlighted(i, targets[i-1]))
				highlighted[i][LauncherParameters.NUM_HIGHLIGHTED_ICONS-1]=targets[i-1];
		}
		
		// Step 5: Compute the accuracy
		
		int successes=computeSuccesses();
		accuracy = ((double) successes)/ExperimentParameters.NUM_TRIALS;
		Log.v("Distributions","Empirical accuracy before adjusting = "+accuracy);
	
		// Step 6: Adjust the accuracy
		
		if(accuracy<ExperimentParameters.MIN_ACCURACY){
			int min_successes= (int) Math.ceil(ExperimentParameters.MIN_ACCURACY*ExperimentParameters.NUM_TRIALS);
			int to_adjust=min_successes-successes;
			
			// Randomly select to_adjust trials
			ArrayList<Integer> trials = new ArrayList<Integer>();
			for(int i=1; i<=ExperimentParameters.NUM_TRIALS;i++)
				trials.add(i);
			Collections.shuffle(trials);
			for(int i=0; i<trials.size(); i++){
				if(to_adjust>0 && !isPredictionCorrect(trials.get(i))){
					adjustTrial(trials.get(i));
					to_adjust--;
				}
				else 
					randomlyChangeTrial(trials.get(i));
			}
		}
		
		// Step 7: Recompute the accuracy, just to be sure
		successes=computeSuccesses();
		accuracy = ((double) successes)/ExperimentParameters.NUM_TRIALS;
		Log.v("Distributions","Empirical accuracy after adjusting = "+accuracy);
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
		for(int i=0; i<LauncherParameters.NUM_HIGHLIGHTED_ICONS; i++){
			if(highlighted[trial][i]==icon)
				return true;
		}
		return false;
	}
	
	// Change one highlighted icon to make the prediction a success
	private static void adjustTrial(int trial){
		int icon=(int) Math.floor(Math.random()*LauncherParameters.NUM_HIGHLIGHTED_ICONS);		// int between 0 and HIGHLIGHTED-1
		highlighted[trial][icon]=targets[trial];
	}
	
	// Change one highlighted icon without modifying the correctness of the prediction 
	private static void randomlyChangeTrial(int trial){
		int icon;
		int replaceBy=(int) Math.floor(Math.random()*NUM_POSITIONS);
		
		if(!isPredictionCorrect(trial))
			icon=(int) Math.floor(Math.random()*LauncherParameters.NUM_HIGHLIGHTED_ICONS);		// int between 0 and HIGHLIGHTED-1
		else
			icon=selectOneHighlightedExceptTarget(trial);
		
		highlighted[trial][icon]=replaceBy;
	}
	
	// Helper function
	private static int selectOneHighlightedExceptTarget(int trial){
		ArrayList<Integer> highlight= new ArrayList<Integer>();
		for(int i=0;i<ExperimentParameters.NUM_HIGHLIGHTED_ICONS;i++){
			if(highlighted[trial][i] != targets[trial])
				highlight.add(i);
		}
		Collections.shuffle(highlight);
		return highlight.get(0);
	}
	
	public static String distributionsLogFile(Context context){
		String logStr ="";
		
		String lineSep = "\n-------------------------------------------------------\n";
		String halfLineSep = "\n" + lineSep.substring(0, (int)lineSep.length()/2) + "\n";
		
		//general
		logStr += "GENERAL \n";
		logStr += "participantID: " + State.participantId + "\n";
		logStr += "zipfSize: " + String.valueOf(zipfSize) + "\n";
		logStr += "zipfCoeff: " + String.valueOf(zipfCoeff) + "\n";
		logStr += "Accuracy: " + String.valueOf(accuracy) + "\n";
		logStr += "Icon Set: " + ExperimentParameters.ICON_SET;
		logStr += "Label Set: " + ExperimentParameters.LABEL_SET;
		
		//logging conditions
		logStr += lineSep;
		logStr += "CONDITIONS \n";
		for (int i=0; i < ExperimentParameters.ConditionEnum.values().length; i++)
			logStr += ExperimentParameters.ConditionEnum.values()[i].toString() + " ";
		
		//logging zipf 
		logStr += lineSep;
		logStr += "ZIPF \n";
		for (int i=0; i < zipfSize; i++)
			logStr += String.valueOf(zipf[i+1]) + " ";
		
		
		//logging image icons and labels
		logStr += lineSep;
		logStr += "IMAGE ICONS AND LABELS\n";
		String iconAddressPrefix = ExperimentParameters.ICON_RESOURCE_ADDRESS_PREFIX;
		for (int c=0; c < ExperimentParameters.NUM_CONDITIONS; c++){
			logStr += halfLineSep;
			logStr += "CONDITION " + String.valueOf(c+1) + ": " + ExperimentParameters.ConditionEnum.values()[c].toString() +"\n";
			for (int pos=0; pos < NUM_POSITIONS; pos++){
				logStr += String.valueOf(pos+1) + " : " + Utils.extractIconName(context.getString(images_ID[c][pos]), iconAddressPrefix) + " "  + context.getString(labels_ID[c][pos]) + "; " ;
				if ((pos+1) % LauncherParameters.NUM_ICONS_PER_PAGE == 0)
					logStr += "\n";
								
			}
					
		}
		
	
		
		//logging target positions
		logStr += lineSep;
		logStr += "TARGET POSITIONS (positions start from 1) \n";
		for (int tr = 0; tr < ExperimentParameters.NUM_TRIALS; tr++){
			logStr += String.valueOf(targets[tr+1]) + " ";
		}
		
		//logging highlighted icons
		logStr += lineSep;
		logStr += "HIGHLIGHTED ICONS' POSITIONS \n";
		
		for (int tr = 0; tr < ExperimentParameters.NUM_TRIALS; tr++){
			for (int icon = 0; icon < ExperimentParameters.NUM_HIGHLIGHTED_ICONS; icon++)
				logStr += String.valueOf(highlighted[tr+1][icon]) + " ";
			logStr += "\n";
		}
		
		return logStr;
	}
	
}
