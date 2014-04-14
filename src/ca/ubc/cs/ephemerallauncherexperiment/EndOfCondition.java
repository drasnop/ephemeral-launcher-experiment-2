package ca.ubc.cs.ephemerallauncherexperiment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EndOfCondition extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end_of_condition);
		// Show the Up button in the action bar.
		//setupActionBar();
	}
	
	public void startNextCondition(View view){
		Intent intent = new Intent(this, Condition.class);
		startActivity(intent);
	}
}
