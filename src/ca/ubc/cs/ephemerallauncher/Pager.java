package ca.ubc.cs.ephemerallauncher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import ca.ubc.cs.ephemerallauncherexperiment.*;

import java.util.ArrayList;

public class Pager extends FragmentActivity{

    PagerAdapter pagerAdapter;
    ViewPager pager;

    private int mInterval = ExperimentParameters.TIMEOUT_CHECK_INTERVAL;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pager);
        setUpPager();

        mHandler = new Handler();
    }

    @Override
    protected void onStart(){
        super.onStart();
        // We wait until the last minute to record the starting time
        Logging.startTime = System.currentTimeMillis();
        Logging.previousPageLandingTime = Logging.startTime;
        Logging.pages_times = "";
        mTimeoutChecker.run();

        logEvent("TrialStarted","");
    }

    protected void onStop(){
        super.onStop();
        mHandler.removeCallbacks(mTimeoutChecker);
    }

    private void checkForTimeout(){
        if (System.currentTimeMillis() - Logging.startTime > ExperimentParameters.TRIAL_TIMEOUT_MS && !State.timeout){
            State.timeout = true;
            mHandler.removeCallbacks(mTimeoutChecker);
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
        String eventLog = Utils.appendWithComma(Utils.getTimeStamp(false), Logging.stateCsvLog(this), eventName, eventDescription);

        FileManager.appendLineToFile(Logging.currentEventsLogFile, eventLog);
    }

    // AP: it would be great if we could have this function in Trial instead...
    // KA: do you mean GridView? Because Trial is another activity.
    // KA: anyway, there are various reasons why I moved this here, the main one being that the highlight
    // checker had to be implemented here. Logically this function belongs to here rather than to a GridView
    // because GridView is just a part of a complete trial
    // AP: No, I meant Trial, because it makes more sense to me (semantically)
    // but you are right, Trial would correspond to the next trial...
    // What about making these log functions static, to put them in Trial?
    // The only dynamic thing remaining in Pager would be startNextTrial
    // KA: this possible
    // Maybe: move every function corresponding to the general concept of Trial to Trial.java
    public void concludeTrial(int page, int position_on_page){

        int global_position = page*LauncherParameters.NUM_ICONS_PER_PAGE+position_on_page+1;
        Distributions.selected[State.trial]=global_position;

        State.success = (Distributions.targets[State.trial] == global_position);

        if(!State.success){
            Utils.vibrate(this);

            // update the MRU of the next trial
            // TODO check if this is still necessary when highlighting randomly
            if(!State.timeout && State.trial<ExperimentParameters.NUM_TRIALS)
                Distributions.highlightIconAtTrial(global_position, State.trial+1);
        }

        mHandler.removeCallbacks(mTimeoutChecker);
        logEvent("ConcludingTrial", "");
        long duration = System.currentTimeMillis()- Logging.startTime;

        int row=(int) Math.floor(position_on_page/4)+1;
        int column=(position_on_page)%4+1;

        String iconName;
        String iconLabel;

        // If finished due to timeout
        if (position_on_page == -1) {
            row = 0;
            column = 0;
            iconName = "None";
            iconLabel = "None";
        }
        else
        {
            iconName = Utils.extractIconName(this.getString(Distributions.images_ID[global_position]), ExperimentParameters.ICON_RESOURCE_ADDRESS_PREFIX);
            iconLabel = this.getString(Distributions.labels_ID[global_position]);

        }


        Logging.logTrial(this, new Result(duration, row, column, iconName, iconLabel));

        if (State.timeout){
            logEvent("Timeout", "");
            Intent intent = new Intent(this, TrialTimeout.class);
            this.startActivity(intent);
        }
        else if (!State.success){
            logEvent("Failure","");
            startNextTrial("Failure");
        }
        else {
            logEvent("Success","");
            startNextTrial("Success");
        }
    }

    private void startNextTrial(String message){
        State.trial++;
        if(State.trial>ExperimentParameters.NUM_TRIALS){
            // end condition

            State.block++;

            State.trial=1;

            if (State.block == ExperimentParameters.NUM_CONDITIONS)
            {
                Intent intent = new Intent(this, EndOfExperiment.class);
                intent.putExtra(ExperimentParameters.SUCCESS_MESSAGE, message);
                this.startActivity(intent);
            }
            else {

                Intent intent = new Intent(this, EndOfCondition.class);
                intent.putExtra(ExperimentParameters.SUCCESS_MESSAGE, message);
                this.startActivity(intent);
            }
        }
        else{
            Intent intent = new Intent(this, Trial.class);
            intent.putExtra(ExperimentParameters.SUCCESS_MESSAGE, message);
            this.startActivity(intent);
        }

    }

    private void setUpPager(){
        pager = (ViewPager) findViewById(R.id.pager);

        // Create grid views for each page
        ArrayList<Page> pages = new ArrayList<Page>();
        for (int i = 0; i < State.num_pages; i++) {
            pages.add(new Page(this,i));
        }


        // Populate pager
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), pages);
        pager.setAdapter(pagerAdapter);

        // Set up animations when changing page
        pager.setOnPageChangeListener(new OnPageChangeListener() {

            public void onPageScrollStateChanged(int state) {

                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    logEvent("DragStart", "");
                    // don't know in which direction we're going! so we do both
                    int position = pagerAdapter.currentPosition;

                    if (position - 1 >= 0)
                        pagerAdapter.getPage(position - 1).getGridView().startPreAnimation();
                    if (position + 1 < State.num_pages)
                        pagerAdapter.getPage(position + 1).getGridView().startPreAnimation();

                    Logging.previousPageStartDragging=System.currentTimeMillis();
                }
                if (state ==ViewPager.SCROLL_STATE_IDLE){
                    logEvent("PageIDLE","");
                }
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            public void onPageSelected(int position) {
                // log
                long current_time=System.currentTimeMillis();
                long time_spent_swiping=current_time-Logging.previousPageStartDragging;
                long time_spent_on_page=Logging.previousPageStartDragging-Logging.previousPageLandingTime;

                Logging.pages_times += "("+State.page + "," + time_spent_on_page +"," + time_spent_swiping + "), ";

                // update
                State.page=position+1;
                State.num_pages_visited++;
                Logging.previousPageLandingTime=current_time;

                pagerAdapter.previousPosition=pagerAdapter.currentPosition;
                pagerAdapter.currentPosition = position;

                //Animation.clearAll();

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
            public void onGlobalLayout() {
                LauncherParameters.switchAnimationTo(LauncherParameters.ANIMATION, pagerAdapter);

                // unregister listener (this is important)
                pager.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        // Parameters.switchAnimationTo(Parameters.ANIMATION,pagerAdapter);		// CRASHES !!!

    }
    @Override //KZ
    public void onBackPressed(){}

}
