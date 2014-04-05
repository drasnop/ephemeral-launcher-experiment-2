package ca.ubc.cs.ephemerallauncherexperiment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.ubc.cs.ephemerallauncher.LauncherParameters;

public class Distributions {

	public static final double zipfCoeff = 1;
	public static final int zipSize = 20;
	public static int NUM_POSITIONS = LauncherParameters.NUM_PAGES * 20;

	// latin square
	public static int[][] conditions = {
		{ 0, 1, 2, 3, 4 }
	};
	
	private static int[] zipf = new int[zipSize+1];
	public static int[] targets = new int[ExperimentParameters.NUM_TRIALS+1];
	public static int[][] highlighted;

	public static void init() {

		// Step 1: Generate Zipfian distribution of frequencies (the first cell of the array is not used)
		
		double denominator = 0;
		double sum_frequencies=0;
		int frequency;
		for (int i = 1; i <= zipSize; i++) {
			denominator += 1.0 / Math.pow(i, zipfCoeff);
		}
		for (int i = 1; i <= zipSize; i++) {
			frequency=(int) Math.round(1.0/Math.pow(i, zipfCoeff)/denominator*ExperimentParameters.NUM_TRIALS);
			if(frequency==0 && sum_frequencies<ExperimentParameters.NUM_TRIALS)
				frequency=1;
			zipf[i]=frequency;
			sum_frequencies+=frequency;
		}
		assert(sum_frequencies==ExperimentParameters.NUM_TRIALS);
		// Step 2: Pick zipfSize positions for the target icon, in [1..LauncherParameters.NUM_PAGES*20]
		
		ArrayList<Integer> allPositions = new ArrayList<Integer>();
		for (int i = 1; i <= LauncherParameters.NUM_PAGES * 20; i++) {
			allPositions.add(i);
		}
		Collections.shuffle(allPositions);
		List<Integer> positions = allPositions.subList(0, zipSize); // Extract from index 0 (included) to zipfSize (excluded)
		assert (positions.size() == zipSize);

		// Step 3: Sample ExperimentParameters.NUM_TRIALS positions according to  the Zipfian distribution
		
		ArrayList<Integer> targetsList = new ArrayList<Integer>();
		for(int i=1; i<=zipSize; i++){
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
				
	}

}
