package ca.ubc.cs.ephemerallauncherexperiment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

public class EndOfCondition extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_end_of_condition);

        if(State.block==5) {
            TextView message_text = (TextView) this.findViewById(R.id.end_of_condition_message);
            message_text.setText("End of the first half of the Experiment.\nPlease fill up the questionnaire and take a break!");
            message_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, ExperimentParameters.FAILURE_MESSAGE_SIZE);
            message_text.setTextColor(this.getResources().getColor(R.color.lightblue));
        }
	}
	
	public void startNextCondition(View view){
		Intent intent = new Intent(this, Condition.class);
		startActivity(intent);
	}
}
