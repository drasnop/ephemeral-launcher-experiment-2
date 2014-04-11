package ca.ubc.cs.ephemerallauncherexperiment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Experiment extends Activity implements OnItemSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_experiment);
		
		Integer participants[] = new Integer[ExperimentParameters.NUM_PARTICIPANTS];
		for(int i=0; i<ExperimentParameters.NUM_PARTICIPANTS; i++){
			participants[i]=i;
		}
		
		// Setup dropdown selector for participant number
		Spinner spinner = (Spinner) findViewById(R.id.participants_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item,participants);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
		spinner.setAdapter(adapter);
		// Specify actions when clicked, use this because implements OnItemSelectedListerner
		spinner.setOnItemSelectedListener(this);
	}
	
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        State.participant=pos;
        State.participantId="P"+pos;
    	// TODO log "P"+pos
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
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
	
	private String getDistributionsFileName(){
		String fileName = Utils.getTimeStamp(true) + "__" + State.participantId + "__DISTRIBUTIONS" + ".log";
		return fileName;
	}
	
	private String getExperimentLogFileName(){
		String fileName = ExperimentParameters.EXPERIMENT_LOG_FILE_NAME;
		return fileName;
	}
	
	
	private void initializeExperiment(){
			
		// (re)Initialize everything in State
		State.init();
		
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
		if (!State.currentExperimentLogFile.exists()){
			FileManager.writeToFile(State.currentExperimentLogFile, Utils.appendWithComma(this.getString(R.string.experiment_log_header), this.getString(R.string.experiment_parameters_log_header)), false);
		}
		
		FileManager.appendLineToFile(State.currentExperimentLogFile, Utils.appendWithComma(Utils.getTimeStamp(false), State.participantId, ExperimentParameters.csvFile()));
		
		//Log distributions
		
		State.currentDistributionsLogFile = FileManager.getFile(this,  ExperimentParameters.LOG_FOLDER, getDistributionsFileName());
		FileManager.writeLineToFile(State.currentDistributionsLogFile, Distributions.distributionsLogFile(this), false);
		
	
		
		
	}
	
	public void startFirstCondition(View view){
		//FileManager.testSavingToSdCard(this);
		initializeExperiment();
	
		Intent intent = new Intent(this, Condition.class);
		startActivity(intent);
	}

}
