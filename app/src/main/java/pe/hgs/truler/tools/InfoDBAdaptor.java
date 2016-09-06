package pe.hgs.truler.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/** 데이터베이스 연결 객체
 * Created by ysb06 on 2016-08-14.
 */
public class InfoDBAdaptor extends SQLiteOpenHelper {

	//데이터베이스 속성
	public static final String DB_NAME = "ProfileDB";
	public static final int DB_VERSION = 1;

	//테이블 속성


	public InfoDBAdaptor(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

	}
}
