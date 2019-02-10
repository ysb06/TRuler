package pe.hgs.truler.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ysb06 on 2017-07-03.
 */

public class DateLoader {

	public static String getToday() {

		long now = System.currentTimeMillis();
		Date date = new Date(now);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		return sdf.format(date);
	}
}
