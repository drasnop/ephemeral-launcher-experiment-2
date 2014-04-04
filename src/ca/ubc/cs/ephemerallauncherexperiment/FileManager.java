package ca.ubc.cs.ephemerallauncherexperiment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileManager {
	
	private static File currentFile;
	
	public static void openFile(Context c, String folderName, String fileName) {
		currentFile = getFile(c, folderName, fileName);
	}
	
	private static File getFile(Context c, String folderName, String fileName) {
		return new File(getExtStorageDir(c, folderName),fileName);
	}
	
	public static void appendLineToFile(String content){
		content = "\n" + content;
		appendToFile(content);
	}
	
	public static void appendToFile(String content) {
		appendToFile(currentFile, content);
	}
	private static void appendToFile(File file, String content) {
		writeToFile(file, content, true);
	}
	
	
	public static void writeToFile(String content, boolean ifAppendData) {
		writeToFile(currentFile, content, ifAppendData);
	}
	private static void writeToFile(File file, String content, boolean ifAppendData) {
		
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
	
	private static File getExtStorageDir(Context context, String dirName) {
	     
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
