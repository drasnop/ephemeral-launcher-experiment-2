package ca.ubc.cs.ephemerallauncherexperiment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class Experiment extends Activity implements OnItemSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_experiment);
		
		// (re)Initialize everything in State
		State.initExperiment();
		
		Integer participants[] = new Integer[ExperimentParameters.NUM_PARTICIPANTS];
		for(int i=0; i<ExperimentParameters.NUM_PARTICIPANTS; i++){
			participants[i]=i;
		}
		
		// Setup drop-down selector for participant number
		NoDefaultSpinner spinner = (NoDefaultSpinner) findViewById(R.id.participants_spinner);
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
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
    }
	
	public void startFirstCondition(View view){	
		if(State.participant<0){
			Toast.makeText(this, "Please select participant #", Toast.LENGTH_SHORT).show();
		} else {
		initializeExperiment();
		
		Intent intent = new Intent(this, Condition.class);
		startActivity(intent);
		}
	}
	
	private void initializeExperiment(){
		
		// Initialize target, highlighted icons, images and labels 
		Distributions.initForExperiment();

        // Initialize logging
        Logging.initialize(this);
		
		//Log distributions
        Logging.logDistributionsAtExperimentInit(this);
	}
	

	@Override //KZ
	public void onBackPressed(){}
}
