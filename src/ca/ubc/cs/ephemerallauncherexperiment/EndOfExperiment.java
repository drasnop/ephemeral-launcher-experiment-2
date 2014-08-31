package ca.ubc.cs.ephemerallauncherexperiment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class EndOfExperiment extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end_of_experiment);
		
		Distributions.computeAccuracy();
		Log.v("Distributions","Empirical accuracy after experiment = "+Distributions.accuracy);
		
		//Log empirical distributions (post-hoc)		
		State.currentDistributionsLogFile = FileManager.getFile(this,  ExperimentParameters.LOG_FOLDER, Experiment.getDistributionsFileName());
		FileManager.writeLineToFile(State.currentDistributionsLogFile, Distributions.postExperimentDistributionLogFile(), false);
	}

	public void finishExperiment(View view){
		Intent intent = new Intent(this, Experiment.class);
		startActivity(intent);
	}
	
}
