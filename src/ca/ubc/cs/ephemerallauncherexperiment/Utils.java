package ca.ubc.cs.ephemerallauncherexperiment;

public class Utils {
	public static String appendWithComma(String... strings){
		String temp = "";
		for (int i=0; i < strings.length-1; i++){
			temp = temp + strings[i] + ", ";
		}
		
		temp = temp + strings[strings.length-1];
		
		return temp;
	}
}
