package ca.ubc.cs.ephemerallauncher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ca.ubc.cs.ephemerallauncherexperiment.R;


/* One page of the "launcher", containing exactly one gridview of icons
*  This class calls the animations appropriate for each event (e.g, swipe)
*/
@SuppressLint("ValidFragment")
public class Page extends Fragment {

	private Context mContext;
	private AnimatedGridView gridview;
	private int page_number;

	public Page(){};
	
	public Page(Context c){
		mContext=c;
	}
	
	public Page(Context c, int page_number){
		mContext=c;
		this.page_number=page_number;
	}
	
	public AnimatedGridView getGridView(){
		return gridview;
	}
	
	public int getPageNumber(){
		return page_number;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		// Inflate the grid layout for this fragment, then populates it with icons	
		
		this.gridview = (AnimatedGridView) inflater.inflate(R.layout.animated_grid, container, false);
		
		
		gridview.init(mContext, page_number);

		return gridview;
	}
	
}
