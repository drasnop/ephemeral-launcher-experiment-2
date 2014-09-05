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
		target_icon.setImageResource(ExperimentParameters.distributions.images_ID[ExperimentParameters.distributions.targets[ExperimentParameters.state.trial]]);
		
		TextView target_icon_label = (TextView) this.findViewById(R.id.target_icon_label);
		target_icon_label.setText(ExperimentParameters.distributions.labels_ID[ExperimentParameters.distributions.targets[ExperimentParameters.state.trial]]);
		
		
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
        else if (message.equals("Practice")){
            message_text.setText(this.getString(R.string.trial_practice_message));
            message_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, ExperimentParameters.PRACTICE_MESSAGE_SIZE);
            message_text.setTextColor(this.getResources().getColor(R.color.lightblue));
        }
        else if (message.equals("Restored")){
            message_text.setText(this.getString(R.string.trial_restored_message));
            message_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, ExperimentParameters.PRACTICE_MESSAGE_SIZE);
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
        ExperimentParameters.state.timeout=false;
        ExperimentParameters.state.missed=false;
        ExperimentParameters.state.success=false;
        ExperimentParameters.state.page=1;
        ExperimentParameters.state.num_pages_visited=1;

		int targetIconPosition = ExperimentParameters.distributions.targets[ExperimentParameters.state.trial]; //starts from 1
		int targetIconPosOnPage = (targetIconPosition-1) % ExperimentParameters.NUM_ICONS_PER_PAGE + 1;
		ExperimentParameters.state.targetIconPage = (int)Math.floor((targetIconPosition-1) / ExperimentParameters.NUM_ICONS_PER_PAGE) +1;
		ExperimentParameters.state.targetIconRow = (int)Math.floor((targetIconPosOnPage-1) / 4) + 1 ;
		ExperimentParameters.state.targetIconColumn = (targetIconPosOnPage-1)%4+1;

        if(ExperimentParameters.state.trial==1){
            ExperimentParameters.distributions.unSwapIconsAfterPractice();
            ExperimentParameters.state.trial_timeout_ms = ExperimentParameters.REGULAR_TRIAL_TIMEOUT_MS;
        }
	}
	
	private void startPager(){
		Intent intent = new Intent(this, ca.ubc.cs.ephemerallauncher.Pager.class);
		startActivity(intent);
	}
	
	@Override //KZ
	public void onBackPressed(){}

}
