package ca.ubc.cs.ephemerallauncherexperiment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {
	public static String appendWithComma(String... strings){
		String temp = "";
		for (int i=0; i < strings.length-1; i++){
			temp = temp + strings[i] + ", ";
		}
		
		temp = temp + strings[strings.length-1];
		
		return temp;
	}
	
	
	public static String getTimeStamp(boolean forFile){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf;
		
		if (forFile) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss"); 
		}
		else {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
		
		return sdf.format(cal.getTime()); 
	}
}
