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
		for(int pos=1; pos<= Distributions.NUM_POSITIONS; pos++){
			State.current_images_ID[pos]=Distributions.images_ID[State.block][pos-1];
				
		}
		
		for(int pos=1; pos <= Distributions.NUM_POSITIONS; pos++){
			State.current_labels_ID[pos]=Distributions.labels_ID[State.block][pos-1];
				
		}
		//Utils.shuffleArrayExceptZero(State.current_images_ID); done in Distributions
		
	}
	
	@Override //KZ
	public void onBackPressed(){}
}
