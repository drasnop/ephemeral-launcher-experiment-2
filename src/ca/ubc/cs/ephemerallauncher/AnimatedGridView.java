package ca.ubc.cs.ephemerallauncher;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import ca.ubc.cs.ephemerallauncherexperiment.R;
import ca.ubc.cs.ephemerallauncherexperiment.State;

/* A custom GridView that supports changes/fadesIn of colored icons 
 */
public class AnimatedGridView extends GridView {

	private int[] highlightedIcons = new int[LauncherParameters.NUM_HIGHLIGHTED_ICONS];

	public AnimatedGridView(Context context) {
		super(context);
	}

	public AnimatedGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AnimatedGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// /////// Public methods

	public void init(final Context mContext) {

		this.setAdapter(new IconAdapter(mContext));

		this.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				iconClicked(position);
			}
		});

	}
	
	public void iconClicked(int position){
		long duration=System.currentTimeMillis()-State.startTime;
		Toast.makeText(this.getContext(), "duration = " + duration + " ms \n"
				+"page = "+ State.page+1 +"\n"
				+"position = "+ position+1, Toast.LENGTH_SHORT).show();
	}
	
	// ------ Public animation functions ------
	
	public void startPreAnimation() {

		for (int i = 0; i < highlightedIcons.length; i++)
			highlightedIcons[i] = -1;

		int position;
		int icon_nb = this.getChildCount();
		
		for (int i = 0; i < LauncherParameters.NUM_HIGHLIGHTED_ICONS; i++) {
			
			/*position = (int) Math.floor(Math.random() * icon_nb);
			while (!isDifferentFromAllHighlighted(position))
				position = (int) Math.floor(Math.random() * icon_nb);*/
			position = i;
			highlightedIcons[i] = position;
		} 
		
		

		if (LauncherParameters.ANIMATION_HAS_PREANIMATION_STATE)
			startPreAnimationAllIcons();
	}

	public void startEphemeralAnimation() {

		for (int i = 0; i < highlightedIcons.length; i++) {
			highlightIcon(highlightedIcons[i]);
		}

		if (LauncherParameters.ANIMATION_AFFECTS_OTHER_ICONS)
			animateOtherIcons();
	}

	public void backToPreAnimationState() {
		if (LauncherParameters.ANIMATION_HAS_PREANIMATION_STATE) {
			// TODO: stop the current animation! (in case they last 10s or so)
			for (int i = 0; i < this.getChildCount(); i++)
				this.getIcon(i).getImageGs().setVisibility(View.GONE);
		}
	}

	// ------ Private helper functions ------

	private Icon getIcon(int position) {
		return (Icon) this.getChildAt(position);
	}

	private void startPreAnimationAllIcons() {
		for (int i = 0; i < this.getChildCount(); i++)
			startPreAnimation(i);
	}

	private void startPreAnimation(int position) {
		
		switch (LauncherParameters.ANIMATION) {
		case COLOR:
		case BLUR:
			if(isDifferentFromAllHighlighted(position)){
				changeMaskImages();
				Effects.changeToGreyScale(this.getIcon(position));
			}
			break;
		case TRANSPARENCY:
			if (isDifferentFromAllHighlighted(position))
				Animation.disappear(this.getIcon(position));
			break;
		default:
			break;
		}
		
		// stupid function call to change the color of the captions
		changeTextColor();
	}
	
	private void highlightIcon(int position) {
		switch (LauncherParameters.ANIMATION) {
		case COLOR:
		case BLUR:
			// Animation.color(this.getIcon(position));    not any more
			break;
		case ZOOM_IN:
			Animation.zoom_in(this.getIcon(position));
			break;
		case ZOOM_OUT:
			Animation.zoom_out(this.getIcon(position));
			break;
		case PULSE_IN:
			Animation.pulse_in(this.getIcon(position));
			break;
		case PULSE_OUT:
			Animation.pulse_out(this.getIcon(position));
			break;
		case TWIST:
			Animation.twist(this.getIcon(position));
			break;
		default:
			break;
		}
	}

	private void animateOtherIcons() {
		for (int i = 0; i < this.getChildCount(); i++)
			animateOtherIcon(i);
	}

	private void animateOtherIcon(int position) {
		switch (LauncherParameters.ANIMATION) {
		case COLOR:
		case BLUR:
			Effects.changeToColor(this.getIcon(position), LauncherParameters.COLOR__FADE_IN_DURATION,
					LauncherParameters.COLOR__START_DELAY);
			break;
		case TRANSPARENCY:
			if (isDifferentFromAllHighlighted(position))
				Animation.fadeIn(this.getIcon(position));
		default:
			break;
		}
	}


	private void changeMaskImages() {
		for (int i = 0; i < this.getChildCount(); i++) {
			switch (LauncherParameters.ANIMATION) {
			case COLOR:
				this.getIcon(i).getImageGs().setImageResource(LauncherParameters.images_gs_ID[i]);
				break;
			case BLUR:
				this.getIcon(i).getImageGs().setImageResource(LauncherParameters.images_b_ID[i]);
				break;
			default:
				break;
			}
		}
	}
	
	// Stupid function required because we can't change the text color globally!
	void changeTextColor() {
		for (int i = 0; i < this.getChildCount(); i++) {
			switch (LauncherParameters.BACKGROUND) {
			case 0:
			case 1:
				this.getIcon(i).getCaption().setTextColor(getResources().getColor(R.color.white));
				break;
			case 2:
				this.getIcon(i).getCaption().setTextColor(getResources().getColor(R.color.darkgrey));
				break;
			default:
				break;
			}
		}
	}

	private boolean isDifferentFromAllHighlighted(int position) {
		for (int i = 0; i < highlightedIcons.length; i++) {
			if (position == highlightedIcons[i])
				return false;
		}
		return true;
	}
}
