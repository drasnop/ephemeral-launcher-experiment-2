package ca.ubc.cs.ephemerallauncherexperiment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import ca.ubc.cs.ephemerallauncher.LauncherParameters;

public class Condition extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_condition);
		
		pickIconsForCondition();
		
		State.trial=1;
		State.page=1;	// maybe useless
		State.condition = ExperimentParameters.ConditionEnum.values()[Distributions.conditions[State.participantNum][State.block]];
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.condition, menu);
		return true;
	}

	

	
	public void startFirstTrial(View view) {

		Intent intent = new Intent(this, Trial.class);
		startActivity(intent);
	}

	// AP: should we put this function in Distributions?
	// KA: or we could initialize everything in Experiment.initializeExperiment()
	// AP: Agreed. I was thinking of doing it once for the entire experiment.
	// However, I'm thinking of putting the "random selection" part in Distributions, because it's sort of a distribution... thoughts?
	//KA: Agree, just call it in initializeExperiment()
	private void pickIconsForCondition(){
		// TODO: randomly select 60 icons
		// For the moment: just take all our set
		for(int p=0;p<3;p++){
			for(int i=1; i<=20; i++){
				State.current_images_ID[p*20+i]=LauncherParameters.images_ID[p*20+i-1];
			}
		}
		Utils.shuffleArrayExceptZero(State.current_images_ID);
	}
	
	@Override //KZ
	public void onBackPressed(){}
}
