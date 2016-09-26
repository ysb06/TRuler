package pe.hgs.truler.tools;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ysb06 on 2016-08-07.
 */
public class Logger {

	private static String sHistory = "-- Logs --\n\n";

	/** 디버그 수준으로 로그 출력
	 *
	 * @param msg 출력할 메시지
	 */
	public static void debug(String msg) {
		if(msg.length() != 0) {
			Log.d("CM_Message", msg);
			log("Msg: "+ msg);
		} else {
			Log.w("CM_Warning", "(No Value)");
			log("Warn: (No Value)");
		}
	}

	/** 디버그 수준으로 로그 출력
	 *
	 * @param msg 출력할 메시지
	 */
	public static void debug(Class caller, String msg) {
		if(msg.length() != 0) {
			Log.d("CM_Message(" + caller.getName() + ")", msg);
			log("Msg(" + caller.getName() + "): " + msg);
		} else {
			Log.w("CM_Warning(" + caller.getName() + ")", "(No Value)");
			log("Warn(" + caller.getName() + "): (No Value)");
		}
	}

	/** 주의 수준으로 로그 출력
	 *
	 * @param msg 출력할 메시지
	 */
	public static void warn(String msg) {
		if(msg.length() != 0) {
			Log.w("CM_Warning", msg);
			log("Warn: "+ msg);
		} else {
			Log.w("CM_Warning", "(No Value)");
			log("Warn: (No Value)");
		}
	}

	/** 주의 수준으로 로그 출력
	 *
	 * @param msg 출력할 메시지
	 */
	public static void warn(Class caller, String msg) {
		if(msg.length() != 0) {
			Log.w("CM_Warning(" + caller.getName() + ")", msg);
			log("Warn(" + caller.getName() + "): " + msg);
		} else {
			Log.w("CM_Warning(" + caller.getName() + ")", "(No Value)");
			log("Warn(" + caller.getName() + "): (No Value)");
		}
	}

	/** 에러 수준으로 로그 출력
	 *
	 * @param msg 출력할 메시지
	 */
	public static void error(String msg) {
		if(msg.length() != 0) {
			Log.e("CM_Error", msg);
			log("Error: "+ msg);
		} else {
			Log.e("CM_Error", "(No Value)");
			log("Error: (No Value)");
		}
	}

	/** 에러 수준으로 로그 출력
	 *
	 * @param msg 출력할 메시지
	 */
	public static void error(Class caller, String msg) {
		if(msg.length() != 0) {
			Log.e("CM_Error(" + caller.getName() + ")", msg);
			log("Error(" + caller.getName() + "): " + msg);
		} else {
			Log.e("CM_Error(" + caller.getName() + ")", "(No Value)");
			log("Error(" + caller.getName() + "): (No Value)");
		}
	}

	public static String getLogs() {
		return sHistory;
	}

	private static void log(String str) {
		sHistory += str + "\n";
	}
}
