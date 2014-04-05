package ca.ubc.cs.ephemerallauncherexperiment;

import java.util.ArrayList;
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
			
		//Initialize condition order

		// Initialize target and higlighted icons
		Distributions.init();
		// TODO: write the distributions in a log file (Kamyar if you want to do that don't hesitate!)
		
		//Initialize icon order
		
		
		//This is just for test now
		// AP: this might be redundant with State.conditions, which is a latin square
		// my idea was to do something like State.conditions[Pariticipant.id%4][State.block] 
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
