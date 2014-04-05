package ca.ubc.cs.ephemerallauncherexperiment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class Experiment extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_experiment);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.experiment, menu);
		return true;
	}
	
	public void startFirstCondition(View view){
		FileManager.testSavingToSdCard(this);
		
		// Set up everything for the experiment
		Distributions.init();
		
		// TODO: write the distributions in a log file (Kamyar if you want to do that don't hesitate!)
		
		Intent intent = new Intent(this, Condition.class);
		startActivity(intent);
	}

}
