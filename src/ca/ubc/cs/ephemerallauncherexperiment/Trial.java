package ca.ubc.cs.ephemerallauncherexperiment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import ca.ubc.cs.ephemerallauncher.LauncherParameters;

public class Trial extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trial);

		ImageView target_icon = (ImageView) this.findViewById(R.id.target_icon);
		int a = LauncherParameters.images_ID[Distributions.targets[State.trial-1]];
		Log.v("Trial",a+"");
		target_icon.setImageResource(a);
		
		LinearLayout trial = (LinearLayout) this.findViewById(R.id.trial);
		trial.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startPager();
			}
		});
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
		Log.v("Trial",State.trial +"");
		Intent intent = new Intent(this, ca.ubc.cs.ephemerallauncher.Pager.class);
		startActivity(intent);
	}

}
