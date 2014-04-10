package ca.ubc.cs.ephemerallauncherexperiment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.ubc.cs.ephemerallauncher.LauncherParameters;

public class Distributions {

	public static final double zipfCoeff = 1;
	public static final int zipfSize = ExperimentParameters.zipfSize;
	public static int NUM_POSITIONS = LauncherParameters.NUM_PAGES * LauncherParameters.NUM_ICONS_PER_PAGE;

	// latin square
	// http://www.maths.qmul.ac.uk/~rab/DOEbook/doeweb6.pdf
	public static int[][] conditions = {
		{ 0, 1, 2, 3, 4 }
	};
	
	private static int[] zipf = new int[zipfSize+1];
	public static int[] targets = new int[ExperimentParameters.NUM_TRIALS+1];
	public static int[][] highlighted = new int[ExperimentParameters.NUM_TRIALS+1][LauncherParameters.NUM_HIGHLIGHTED_ICONS];

	public static double accuracy;
	
	public static void init() {

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
		
		// Step 4: Generate highlighted icons sequence
		
		// For the moment, just the most frequently used
		for(int i=1; i<=ExperimentParameters.NUM_TRIALS; i++){
			for(int j=0; j<LauncherParameters.NUM_HIGHLIGHTED_ICONS; j++){
				highlighted[i][j]=positions.get(j);
			}
		}
		
		// Step 5: Compute the accuracy
		int successes=0;
		for(int i=1; i<=ExperimentParameters.NUM_TRIALS; i++){
			if(correctPrediction(i))
				successes++;
		}
		accuracy= ((double) successes)/ExperimentParameters.NUM_TRIALS;					
	}

	private static boolean correctPrediction(int trial){
		for(int i=0; i<LauncherParameters.NUM_HIGHLIGHTED_ICONS; i++){
			if(highlighted[trial][i]==targets[trial])
				return true;
		}
		return false;
	}
	
}
