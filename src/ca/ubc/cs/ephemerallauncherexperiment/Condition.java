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
		State.initCondition();	
		LauncherParameters.ANIMATION = Utils.conditionToAnimation(State.condition);	
		pickIconsForCondition();		
		
		// Adjust the text
		((TextView) this.findViewById(R.id.start_condition)).setText("Condition #"+State.block);
	}
	
	public void startFirstTrial(View view) {

		Intent intent = new Intent(this, Trial.class);
		intent.putExtra(ExperimentParameters.SUCCESS_MESSAGE, "None");
		startActivity(intent);
	}

	private void pickIconsForCondition(){
		for(int pos=1; pos<= Distributions.NUM_POSITIONS; pos++){
			State.current_images_ID[pos]=Distributions.images_ID[State.block][pos-1];
			/*State.current_images_gs_ID[pos]=Distributions.images_gs_ID[State.block][pos-1];*/
		}
		
		for(int pos=1; pos <= Distributions.NUM_POSITIONS; pos++){
			State.current_labels_ID[pos]=Distributions.labels_ID[State.block][pos-1];			
		}	
	}
	
	@Override //KZ
	public void onBackPressed(){}
}
