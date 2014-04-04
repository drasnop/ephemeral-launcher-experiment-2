package ca.ubc.cs.ephemerallauncherexperiment;

import java.util.ArrayList;
import java.util.Collections;

import ca.ubc.cs.ephemerallauncher.LauncherParameters;

public class Distributions {

	public static final double zipfCoeff = 1;
	public static final int zipSize = 20;
	public static int NUM_POSITIONS = LauncherParameters.NUM_PAGES * 20;

	// latin square
	public static int[][] conditions = {
		{ 0, 1, 2, 3, 4 }
	};
	
	private static int[] zipf;
	public static int[] targets = { 15, 12, 2, 6, 20 };
	public static int[][] highlighted;

	public static void init() {

		// Step 1: Generate Zipfian distribution of frequencies (the first cell of the array is not used)
		zipf = new int[zipSize+1];
		double denominator = 0;
		for (int i = 1; i <= zipSize; i++) {
			denominator += 1.0 / Math.pow(i, zipfCoeff);
		}
		for (int i = 1; i <= zipSize; i++) {
			zipf[i]=(int) Math.round(1.0/Math.pow(i, zipfCoeff)/denominator*ExperimentParameters.NUM_TRIALS);
		}

		// Step 2: Pick zipfSize positions for the target icon, in [1..LauncherParameters.NUM_PAGES*20]
		ArrayList<Integer> allPositions = new ArrayList<Integer>();
		for (int i = 1; i <= LauncherParameters.NUM_PAGES * 20; i++) {
			allPositions.add(i);
		}
		Collections.shuffle(allPositions);
		ArrayList<Integer> positions = (ArrayList<Integer>) allPositions.subList(0, zipSize); // Extract from index 0 (included) to zipfSize (excluded)
		assert (positions.size() == zipSize);

		// Step 3: Sample ExperimentParameters.NUM_TRIALS positions according to  the Zipfian distribution
		
	}
	
}
