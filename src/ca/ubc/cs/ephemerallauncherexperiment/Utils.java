package ca.ubc.cs.ephemerallauncherexperiment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Vibrator;
import ca.ubc.cs.ephemerallauncher.LauncherParameters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Utils {
	public static String appendWithComma(String... strings) {
		String temp = "";
		for (int i = 0; i < strings.length - 1; i++) {
			temp = temp + strings[i] + ",";
		}

		temp = temp + strings[strings.length - 1];

		return temp;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getTimeStamp(boolean forFile) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf;

		if (forFile) {
			sdf = new SimpleDateFormat("yyyy-MM-dd__HH-mm-ss");
		} else {
			sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		}

		return sdf.format(cal.getTime());
	}

	// Use collections only because I'm lazy
	public static void shuffleArrayExceptZero(int[] array) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i < array.length; i++) {
			list.add(array[i]);
		}

		Collections.shuffle(list);

		for (int i = 1; i < array.length; i++) {
			array[i] = list.get(i-1);
		}
	}
	
	public static String extractIconName(String iconResourceAddress, String iconResourceAddressPrefix){
		int addressLength = iconResourceAddressPrefix.length();
		return iconResourceAddress.substring(addressLength);
	}
	
	public static LauncherParameters.AnimationType effectToAnimation(ExperimentParameters.EffectEnum condition){
		
		switch (condition){
		/*case GREYBLUR:
			return LauncherParameters.AnimationType.BLUR;
			
		case TRANSPARENCY:
			return LauncherParameters.AnimationType.TRANSPARENCY;*/
			
		case TWIST:
			return LauncherParameters.AnimationType.TWIST;
			
		case PULSEOUT:
			return LauncherParameters.AnimationType.PULSE_OUT;
			
		case CONTROL:
			return LauncherParameters.AnimationType.NONE;
			
		default:
			return LauncherParameters.AnimationType.NONE;
			
		}
	}

	public static void vibrate(Context context) {
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(ExperimentParameters.VIBRATION_DURATION_MS);
	}
	
	public static String padWithZero(int i){
		String res="";
		if(i<10)
			res+="0";
		res+=i;
		return res;
	}
}
