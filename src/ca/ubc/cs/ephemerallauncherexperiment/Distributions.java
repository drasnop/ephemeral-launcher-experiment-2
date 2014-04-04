package ca.ubc.cs.ephemerallauncherexperiment;

import java.util.ArrayList;
import java.util.Collections;

import ca.ubc.cs.ephemerallauncher.LauncherParameters;

public class Distributions {
	// I'm not sure if we're going to keep this class, but for the moment:
	
	public static final int zipfCoeff=1;
	public static final int zipSize=20;
	public static int NUM_POSITIONS=LauncherParameters.NUM_PAGES*20;
	
	// latin square
	public static int[][] conditions= {
		{0,1,2,3,4}
	};
	
	public static int[] targets={15,12,2,6,20};
	
	public static int[][] highlighted;
	
	// hardcoded zipfian frequency distribution
	private static int[] zipf={11,6,4,3,2,2,2,1,1,1,1,1};
	
	public static void init(){
		// Step 1: Pick zipfSize positions for the target icon, in [1..LauncherParameters.NUM_PAGES*20]
		ArrayList<Integer> allPositions = new ArrayList<Integer>();
		for(int i=1; i<=LauncherParameters.NUM_PAGES*20;i++){
			allPositions.add(i);
		}
		Collections.shuffle(allPositions);
		ArrayList<Integer> positions = (ArrayList<Integer>) allPositions.subList(0, zipSize);	// from index 0 (included) to zipfSize (excluded);
		assert(positions.size()==zipSize);
		
		// Step 2: Sample ExperimentParameters.NUM_TRIALS positions according to the Zipfian distribution
	}
}
