package ca.ubc.cs.ephemerallauncherexperiment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import ca.ubc.cs.ephemerallauncher.LauncherParameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Experiment extends Activity implements OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experiment);

        // Initialize State and Distributions
        ExperimentParameters.state=new State();
        ExperimentParameters.distributions=new Distributions();
        ExperimentParameters.state.initExperiment();

        // Selection of participants
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
        ExperimentParameters.state.participant=pos;
        ExperimentParameters.state.participantId="P"+pos;
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
    }

    public void startFirstCondition(View view){
        if(ExperimentParameters.state.participant<0){
            Toast.makeText(this, "Please select participant #", Toast.LENGTH_SHORT).show();
        } else {
            initializeExperiment();

            Intent intent = new Intent(this, Condition.class);
            startActivity(intent);
        }
    }

    private void initializeExperiment(){

        // Initialize zipfian
        ExperimentParameters.distributions.initForExperiment();

        // Initialize logging
        Logging.initialize(this);

        //Log distributions
        Logging.logDistributionsAtExperimentInit();
    }

    public void restore(View view) {

        if(ExperimentParameters.state.participant<0) {
            Toast.makeText(this, "Please select participant #", Toast.LENGTH_SHORT).show();
            return;
        }

        try{
            File backup = new File(FileManager.getExtStorageDir(this, ExperimentParameters.LOG_FOLDER),Logging.getExperimentBackupFileName());
            if(!backup.exists()){
                Toast.makeText(this, "No backup file found for this participant!", Toast.LENGTH_SHORT).show();
                return;
            }

            FileInputStream fis = new FileInputStream(backup);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ExperimentParameters.state = (State)ois.readObject();
            ExperimentParameters.distributions = (Distributions)ois.readObject();
            ois.close();

            LauncherParameters.ANIMATION = Utils.effectToAnimation(ExperimentParameters.state.effect);

            Intent intent = new Intent(this, Trial.class);
            intent.putExtra(ExperimentParameters.SUCCESS_MESSAGE, "Restored");
            if(ExperimentParameters.state.trial==0)
                ExperimentParameters.state.trial_timeout_ms = ExperimentParameters.PRACTICE_TRIAL_TIMEOUT_MS;
            startActivity(intent);

        }catch(IOException e ){
            e.printStackTrace();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    @Override //KZ
    public void onBackPressed(){}
}
