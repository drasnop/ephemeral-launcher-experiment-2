package ca.ubc.cs.ephemerallauncherexperiment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
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
	
	public void startCondition(View view){
		testSavingToSdCard();
		/*Intent intent = new Intent(this, Condition.class);
		startActivity(intent);*/
	}
	
	public void testSavingToSdCard() {
		String filename = "file1";
		File file = new File(getStorageDir(this, "dir1"), filename);
		
		try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);	
			bw.write("HELLO MY NAME IS KAMYAR!");
			bw.flush();
			bw.close(); }
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public File getStorageDir(Context context, String dirName) {
	     
	    File file = new File(context.getExternalFilesDir(
	            "EXPERIMENT_DATA"), dirName);
	    if (!file.mkdirs()) {
	        Log.e("LOG", "Directory not created");
	    }
	    return file;
	}

}
