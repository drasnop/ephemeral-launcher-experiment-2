package ca.ubc.cs.ephemerallauncherexperiment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
		
		//Initialize icon order
		
		//Initialize target and highlighted icons
		
		//Initialize condition order
		
		//This is just for test now
		State.listOfConditions = new ArrayList<ExperimentParameters.ConditionEnum>();
		State.listOfConditions.add(ExperimentParameters.ConditionEnum.PRACTICE);
		State.listOfConditions.add(ExperimentParameters.ConditionEnum.PULSEOUT);
		State.listOfConditions.add(ExperimentParameters.ConditionEnum.TRANSPARENCY);
		State.listOfConditions.add(ExperimentParameters.ConditionEnum.TWIST);
		
		State.condition = State.listOfConditions.get(0);
				
		
		//Initialize log file
		
		FileManager.openFile(this, "EXP1", getFileName());
		FileManager.writeToFile(this.getString(R.string.trial_log_header), false);
		
	}
	
	public void startFirstCondition(View view){
		//FileManager.testSavingToSdCard(this);
		initializeExperiment();
			
		Intent intent = new Intent(this, Condition.class);
		startActivity(intent);
	}

}
