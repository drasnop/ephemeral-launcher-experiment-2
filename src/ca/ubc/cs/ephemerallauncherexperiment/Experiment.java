package ca.ubc.cs.ephemerallauncherexperiment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

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
	
	private String getFileName(){
		String fileName =  Utils.getTimeStamp(true) + " - " + State.participantId;
		Toast.makeText(this, fileName, Toast.LENGTH_SHORT).show();
		return fileName;
	}
	
	private void initializeExperiment(){
		
		
	}
	
	public void startFirstCondition(View view){
		//FileManager.testSavingToSdCard(this);
		
		FileManager.openFile(this, "EXP1", getFileName());
		FileManager.writeToFile(this.getString(R.string.trial_log_header), false);
		
		Intent intent = new Intent(this, Condition.class);
		startActivity(intent);
	}

}
