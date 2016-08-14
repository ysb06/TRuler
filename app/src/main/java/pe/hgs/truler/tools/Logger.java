package pe.hgs.truler.tools;

import android.util.Log;

/**
 * Created by ysb06 on 2016-08-07.
 */
public class Logger {

	/** 디버그 수준으로 로그 출력
	 *
	 * @param msg 출력할 메시지
	 */
	public static void debug(String msg) {
		Log.d("CM_MainActivity", msg);
	}

	/** 주의 수준으로 로그 출력
	 *
	 * @param msg 출력할 메시지
	 */
	public static void warn(String msg) {
		Log.w("CM_MainActivity", msg);
	}

	/** 에러 수준으로 로그 출력
	 *
	 * @param msg 출력할 메시지
	 */
	public static void error(String msg) {
		Log.e("CM_MainActivity", msg);
	}
}
