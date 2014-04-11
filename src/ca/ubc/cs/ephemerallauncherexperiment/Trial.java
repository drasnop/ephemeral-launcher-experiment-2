package ca.ubc.cs.ephemerallauncherexperiment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import ca.ubc.cs.ephemerallauncher.LauncherParameters;

public class Trial extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trial);

		ImageView target_icon = (ImageView) this.findViewById(R.id.target_icon);
		target_icon.setImageResource(State.current_images_ID[Distributions.targets[State.trial]]);
		
		initializeTrial();
		
		RelativeLayout trial = (RelativeLayout) this.findViewById(R.id.trial);
		trial.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startPager();
			}
		});
	}
	
	private void initializeTrial(){
		int targetIconPosition = Distributions.targets[State.trial];
		int targetIconPosOnPage = targetIconPosition % LauncherParameters.NUM_ICONS_PER_PAGE + 1;
		State.targetIconPage = (int)Math.floor(targetIconPosition / LauncherParameters.NUM_ICONS_PER_PAGE) +1; 
		State.targetIconRow = (int)Math.floor(targetIconPosOnPage / 4) + 1 ;
		State.targetIconColumn = (targetIconPosOnPage-1)%4+1; 
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trial, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void startPager(){
		Intent intent = new Intent(this, ca.ubc.cs.ephemerallauncher.Pager.class);
		startActivity(intent);
	}
	
	@Override //KZ
	public void onBackPressed(){}

}
