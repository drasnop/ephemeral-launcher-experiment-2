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
		State.initStateForCondition();
        Distributions.initForCondition();
		LauncherParameters.ANIMATION = Utils.effectToAnimation(State.effect);
		
		// Adjust the text
		((TextView) this.findViewById(R.id.start_condition)).setText("Condition #"+State.block);
	}
	
	public void startFirstTrial(View view) {

		Intent intent = new Intent(this, Trial.class);
		intent.putExtra(ExperimentParameters.SUCCESS_MESSAGE, "None");
		startActivity(intent);
	}
	
	@Override //KZ
	public void onBackPressed(){}
}
