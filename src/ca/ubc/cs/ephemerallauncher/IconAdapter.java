package ca.ubc.cs.ephemerallauncher;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import ca.ubc.cs.ephemerallauncherexperiment.Distributions;
import ca.ubc.cs.ephemerallauncherexperiment.R;
import ca.ubc.cs.ephemerallauncherexperiment.State;

public class IconAdapter extends BaseAdapter {
	private Context mContext;
	private int page_number;

	public IconAdapter(Context c, int page_number) {
		mContext = c;
		this.page_number=page_number;
	}

	public int getCount() {
		return LauncherParameters.NUM_ICONS_PER_PAGE;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new View for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		Icon icon;
		int global_position=page_number*LauncherParameters.NUM_ICONS_PER_PAGE+position+1;	// 1..60

		if(global_position> State.num_positions()){
			Log.e("Adapter", "We should never get this global position: "+Integer.toString(global_position) + " " + Integer.toString(position));
			global_position= State.num_positions();
		}
		
		if (convertView == null) {
			// if it's not recycled, initialize the view
			// [AP: I don't really understand this test...]
			// [AP: Contrary to the doc, this doesn't return parent...]
			icon=(Icon) LayoutInflater.from(mContext).inflate(R.layout.icon, parent,false);
			
			// Set up the colored image
			icon.getImage().setImageResource(Distributions.images_ID[global_position]);

			// Set up the greyscale image
			// icon.getImageGs().setImageResource(Distributions.images_gs_ID[global_position]);
			// icon.getImageGs().setVisibility(ViewGroup.GONE);
			
			// The caption
			icon.getCaption().setText(Distributions.labels_ID[global_position]);
			
		} else {
			// [AP: I haven't quite figured out when this happens...
			icon = (Icon) convertView;
		}


		return icon;
	}
}