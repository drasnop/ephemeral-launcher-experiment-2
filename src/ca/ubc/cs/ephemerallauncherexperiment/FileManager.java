package ca.ubc.cs.ephemerallauncherexperiment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileManager {
	
	public static void testSavingToSdCard(Context c) {
		String filename = "file1";
		boolean ifAppendData = true;
		File file = new File(getExtStorageDir(c, "dir1"),filename);
		
		try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), ifAppendData);
			BufferedWriter bw = new BufferedWriter(fw);	
			bw.write(R.string.file_test);
			bw.flush();
			bw.close(); }
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public static File getExtStorageDir(Context context, String dirName) {
	     
	    File file = new File(context.getExternalFilesDir(
	            "EXPERIMENT_DATA"), dirName);
	    if (!file.mkdirs()) {
	        Log.e("LOG", "Directory not created");
	    }
	    return file;
	}
	
	
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}
}
