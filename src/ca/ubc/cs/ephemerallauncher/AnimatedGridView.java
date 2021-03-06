package ca.ubc.cs.ephemerallauncher;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import ca.ubc.cs.ephemerallauncherexperiment.ExperimentParameters;

import java.util.ArrayList;

/* A custom GridView that supports changes/fadesIn of colored icons 
 */
public class AnimatedGridView extends GridView {

	private ArrayList<Integer> highlightedIcons = new ArrayList<Integer>();
	private int page_number;

	public AnimatedGridView(Context context) {
		super(context);
	}

	public AnimatedGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AnimatedGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// ------- Public methods -------

	public void init(final Context mContext, int page_number) {
		
		this.setAdapter(new IconAdapter(mContext, page_number));

		this.page_number=page_number;
		
		// Define highlighted icons for this trial
		int position;			// from 1 to NUM_PAGES*NUM_ICONS_PER_PAGE
		int position_on_page;	// from 0 to NUM_ICONS_PER_PAGE-1
		String log="icons: ";
		for(int i=0; i< ExperimentParameters.state.num_highlighted_icons(); i++){
			position=ExperimentParameters.distributions.highlighted[ExperimentParameters.state.trial][i];
			log=log+position;
			if(this.page_number == (position-1)/LauncherParameters.NUM_ICONS_PER_PAGE){
				position_on_page=(position-1)%LauncherParameters.NUM_ICONS_PER_PAGE;
				highlightedIcons.add(position_on_page);
				log+=".";
			}
			log+=" ";
		}
		
		this.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				iconClicked(position);
			}
		});

	}
	
	// This ((Pager) this.getContext()) is great, Kamyar!
	public void iconClicked(int position){
		((Pager) this.getContext()).concludeTrial(page_number,position);
	}
	
	// ------ Public animation functions ------
	
	public void startPreAnimation() {
		if (LauncherParameters.ANIMATION_HAS_PREANIMATION_STATE)
			startPreAnimationAllIcons();
	}

	public void startEphemeralAnimation() {

		for(Integer icon:highlightedIcons)
			highlightIcon(icon);

		if (LauncherParameters.ANIMATION_AFFECTS_OTHER_ICONS)
			animateOtherIcons();
	}

	public void backToPreAnimationState() {
		if (LauncherParameters.ANIMATION_HAS_PREANIMATION_STATE) {
			// TODO: stop the current animation! (in case they last 10s or so)
			// Animation.clearAll() could be used in this case.
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
				Effects.changeToGreyScale(this.getIcon(position));
			}
			break;
		case TRANSPARENCY:
			if (isDifferentFromAllHighlighted(position))
				Animation.changeToTransparent(this.getIcon(position));
			break;
		default:
			break;
		}
	}
	
	private void highlightIcon(int position) {
		switch (LauncherParameters.ANIMATION) {
		case COLOR:
		case BLUR:
			Animation.color(this.getIcon(position));
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

	private boolean isDifferentFromAllHighlighted(int position) {
		for(Integer icon:highlightedIcons){
			if(icon == position)
				return false;
		}
		return true;
	}
}
