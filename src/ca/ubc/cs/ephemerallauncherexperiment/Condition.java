package ca.ubc.cs.ephemerallauncherexperiment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class Condition extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_condition);
		
		State.initCondition();
		
		pickIconsForCondition();		
		
		// Adjust the text
		((TextView) this.findViewById(R.id.start_condition)).setText("Condition #"+State.block);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.condition, menu);
		return true;
	}

	

	
	public void startFirstTrial(View view) {

		Intent intent = new Intent(this, Trial.class);
		intent.putExtra(ExperimentParameters.SUCCESS_MESSAGE, "None");
		startActivity(intent);
	}

	
	private void pickIconsForCondition(){
		for(int tr=1; tr<= Distributions.NUM_POSITIONS; tr++){
			State.current_images_ID[tr]=Distributions.images_ID[State.block][tr-1];
				
		}
		
		for(int tr=1; tr <= Distributions.NUM_POSITIONS; tr++){
			State.current_labels_ID[tr]=Distributions.labels_ID[State.block][tr-1];
				
		}
		//Utils.shuffleArrayExceptZero(State.current_images_ID); done in Distributions
		
	}
	
	@Override //KZ
	public void onBackPressed(){}
}
