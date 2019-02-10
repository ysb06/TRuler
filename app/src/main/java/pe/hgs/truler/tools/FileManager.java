package pe.hgs.truler.tools;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by ysb06 on 2016-10-11.
 */

public class FileManager {

	private static final char[] ILLEGAL_NAME = {'<', '>', ':', '"', '/', '\\', '|', '?', '*'};

	public static String convertToValidName(String name) {
		String str = name;
		for(char c : ILLEGAL_NAME) {
			str = str.replace(c, '_');
		}
		return str;
	}

	public static boolean writeFile(String filename, String content) {
		Logger.debug("File saving requested : " + Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED));
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Logger.debug("Storage Founded");

			String sSDdir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Context/";
			File dir = new File(sSDdir);
			if(dir.mkdir()) {
				Logger.debug("Directory is generated");
			}

			File file = new File(sSDdir, convertToValidName(filename));
			try {
				if(file.exists()) {
					file.delete();
				}
				file.createNewFile();
				String path = file.getAbsolutePath();

				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "EUC-KR"));
				writer.write(content);
				writer.close();

				/*
				FileWriter writer = new FileWriter(file, true);
				writer.append(content);
				writer.flush();
				writer.close();
				//*/

				Logger.debug("Writing Complete... " + path);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		} else {
			Logger.debug("There is no storage");
			return false;
		}
	}

	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	public static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
	}
}
