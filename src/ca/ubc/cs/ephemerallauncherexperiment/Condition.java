package ca.ubc.cs.ephemerallauncherexperiment;

import java.util.Calendar;
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.condition, menu);
		return true;
	}

	private String getFileName() {
		Calendar cal = Calendar.getInstance();
		String fileName = cal.toString() + " - " + State.participantId + " - " + State.condition.toString();
		return fileName;
	}
	
	public void startFirstTrial(View view) {

		Intent intent = new Intent(this, Trial.class);
		startActivity(intent);
	}

	// AP: should we put this function in Distributions?
	// KA: or we could initialize everything in Experiment.initializeExperiment()
	private void pickIconsForCondition(){
		// TODO: randomly select the relevant icons
		// For the moment: just duplicate our icon set
		for(int p=0;p<3;p++){
			for(int i=1; i<=20; i++){
				State.current_images_ID[p*20+i]=LauncherParameters.images_ID[i-1];
			}
		}
	}
}
