package ca.ubc.cs.ephemerallauncherexperiment;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
	
		
	public static void openFile(Context c, String folderName, String fileName) {
		ExperimentParameters.state.currentTrialsLogFile = getFile(c, folderName, fileName);
	}
	
	public static File getFile(Context c, String folderName, String fileName) {
        Log.v("FileManager","getFile "+fileName);
		return new File(getExtStorageDir(c, folderName),fileName);
    }

	public static void appendLineToFile(File file, String content){
		writeLineToFile(file, content, true);
	}
	public static void appendToFile(String content) {
		appendToFile(ExperimentParameters.state.currentTrialsLogFile, content);
	}
	public static void appendToFile(File file, String content) {
		writeToFile(file, content, true);
	}
	public static void writeLineToFile(File file, String content, boolean ifAppendData){
		content = "\n" + content;
		writeToFile(file, content, ifAppendData);
	}
	public static void writeLineToFile(String content, boolean ifAppendData){
		content = "\n" + content;
		writeToFile(content, ifAppendData);
	}
	public static void writeToFile(String content, boolean ifAppendData) {
		writeToFile(ExperimentParameters.state.currentTrialsLogFile, content, ifAppendData);
	}
	public static void writeToFile(File file, String content, boolean ifAppendData) {
		
		try {
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), ifAppendData);
			BufferedWriter bw = new BufferedWriter(fw);	
			bw.write(content);
			bw.flush();
			bw.close(); }
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public static void testSavingToSdCard(Context c) {
		String fileName = "file_test";
		String folderName = "dir_test";
		
		openFile(c, folderName, fileName);
		
		appendToFile(c.getString(R.string.file_test));
	}
	
	public static File getExtStorageDir(Context context, String dirName) {
	     
	    File file = new File(context.getExternalFilesDir(
	            "EXPERIMENT_DATA"), dirName);
	    if (!file.mkdirs()) {
	        // Log.e("LOG", "Directory not created");
	    }
	    return file;
	}
	
	
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    return Environment.MEDIA_MOUNTED.equals(state);
	}
}
