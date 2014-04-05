package ca.ubc.cs.ephemerallauncher;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import ca.ubc.cs.ephemerallauncherexperiment.Condition;
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
		mTimeoutChecker.run();
	}
	
	protected void onStop(){
		super.onStop();
		mHandler.removeCallbacks(mTimeoutChecker);
	}
	
	private void checkForTimeout(){
		if (System.currentTimeMillis() - State.startTime > ExperimentParameters.TRIAL_TIMEOUT_MS){
			State.timeout = true;
			concludeTrial(-1);
			
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
	  
	private String resultCsvLog(long duration, int row, int column, boolean ifSuccess, boolean ifTimeout){
		String successStr = ifSuccess? "Success" : "Failure";
		String timeoutStr = ifTimeout? "Timeout" : "InTime";
		String log = Utils.appendWithComma(String.valueOf(duration), String.valueOf(State.page),String.valueOf(row), String.valueOf(column), successStr, timeoutStr);
		return log;
		}
	
	private void logTrial(long duration, int row, int column, boolean ifSuccess, boolean ifTimeout){
		
		String finalTrialLog = Utils.appendWithComma(Utils.getTimeStamp(false), State.stateCsvLog(), resultCsvLog(duration, row, column, ifSuccess,  ifTimeout));
		
//		Toast.makeText(this, finalTrialLog, Toast.LENGTH_SHORT).show();
		
		FileManager.appendLineToFile(finalTrialLog);
	}
	  
	public void concludeTrial(int position){
		boolean success;
		
		long duration=System.currentTimeMillis()-State.startTime;
		
		int row=(int) Math.floor(position/4)+1;
		int column=position%4+1;
		
		//finished due to timeout
		if (position == -1) {
			row = 0;
			column = 0;
		}
		
		//TODO: check success and update ifSuccess
		success = true;//just for test
		
		
		logTrial(duration, row, column, success, State.timeout);		
		
/*		Toast.makeText(this, "trial = "+State.trial+"\n"
				+"duration = " + duration + " ms \n"
				+"page = "+ State.page +"\n"
				+"position = "+ row+","+column, Toast.LENGTH_SHORT).show();*/
		
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
			State.condition = State.listOfConditions.get(State.block);
			
			State.trial=1;
			
			if (State.block == ExperimentParameters.NUM_CONDITIONS)
			{
				Intent intent = new Intent(this, EndOfExperiment.class);
				this.startActivity(intent);
			}
			else {
				
				Intent intent = new Intent(this, Condition.class);
				this.startActivity(intent);
			}
		}	
		else{
			Intent intent = new Intent(this, Trial.class);
			this.startActivity(intent);
		}
		
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

}
