package ca.ubc.cs.ephemerallauncherexperiment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class EndOfExperiment extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end_of_experiment);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.end_of_experiment, menu);
		return true;
	}

	public void finishExperiment(View view){
		Intent intent = new Intent(this, Experiment.class);
		startActivity(intent);
	}
	
}
