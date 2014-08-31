package ca.ubc.cs.ephemerallauncherexperiment;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import ca.ubc.cs.ephemerallauncher.Effects;

public class Trial extends Activity {

	@SuppressWarnings("unused")
	private Animator messageAnimator;
	
	@Override	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trial);

		ImageView target_icon = (ImageView) this.findViewById(R.id.target_icon);
		target_icon.setImageResource(State.current_images_ID[Distributions.targets[State.trial]]);
		
		TextView target_icon_label = (TextView) this.findViewById(R.id.target_icon_label);
		target_icon_label.setText(State.current_labels_ID[Distributions.targets[State.trial]]);
		
		
		initializeTrial();
		
		Intent intent = getIntent();
		String message = intent.getStringExtra(ExperimentParameters.SUCCESS_MESSAGE);
		
		//processing the message
		TextView message_text = (TextView) this.findViewById(R.id.message_text_view);
		
		if (message.equals("Success")){
			message_text.setText(this.getString(R.string.trial_success_message));
			message_text.setTextColor(ExperimentParameters.SUCCESS_MESSAGE_COLOR);
			message_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, ExperimentParameters.SUCCESS_MESSAGE_SIZE);
			messageAnimator = Effects.animateObjectProperty(message_text, "alpha", ExperimentParameters.SUCCESS_MESSAGE_DURATION_MS, ExperimentParameters.SUCCESS_MESSAGE_DELAY_MS, 1f, 0f);
		}
		else if (message.equals("Failure")){
			message_text.setText(this.getString(R.string.trial_failure_message));
			message_text.setTextColor(ExperimentParameters.FAILURE_MESSAGE_COLOR);
			message_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, ExperimentParameters.FAILURE_MESSAGE_SIZE);
			messageAnimator = Effects.animateObjectProperty(message_text, "alpha", ExperimentParameters.FAILURE_MESSAGE_DURATION_MS, ExperimentParameters.FAILURE_MESSAGE_DELAY_MS, 1f, 0f);
		}
		else if (message.equals("Timeout")){
			//message_text.setText(this.getString(R.string.trial_timeout_message));
			message_text.setText("");
			
		}
		else if (message.equals("None")){
			message_text.setText(this.getString(R.string.trial_none_message));
		}
		
		
		
		RelativeLayout trial = (RelativeLayout) this.findViewById(R.id.trial);
		trial.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startPager();
			}
		});
	}
	
	private void initializeTrial(){
		int targetIconPosition = Distributions.targets[State.trial]; //starts from 1
		int targetIconPosOnPage = (targetIconPosition-1) % ExperimentParameters.NUM_ICONS_PER_PAGE + 1;
		State.targetIconPage = (int)Math.floor((targetIconPosition-1) / ExperimentParameters.NUM_ICONS_PER_PAGE) +1;
		State.targetIconRow = (int)Math.floor((targetIconPosOnPage-1) / 4) + 1 ;
		State.targetIconColumn = (targetIconPosOnPage-1)%4+1; 
		State.timeout=false;
		State.missed=false;
		State.success=false;
		
	}
	
	private void startPager(){
		Intent intent = new Intent(this, ca.ubc.cs.ephemerallauncher.Pager.class);
		startActivity(intent);
	}
	
	@Override //KZ
	public void onBackPressed(){}

}
