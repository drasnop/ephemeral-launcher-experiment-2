package ca.ubc.cs.ephemerallauncherexperiment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Utils {
	public static String appendWithComma(String... strings) {
		String temp = "";
		for (int i = 0; i < strings.length - 1; i++) {
			temp = temp + strings[i] + ", ";
		}

		temp = temp + strings[strings.length - 1];

		return temp;
	}

	public static String getTimeStamp(boolean forFile) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf;

		if (forFile) {
			sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
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
}
