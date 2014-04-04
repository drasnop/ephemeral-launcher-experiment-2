package ca.ubc.cs.ephemerallauncher;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import ca.ubc.cs.ephemerallauncherexperiment.R;
import ca.ubc.cs.ephemerallauncherexperiment.State;

public class Pager extends FragmentActivity{

	PagerAdapter pagerAdapter;
	ViewPager pager;
	long startTime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.pager);
		setUpPager();
		
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		State.startTime = System.currentTimeMillis();
	}
	
	private void setUpPager(){
		pager = (ViewPager) findViewById(R.id.pager);

		// Create grid views for each page
		ArrayList<Page> pages = new ArrayList<Page>();
		for (int i = 0; i < LauncherParameters.NUM_PAGES; i++) {
			pages.add(new Page(this));
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
				pagerAdapter.previousPosition=pagerAdapter.currentPosition;
				pagerAdapter.currentPosition = position;
				
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

		// Parameters.switchAnimationTo(Parameters.ANIMATION,pagerAdapter);		// CRASHES
	}
	

}
