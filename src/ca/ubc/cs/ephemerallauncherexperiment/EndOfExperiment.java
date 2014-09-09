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
		
		ExperimentParameters.distributions.computeAccuracy();
		Log.v("Distributions","Empirical accuracy after experiment = "+ExperimentParameters.distributions.empiricalAccuracy);
		
		//Log empirical distributions (post-hoc)
        Logging.logPostExperimentDistributions();
	}

	public void finishExperiment(View view){
		Intent intent = new Intent(this, Experiment.class);
		startActivity(intent);
	}
	
}
