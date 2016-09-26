package pe.hgs.truler.tools.ergonomics;

import java.util.HashMap;

import pe.hgs.truler.R;
import pe.hgs.truler.tools.Logger;

/** 자세에 대한 정의 데이터베이스 클래스, 원래는 데이터베이스를 사용하여 데이터베이스와 요청을 중계하는 클래스가 되어야 하나
 * 작성자의 귀차니즘으로 데이터베이스에 들어갈 데이터를 모두 코드에 구현하였음.
 * <p>그리고 코드 사용 편의성을 늘리기 위해 객체 생성 시 코드에 있는 데이터를 다시 Hashmap을 생성하여 저장하고 Key와 함께 데이터를 요청할 때마다 Hashmap에서 꺼내는
 * 아주 비효율적인 방식을 사용하고 있음. 또한 원래라면 싱글턴 객체로 만들어 사용해야 하나 코드 작성자의 알 수 없는 이유로 그냥 일반 클래스로 작성되어 있음. 추후 이 코드에 대한 재작성을 필요로 함.<p>
 *
 * Created by ysb06 on 2016-08-19.
 */
public class PostureRiskAnalyzer
{
	// TODO: 2016-08-19 주의 이 부분은 추후 외부 테이블 객체(Ex. CSV)를 읽어들이는 식으로 변경을 요함

	public static final String[] NAME_UPPER_POSTURE
			= { "B0-S0-E45", "B0-S0-E90", "B0-S45-E0", "B0-S45-E45", "B0-S45-E90", "B0-S90-E45", "B0-S90-E90", "B0-S120-E0",
			"B45-S45-E0", "B45-S45-E45", "B45-S90-E0", "B45-S90-E45", "B90-S90-E0", "B90-S90-E45" };
	public static final String[] NAME_LOWER_POSTURE
			= { "STD", "KF150", "KF120", "KF90", "KF60", "KF30", "KF30C", "KNL_1", "KNL_2", "SF_CRS", "SC0", "SC20", "SC40"};

	public static final int[] RISK_UPPER_POSTURE_BASIC 		= {1, 1, 2, 2, 2, 3, 3, 4, 2, 3, 3, 3, 2, 2};
	public static final int[] RISK_LOWER_POSTURE_BASIC 		= {2, 3, 4, 4, 4, 3, 3, 3, 3, 1, 2, 1, 1};

	public static final int[] RISK_LV1_UPPER_POSTURE_TIME 	= {3, 2, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
	public static final int[] RISK_LV2_UPPER_POSTURE_TIME 	= {8, 7, 2, 2, 1, -1, -1, -1, 1, -1, -1, -1, 1, 1};
	public static final int[] RISK_LV3_UPPER_POSTURE_TIME 	= {17, 14, 9, 8, 5, 2, 2, -1, 2, 5, 2, 4, 6, 5};

	public static final int[] RISK_LV1_LOWER_POSTURE_TIME 	= {2, -1, -1, -1, -1, -1, -1, -1, -1, 2, 1, 2, 2};
	public static final int[] RISK_LV2_LOWER_POSTURE_TIME 	= {11, 8, -1, -1, -1, 2, 2, 2, 2, 11, 2, 13, 18};
	public static final int[] RISK_LV3_LOWER_POSTURE_TIME 	= {33, 12, -1, -1, -1, 9, 3, 4, 5, 35, 18, 38, 57};

	public static final int[] IMAGE_ID_UPPER_POSTURE 	= { R.drawable.upper01, R.drawable.upper02, R.drawable.upper03, R.drawable.upper04, R.drawable.upper05, R.drawable.upper06, R.drawable.upper07,
																	R.drawable.upper08, R.drawable.upper09, R.drawable.upper10, R.drawable.upper11, R.drawable.upper12, R.drawable.upper13, R.drawable.upper14,};
	public static final int[] IMAGE_ID_LOWER_POSTURE 	= { R.drawable.lower01, R.drawable.lower02, R.drawable.lower03, R.drawable.lower04, R.drawable.lower05, R.drawable.lower06, R.drawable.lower07,
																	R.drawable.lower08, R.drawable.lower09, R.drawable.lower10, R.drawable.lower11, R.drawable.lower12, R.drawable.lower13};

	//현재 클래스 내부에서 쓸 데이터 읽기 편이성을 위한 변수들
	private HashMap<String, Integer> mapPostureRisk;
	private HashMap<String, Integer> mapPostureLv1RiskTime;
	private HashMap<String, Integer> mapPostureLv2RiskTime;
	private HashMap<String, Integer> mapPostureLv3RiskTime;
	private HashMap<String, Integer> mapPostureImageID;


	public PostureRiskAnalyzer() {
		if(NAME_UPPER_POSTURE.length == RISK_UPPER_POSTURE_BASIC.length &&
				NAME_UPPER_POSTURE.length == RISK_LV1_UPPER_POSTURE_TIME.length &&
				NAME_UPPER_POSTURE.length == RISK_LV2_UPPER_POSTURE_TIME.length &&
				NAME_UPPER_POSTURE.length == RISK_LV3_UPPER_POSTURE_TIME.length &&
				NAME_UPPER_POSTURE.length == IMAGE_ID_UPPER_POSTURE.length) {
			Logger.debug("Upper Posture Data Verified");
		}
		if(NAME_UPPER_POSTURE == null) {
			Logger.error("Runtime Error Fuck");
		}

		if(NAME_LOWER_POSTURE.length == RISK_LOWER_POSTURE_BASIC.length &&
				NAME_LOWER_POSTURE.length == RISK_LV1_LOWER_POSTURE_TIME.length &&
				NAME_LOWER_POSTURE.length == RISK_LV2_LOWER_POSTURE_TIME.length &&
				NAME_LOWER_POSTURE.length == RISK_LV3_LOWER_POSTURE_TIME.length &&
				NAME_LOWER_POSTURE.length == IMAGE_ID_LOWER_POSTURE.length) {
			Logger.debug("Lower Posture Data Verified");
		}

		mapPostureRisk = new HashMap<>();
		mapPostureLv1RiskTime = new HashMap<>();
		mapPostureLv2RiskTime = new HashMap<>();
		mapPostureLv3RiskTime = new HashMap<>();
		mapPostureImageID = new HashMap<>();

		for(int i = 0; i < NAME_UPPER_POSTURE.length; i++ ) {
			mapPostureRisk.put(NAME_UPPER_POSTURE[i], RISK_UPPER_POSTURE_BASIC[i]);
			mapPostureLv1RiskTime.put(NAME_UPPER_POSTURE[i], RISK_LV1_UPPER_POSTURE_TIME[i]);
			mapPostureLv2RiskTime.put(NAME_UPPER_POSTURE[i], RISK_LV2_UPPER_POSTURE_TIME[i]);
			mapPostureLv3RiskTime.put(NAME_UPPER_POSTURE[i], RISK_LV3_UPPER_POSTURE_TIME[i]);
			mapPostureImageID.put(NAME_UPPER_POSTURE[i], IMAGE_ID_UPPER_POSTURE[i]);
		}

		for(int i = 0; i < NAME_LOWER_POSTURE.length; i++) {
			mapPostureRisk.put(NAME_LOWER_POSTURE[i], RISK_LOWER_POSTURE_BASIC[i]);
			mapPostureLv1RiskTime.put(NAME_LOWER_POSTURE[i], RISK_LV1_LOWER_POSTURE_TIME[i]);
			mapPostureLv2RiskTime.put(NAME_LOWER_POSTURE[i], RISK_LV2_LOWER_POSTURE_TIME[i]);
			mapPostureLv3RiskTime.put(NAME_LOWER_POSTURE[i], RISK_LV3_LOWER_POSTURE_TIME[i]);
			mapPostureImageID.put(NAME_LOWER_POSTURE[i], IMAGE_ID_LOWER_POSTURE[i]);
		}
	}

	/** 상지 또는 하지 자세에서 시간을 고려하지 않은 위험도 반환
	 *
	 * @param posture 위험도를 구할 자세 객체
	 * @return 해당 자세의 위험도
	 */
	public int getBasicRisk(Posture posture) {
		Integer val = mapPostureRisk.get(posture.getName());
		if(val != null)
			return val;
		else
			return 0;
	}

	/** 자세와 시간에 따른 위험도를 반환
	 *
	 * @param posture 자세 객체
	 * @param time 시간(분)
	 * @return 위험도
	 */
	public int getTimeRisk(Posture posture, int time) {
		Integer iMaxLv1 = mapPostureLv1RiskTime.get(posture.getName());
		Integer iMaxLv2 = mapPostureLv2RiskTime.get(posture.getName());
		Integer iMaxLv3 = mapPostureLv3RiskTime.get(posture.getName());

		if(time < iMaxLv1) {
			return 1;
		} else if(time < iMaxLv2) {
			return 2;
		} else if(time < iMaxLv3) {
			return 3;
		} else {
			return 4;
		}
	}

	/** 해당 자세를 나타내는 그림의 리소스 ID
	 *
	 * @param posture ID를 구할 자세
	 * @return 리소스 ID
	 */
	public int getImageID(Posture posture) {
		Integer integer = mapPostureImageID.get(posture.getName());
		if(integer == null) {
			Logger.error("There is no such posture");
			return -1;
		}
		return integer;
	}

	/** 해당 자세가 데이터베이스 상에서 정의되었는지 여부를 판단
	 *
	 * @param posture 정의 여부를 판단할 자세 객체
	 * @return 자세가 데이터베이스 상에서 정의되었는지 여부
	 */
	public boolean isDefined(Posture posture) {
		switch (posture.getType()) {
			case UPPER:
				for(String name : NAME_UPPER_POSTURE) {
					if(name.equals(posture.getName())) {
						return true;
					}
				}
			case LOWER:
				for(String name : NAME_LOWER_POSTURE) {
					if(name.equals(posture.getName())) {
						return true;
					}
				}
		}
		return false;
	}

	/** 데이터베이스 상에서 해당 자세의 다음 위치에 있는 자세를 반환, 먼저 상지 자세에서 검색 후 결과가 없을 경우 하지에서 검색.
	 * 두 데이터베이스에서 모두 없는 경우 null 반환
	 *
	 * @param posture 기준 위치 자세 객체
	 * @return 기준 자세의 다음 위치에 있는 자세
	 */
	public Posture getNextPosture(Posture posture) {
		for(int i = 0; i < NAME_UPPER_POSTURE.length; i++) {
			if(NAME_UPPER_POSTURE[i].equals(posture.getName())) {
				if(i < NAME_UPPER_POSTURE.length - 1) {
					return new Posture(Posture.PostureType.UPPER, NAME_UPPER_POSTURE[i + 1]);
				} else {
					return new Posture(Posture.PostureType.UPPER, NAME_UPPER_POSTURE[0]);
				}
			}
		}

		for(int i = 0; i < NAME_LOWER_POSTURE.length; i++) {
			if(NAME_LOWER_POSTURE[i].equals(posture.getName())) {
				if(i < NAME_LOWER_POSTURE.length - 1) {
					return new Posture(Posture.PostureType.LOWER, NAME_LOWER_POSTURE[i + 1]);
				} else {
					return new Posture(Posture.PostureType.LOWER, NAME_LOWER_POSTURE[0]);
				}
			}
		}

		return null;
	}

	/** 데이터베이스 상에서 해당 자세의 이전 위치에 있는 자세를 반환, 먼저 상지 자세에서 검색 후 결과가 없을 경우 하지에서 검색.
	 * 두 데이터베이스에서 모두 없는 경우 null 반환
	 *
	 * @param posture 기준 위치 자세 객체
	 * @return 기준 자세의 이전 위치에 있는 자세
	 */
	public Posture getPrevPosture(Posture posture) {
		for(int i = 0; i < NAME_UPPER_POSTURE.length; i++) {
			if(NAME_UPPER_POSTURE[i].equals(posture.getName())) {
				if(i > 0) {
					return new Posture(Posture.PostureType.UPPER, NAME_UPPER_POSTURE[i - 1]);
				} else {
					return new Posture(Posture.PostureType.UPPER, NAME_UPPER_POSTURE[NAME_UPPER_POSTURE.length - 1]);
				}
			}
		}

		for(int i = 0; i < NAME_LOWER_POSTURE.length; i++) {
			if(NAME_LOWER_POSTURE[i].equals(posture.getName())) {
				if(i > 0) {
					return new Posture(Posture.PostureType.LOWER, NAME_LOWER_POSTURE[i - 1]);
				} else {
					return new Posture(Posture.PostureType.LOWER, NAME_LOWER_POSTURE[NAME_LOWER_POSTURE.length - 1]);
				}
			}
		}

		return null;
	}


	/** 해당 이름의 자세의 순서를 반환. 상지, 하지에 대해 따로 적용. 없는 자세일 경우 -1 반환
	 *
	 * @param name 자세의 이름
	 * @return 해당 자세의 순서 (0~)
	 */
	public static int getOrder(String name) {

		for(int i = 0; i < NAME_UPPER_POSTURE.length; i++) {
			if(NAME_UPPER_POSTURE[i].equals(name)) {
				return i;
			}
		}

		for(int i = 0; i < NAME_LOWER_POSTURE.length; i++) {
			if(NAME_LOWER_POSTURE[i].equals(name)) {
				return i;
			}
		}

		return -1;
	}
}
