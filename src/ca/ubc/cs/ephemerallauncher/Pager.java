package ca.ubc.cs.ephemerallauncher;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import ca.ubc.cs.ephemerallauncherexperiment.Distributions;
import ca.ubc.cs.ephemerallauncherexperiment.EndOfCondition;
import ca.ubc.cs.ephemerallauncherexperiment.EndOfExperiment;
import ca.ubc.cs.ephemerallauncherexperiment.ExperimentParameters;
import ca.ubc.cs.ephemerallauncherexperiment.FileManager;
import ca.ubc.cs.ephemerallauncherexperiment.R;
import ca.ubc.cs.ephemerallauncherexperiment.State;
import ca.ubc.cs.ephemerallauncherexperiment.Trial;
import ca.ubc.cs.ephemerallauncherexperiment.TrialIncorrectSelection;
import ca.ubc.cs.ephemerallauncherexperiment.TrialTimeout;
import ca.ubc.cs.ephemerallauncherexperiment.Utils;

public class Pager extends FragmentActivity{

	PagerAdapter pagerAdapter;
	ViewPager pager;
	long startTime;
	
	private int mInterval = ExperimentParameters.TIMEOUT_CHECK_INTERVAL; 
	private Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.pager);
		State.page=1;
		setUpPager();		
		
		mHandler = new Handler();
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		State.startTime = System.currentTimeMillis();
		State.timeout = false;
		State.missed = false;
		mTimeoutChecker.run();
		
		logEvent("TrialStarted","");
	}
	
	protected void onStop(){
		super.onStop();
		mHandler.removeCallbacks(mTimeoutChecker);
	}
	
	private void checkForTimeout(){
		if (System.currentTimeMillis() - State.startTime > ExperimentParameters.TRIAL_TIMEOUT_MS){
			State.timeout = true;
			concludeTrial(-1,-1);			
		}
	}
    
	Runnable mTimeoutChecker = new Runnable() {
	    @Override 
	    public void run() {
	      checkForTimeout(); //this function can change value of mInterval.
	      mHandler.postDelayed(mTimeoutChecker, mInterval);
	    }
	  };

	
	/*void stopRepeatingTask() {
	   mHandler.removeCallbacks(mTimeoutChecker);
	}*/
	  
	
	private void logEvent(String eventName, String eventDescription){
		String eventLog = Utils.appendWithComma(Utils.getTimeStamp(false), State.stateCsvLog(), eventName, eventDescription);
		
		FileManager.appendLineToFile(State.currentEventsLogFile, eventLog);
	}
	  
	private String resultCsvLog(boolean ifHighlighted, long duration, int row, int column, boolean ifSuccess, boolean ifTimeout, boolean ifMissed){
		String highlightedStr = ifHighlighted? "Highlighted" : "Normal"; 
		String successStr = ifSuccess? "Success" : "Failure";
		String timeoutStr = ifTimeout? "Timeout" : "InTime";
		String missedStr = ifMissed? "Miss" : "Catch";
		String log = Utils.appendWithComma(highlightedStr, String.valueOf(duration), String.valueOf(State.page),String.valueOf(row), String.valueOf(column), successStr, timeoutStr, missedStr);
		
		return log;
	}
	
	private void logTrial(boolean ifHighlighted, long duration, int row, int column,  boolean ifSuccess, boolean ifTimeout, boolean ifMissed){
		
		String finalTrialLog = Utils.appendWithComma(Utils.getTimeStamp(false), State.stateCsvLog(), resultCsvLog(ifHighlighted, duration, row, column,  ifSuccess,  ifTimeout, ifMissed));
		
		FileManager.appendLineToFile(State.currentTrialsLogFile,finalTrialLog);
	}
	
	// AP: it would be great if we could have this function in Trial instead...
	// KA: do you mean GridView? Because Trial is another activity.
	// KA: anyway, there are various reasons why I moved this here, the main one being that the highlight
	// checker had to be implemented here. Logically this function belongs to here rather than to a GridView
	// because GridView is just a part of a complete trial
	public void concludeTrial(int page, int position_on_page){
		
		int global_position = page*LauncherParameters.NUM_ICONS_PER_PAGE+position_on_page;
		boolean success = (Distributions.targets[State.trial] == global_position);
		boolean ifHighlighted = isHighlighted(global_position);
		Log.v("Pager", ifHighlighted? "Highlighted" : "Normal");
		long duration = System.currentTimeMillis()-State.startTime;
		
		int row=(int) Math.floor(position_on_page/4)+1;
		int column=(position_on_page-1)%4+1;
		
		// If finished due to timeout
		if (position_on_page == -1) {
			row = 0;
			column = 0;
		}
				
		logTrial(ifHighlighted, duration, row, column, success, State.timeout, State.missed);		
		
		if (State.timeout){
				Intent intent = new Intent(this, TrialTimeout.class);
				this.startActivity(intent);
		}
		else if (!success){
			Intent intent = new Intent(this, TrialIncorrectSelection.class);
			this.startActivity(intent);
		}
		else {
			startNextTrial();
		}
	}
	
	private void startNextTrial(){
		State.trial++;
		if(State.trial>ExperimentParameters.NUM_TRIALS){
			// end condition
			
			State.block++;
			
			State.trial=1;
			
			if (State.block == ExperimentParameters.NUM_CONDITIONS)
			{
				Intent intent = new Intent(this, EndOfExperiment.class);
				this.startActivity(intent);
			}
			else {
				
				Intent intent = new Intent(this, EndOfCondition.class);
				this.startActivity(intent);
			}
		}	
		else{
			Intent intent = new Intent(this, Trial.class);
			this.startActivity(intent);
		}
		
	}
	
	private boolean isHighlighted(int position){
		for(Integer icon:Distributions.highlighted[State.trial]){
			if(position == icon)
				return true;
		}
		return false;
	}
	
	private void setUpPager(){
		pager = (ViewPager) findViewById(R.id.pager);

		// Create grid views for each page
		ArrayList<Page> pages = new ArrayList<Page>();
		for (int i = 0; i < LauncherParameters.NUM_PAGES; i++) {
			pages.add(new Page(this,i));
		}
		
		  
		// Populate pager
		pagerAdapter = new PagerAdapter(getSupportFragmentManager(), pages);
		pager.setAdapter(pagerAdapter);

		// Set up animations when changing page
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			public void onPageScrollStateChanged(int state) {

				if (state == ViewPager.SCROLL_STATE_DRAGGING) {
					
					// don't know in which direction we're going! so we do both
					int position = pagerAdapter.currentPosition;
					
					if (position - 1 >= 0)
						pagerAdapter.getPage(position - 1).getGridView().startPreAnimation();
					if (position + 1 < LauncherParameters.NUM_PAGES)
						pagerAdapter.getPage(position + 1).getGridView().startPreAnimation(); 
				}
			}

			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				
			}

			public void onPageSelected(int position) {
				State.page=position+1;
				
				pagerAdapter.previousPosition=pagerAdapter.currentPosition;
				pagerAdapter.currentPosition = position;
				
				Animation.clearAll();
				
				logEvent("NewPage", "");
				
				//checking for misses
				if (State.page > State.targetIconPage) {
					State.missed = true;
				}
				
				pagerAdapter.getCurrentPage().getGridView().startEphemeralAnimation();
				pagerAdapter.getPreviousPage().getGridView().backToPreAnimationState();
			}

		});

		// Switch to initial animation type when the layout has been created, then plays it
		pager.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {		
			boolean firstTime=true;		// This is a workaround, but seems to work pretty well...
			public void onGlobalLayout() {
				if(firstTime){
					LauncherParameters.switchAnimationTo(LauncherParameters.ANIMATION, pagerAdapter);
					firstTime=false;
				}
			}
		});

		// Parameters.switchAnimationTo(Parameters.ANIMATION,pagerAdapter);		// CRASHES !!!		
		
	}
	@Override //KZ
	public void onBackPressed(){}

}
