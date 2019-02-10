package pe.hgs.truler.tools.ergonomics;

import android.content.Context;
import pe.hgs.truler.tools.Logger;

/** 자세 관련 데이터베이스 접근 클래스
 * Created by ysb06 on 2016-11-16.
 */
public class ErgoDatabase {
	private static ErgoDatabase ourInstance = new ErgoDatabase();

	public static ErgoDatabase getInstance() {
		return ourInstance;
	}

	public static final String[] NAME_UPPER_POSTURE
			= {"B0-S0-E45", "B0-S0-E90", "B0-S45-E0", "B0-S45-E45", "B0-S45-E90",
			"B0-S90-E45", "B0-S90-E90", "B0-S120-E0", "B45-S45-E0", "B45-S45-E45",
			"B45-S90-E0", "B45-S90-E45", "B90-S90-E0", "B90-S90-E45"};
	public static final String[] NAME_LOWER_POSTURE
			= {"STD", "KF150", "KF120", "KF90", "KF60", "KF30", "KF30C", "KNL_1", "KNL_2", "SF_CRS", "SC0", "SC20", "SC40"};


	public PostureAnalyzer analyzer;        //// TODO: 2016-11-17 없애야 할 필요악이자 악의 축

	private ErgoDatabase() {
		analyzer = new PostureAnalyzer();
	}

	/** 상지+하지 모두 조합된 자세의 이미지 리소스 ID 반환
	 *
	 * @param context 현재 Context
	 * @param upper 상지 자세
	 * @param lower 하지 자세
	 * @return 해당 이미지 리소스 ID (정의되지 않은 ID일 경우 -1 반환)
	 */
	public int getPostureImageID(Context context, Posture upper, Posture lower) {
		return getPostureImageID(context, upper.getName(), lower.getName());
	}

	/** 상지+하지 모두 조합된 자세의 이미지 리소스 ID 반환
	 *
	 * @param context 현재 Context
	 * @param upper 상지 자세 이름
	 * @param lower 하지 자세 이름
	 * @return 해당 이미지 리소스 ID (정의되지 않은 ID일 경우 -1 반환)
	 */
	public int getPostureImageID(Context context, String upper, String lower) {
		int iUpper = getPostureID(upper, PostureType.UPPER);
		int iLower = getPostureID(lower, PostureType.LOWER);

		if(iUpper >= 0 && iLower >= 0) {
			StringBuilder builder = new StringBuilder("posture_");
			if(iUpper < 10) {
				builder.append(0);
			}
			builder.append(iUpper);
			builder.append('_');
			if(iLower < 10) {
				builder.append(0);
			}
			builder.append(iLower);
			Logger.debug(this.getClass(), "Image ID -> " + builder.toString());
			int id = context.getResources().getIdentifier(builder.toString(), "drawable", context.getPackageName());
			if(id != 0)
				return id;
		}
		Logger.error(this.getClass(), "Null ID returned");
		return -1;
	}

	/** 자세의 이미지 리소스 ID 반환
	 *
	 * @param posture 자세
	 * @return 해당 이미지 리소스 ID (정의되지 않은 ID일 경우 -1 반환)
	 */
	public int getPostureImageID(Posture posture) {
		return getPostureImageID(posture.getName(), posture.getType());
	}


	/** 자세의 이미지 리소스 ID 반환
	 *
	 * @param posture 자세 이름
	 * @param type 상지 또는 하지 여부
	 * @return 해당 이미지 리소스 ID (정의되지 않은 ID일 경우 -1 반환)
	 */
	public int getPostureImageID(String posture, PostureType type) {
		switch (type) {
			case UPPER:
				return PostureAnalyzer.IMAGE_ID_UPPER_POSTURE[getPostureID(posture, PostureType.UPPER)];
			case LOWER:
				return PostureAnalyzer.IMAGE_ID_LOWER_POSTURE[getPostureID(posture, PostureType.LOWER)];
		}
		Logger.error(this.getClass(), "Null " + type +" ID returned");
		return -1;
	}

	private int getPostureID(String posture, PostureType type) {
		switch (type) {
			case UPPER:
				for(int i = 0; i < NAME_UPPER_POSTURE.length; i++) {
					if(NAME_UPPER_POSTURE[i].equals(posture)) {
						return i;
					}
				}
				break;
			case LOWER:
				for(int i = 0; i < NAME_LOWER_POSTURE.length; i++) {
					if(NAME_LOWER_POSTURE[i].equals(posture)) {
						return i;
					}
				}
				break;
		}
		Logger.error(this.getClass(), "No defined " + type + " posture");
		return -1;
	}
}
