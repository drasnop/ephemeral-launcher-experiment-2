package ca.ubc.ca.ephemerallauncherexperiment.deprecated;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class Condition extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_condition);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.condition, menu);
		return true;
	}
	
	public void startTrial(View view){
		
	Intent intent = new Intent(this, Trial.class);
	startActivity(intent);
	
	}
	/*	private String getFileName() {
	Calendar cal = Calendar.getInstance();
	String fileName = cal.toString() + " - " + State.participantId + " - " + State.condition.toString();
	return fileName;
}*/

}
