package ca.ubc.cs.ephemerallauncherexperiment;

import java.util.ArrayList;

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
	
	@Override //KZ
	public void onBackPressed(){}

	private String getTrialLogFileName(){
		String fileName =  Utils.getTimeStamp(true) + "__" + State.participantId+".log";
//		Toast.makeText(this, fileName, Toast.LENGTH_SHORT).show();
		return fileName;
	}
	
	private String getEventsLogFileName(){
		String fileName = Utils.getTimeStamp(true) + "__" + State.participantId + "__EVENTS" + ".log";
		return fileName;
	}
	
	private String getExperimentLogFileName(){
		String fileName = ExperimentParameters.EXPERIMENT_LOG_FILE_NAME;
		return fileName;
	}
	
	
	private void initializeExperiment(){
			
		//Initialize condition order

		// Initialize target and highlighted icons
		Distributions.init();
		// TODO: write the distributions in a log file (Kamyar if you want to do that don't hesitate!)
		
		//Initialize icon order
		
				
		
		//Initialize trial log file
		
		State.currentTrialsLogFile = FileManager.getFile(this, ExperimentParameters.LOG_FOLDER, getTrialLogFileName());
		FileManager.writeToFile(State.currentTrialsLogFile, Utils.appendWithComma(this.getString(R.string.state_log_header), this.getString(R.string.trial_log_header)), false);
		
		//Initialize event log file
		
		State.currentEventsLogFile = FileManager.getFile(this, ExperimentParameters.LOG_FOLDER, getEventsLogFileName());
		FileManager.writeToFile(State.currentEventsLogFile, Utils.appendWithComma(this.getString(R.string.state_log_header), this.getString(R.string.events_log_header)), false);
		
		//Initialize experiment log file
		
		State.currentExperimentLogFile = FileManager.getFile(this, ExperimentParameters.LOG_FOLDER, getExperimentLogFileName());
		//FileManager.appendLineToFile(State.currentExperimentLogFile, Utils.appendWithComma(this.getString(R.string.experiment_log_header), this.getString(R.string.experiment_parameters_log_header)));
		FileManager.appendLineToFile(State.currentExperimentLogFile, Utils.appendWithComma(Utils.getTimeStamp(false), State.participantId, ExperimentParameters.csvFile()));
		
	
		
		
	}
	
	public void startFirstCondition(View view){
		//FileManager.testSavingToSdCard(this);
		initializeExperiment();
	
		Intent intent = new Intent(this, Condition.class);
		startActivity(intent);
	}

}
