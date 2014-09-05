package ca.ubc.cs.ephemerallauncherexperiment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import ca.ubc.cs.ephemerallauncher.LauncherParameters;

public class Condition extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_condition);
		initializeCondition();
	}
	
	private void initializeCondition(){
		ExperimentParameters.state.initStateForCondition();
        ExperimentParameters.distributions.initForCondition();
		LauncherParameters.ANIMATION = Utils.effectToAnimation(ExperimentParameters.state.effect);
        Logging.logDistributionsAtConditionInit(this);

		// Show block #
		((TextView) this.findViewById(R.id.start_condition)).setText("Condition #"+ExperimentParameters.state.block);

        // Show # of pages
        ((TextView) this.findViewById(R.id.num_pages)).setText(ExperimentParameters.state.num_pages+" pages");
	}
	
	public void startFirstTrial(View view) {
		Intent intent = new Intent(this, Trial.class);
		intent.putExtra(ExperimentParameters.SUCCESS_MESSAGE, "Practice");
        ExperimentParameters.state.trial_timeout_ms = ExperimentParameters.PRACTICE_TRIAL_TIMEOUT_MS;
		startActivity(intent);
	}
	
	@Override //KZ
	public void onBackPressed(){}
}
