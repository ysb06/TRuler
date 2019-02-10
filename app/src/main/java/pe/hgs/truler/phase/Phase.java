package pe.hgs.truler.phase;

/** 페이즈 관련 신호. 페이즈 간 구분이나 각 페이즈에서 리턴 값 전송 시 사용되는 코드
 * Created by ysb06 on 2016-08-07.
 */
public interface Phase {

	/** 메인 메뉴 및 타이틀 화면 */
    int PHASE_MAIN = 1;
	/** 정보 입력 단계 */
	int PHASE_INPUT_INFORMATION = 2;
	/** 카메라 촬영 단계 */
	int PHASE_CAMERA = 3;
	/** 사진 선택 단계 */
	int PHASE_IMAGE_LOADING = 4;
	/** 관절 선택 단계 */
	int PHASE_JOINT_SELECTION = 5;
	/** 자세 선택 단계 */
	int PHASE_REVISION = 6;
	/** 결과 종합 단계 */
	int PHASE_RESULT = 7;
	/**	결과 상지 및 하지 단계 */
	int PHASE_RESULT_DETAIL = 8;

	String INFO_WORKER = "TaskInfo_01";
	String INFO_CROP = "TaskInfo_02";
	String INFO_TASK = "TaskInfo_03";
	String INFO_SUB_TASK = "TaskInfo_04";
	String INFO_LOCATION = "TaskInfo_05";
	String INFO_WORK_TIME = "TaskInfo_06";
	String INFO_ASSESSOR = "TaskInfo_07";
	String INFO_WEIGHT = "TaskInfo_08";
	String INFO_ACT_POINT = "TaskInfo_09";
	String INFO_NECK_BAND = "TaskInfo_10";

	String URI_SELECTED_IMAGE = "MainActivity_01";

	String JOINT_HEAD = "JointSelection_01";
	String JOINT_SHOULDER = "JointSelection_02";
	String JOINT_ELBOW = "JointSelection_03";
	String JOINT_WRIST = "JointSelection_04";
	String JOINT_WAIST = "JointSelection_05";
	String JOINT_KNEE = "JointSelection_06";
	String JOINT_FOOT = "JointSelection_07";

	String NUMBER_IMAGE_ROTATE = "JointSelection_08";

	String RESULT_UPPER_BASIC = "JointRevision_01";
	String RESULT_UPPER_TIME = "JointRevision_02";
	String RESULT_LOWER_BASIC = "JointRevision_03";
	String RESULT_LOWER_TIME = "JointRevision_04";
	String RESULT_UPPER_NAME = "JointRevision_05";
	String RESULT_LOWER_NAME = "JointRevision_06";
}
