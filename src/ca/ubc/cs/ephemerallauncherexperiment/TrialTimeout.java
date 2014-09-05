package ca.ubc.cs.ephemerallauncherexperiment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TrialTimeout extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trial_timeout);
	}

	public void startNextTrial(View view) {

		String message = "Timeout";

		ExperimentParameters.state.trial++;
		if (ExperimentParameters.state.trial > ExperimentParameters.NUM_TRIALS) {
			// end condition

			ExperimentParameters.state.block++;

			if (ExperimentParameters.state.block == ExperimentParameters.NUM_CONDITIONS) {
				Intent intent = new Intent(this, EndOfExperiment.class);
				intent.putExtra(ExperimentParameters.SUCCESS_MESSAGE, message);
				startActivity(intent);
			} else {

				Intent intent = new Intent(this, EndOfCondition.class);
				intent.putExtra(ExperimentParameters.SUCCESS_MESSAGE, message);
				startActivity(intent);
			}
		} else {
			Intent intent = new Intent(this, Trial.class);
			intent.putExtra(ExperimentParameters.SUCCESS_MESSAGE, message);
			startActivity(intent);
		}
	}

	@Override
	// KZ
	public void onBackPressed() {
	}
}
