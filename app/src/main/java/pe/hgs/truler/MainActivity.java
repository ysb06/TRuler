package pe.hgs.truler;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import pe.hgs.truler.phase.FinalResult;
import pe.hgs.truler.phase.JointRevision;
import pe.hgs.truler.phase.JointSelection;
import pe.hgs.truler.phase.Phase;
import pe.hgs.truler.phase.ResultFinal;
import pe.hgs.truler.phase.TaskInfo;
import pe.hgs.truler.phase.legacy.others.MainMenu;
import pe.hgs.truler.tools.FileManager;
import pe.hgs.truler.tools.Logger;
import pe.hgs.truler.tools.ergonomics.Joint;

/** 액티비티 전환 관리 */
public class MainActivity extends AppCompatActivity implements Phase {

	private static final int RESULT_OK_CAMERA = 10;
	private static final int RESULT_OK_IMAGE = 11;

	private String sWorker = "";
	private String sCrop = "";
	private String sTask = "";
	private String sSubTask = "";
	private String sLocation = "";
	private int iWorkTime = 0;
	private String sAssessor = "";

	private Uri uriImage;

	private Joint[] listJoints = new Joint[7];
	private int iRotateCount = 0;

	private String sUpperName = "";
	private int iUpperRisk = 0;
	private int iUpperTimeRisk = 0;
	private String sLowerName = "";
	private int iLowerRisk = 0;
	private int iLowerTimeRisk = 0;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		
		Intent intent = new Intent(this, MainMenu.class);
		startActivityForResult(intent, Phase.PHASE_MAIN);
		
		Logger.debug("MainActivity OnCreate");
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case Phase.PHASE_MAIN:
				//______________________________ 메인 화면 종료 ______________________________ //
				if(resultCode == RESULT_OK) {
					Intent intent = new Intent(this, TaskInfo.class);
					startActivityForResult(intent, Phase.PHASE_INPUT_INFORMATION);		//정보 입력 단계로...
				} else if(resultCode == RESULT_CANCELED) {
					finish();		//타이틀 화면에서 뒤로가기를 누르면 바로 종료
					Logger.warn(this.getClass(), "The App Terminated");
				} else {
					Logger.error(this.getClass(), "Unknown Result -> " + resultCode);
				}
				break;
			case Phase.PHASE_INPUT_INFORMATION:
				//______________________________ 정보 입력 종료 ______________________________ //
				if(resultCode == RESULT_OK_CAMERA) {				// 카메라 촬영을 선택했을 시
					setInformation(data);		// 입력 받은 정보 기록
					Logger.debug("카메라 선택");
					Intent intentAIC = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intentAIC, Phase.PHASE_CAMERA);
				} else if(resultCode == RESULT_OK_IMAGE) {		// 이미지 선택을 선택했을 시
					setInformation(data);		// 입력 받은 정보 기록
					Logger.debug("저장소 선택");
					runImageSelection();
				} else if(resultCode == RESULT_CANCELED) {
					Intent intent = new Intent(this, MainMenu.class);
					startActivityForResult(intent, Phase.PHASE_MAIN);        //아무 것도 입력되지 않은채 종료되면 타이틀화면으로 다시 이동
				} else {
					Logger.error(this.getClass(), "Unknown Result -> " + resultCode);
				}
				break;
			case Phase.PHASE_CAMERA:
				//______________________________ 카메라 촬영 종료 ______________________________ //
				if(resultCode == RESULT_OK) {
					Logger.debug("Camera -> " + data.getData().toString());
					runJointSelection(data.getData());
				} else if(resultCode == RESULT_CANCELED) {
					rerunInputInformation();
				} else {
					Logger.error(this.getClass(), "Unknown Result -> " + resultCode);
				}
				break;
			case Phase.PHASE_IMAGE_LOADING:
				//______________________________ 사진 선택 종료 ______________________________ //
				if(resultCode == RESULT_OK) {
					runJointSelection(data.getData());
				} else if(resultCode == RESULT_CANCELED) {
					rerunInputInformation();
				} else {
					Logger.error(this.getClass(), "Unknown Result -> " + resultCode);
				}
				break;
			case Phase.PHASE_JOINT_SELECTION:
				//______________________________ 관절 입력 종료 ______________________________ //
				if(resultCode == RESULT_OK) {
					listJoints[0] = data.getParcelableExtra(JOINT_HEAD);
					listJoints[1] = data.getParcelableExtra(JOINT_SHOULDER);
					listJoints[2] = data.getParcelableExtra(JOINT_ELBOW);
					listJoints[3] = data.getParcelableExtra(JOINT_WRIST);
					listJoints[4] = data.getParcelableExtra(JOINT_WAIST);
					listJoints[5] = data.getParcelableExtra(JOINT_KNEE);
					listJoints[6] = data.getParcelableExtra(JOINT_FOOT);

					iRotateCount = data.getIntExtra(NUMBER_IMAGE_ROTATE, 0);

					runJointRevision();
				} else if(resultCode == RESULT_CANCELED) {
					rerunInputInformation();
				} else {
					Logger.error(this.getClass(), "Unknown Result -> " + resultCode);
				}
				break;
			case Phase.PHASE_REVISION:
				//______________________________ 자세 입력 종료 ______________________________ //
				if(resultCode == RESULT_OK) {
					sUpperName = data.getStringExtra(RESULT_UPPER_NAME);
					iUpperRisk = data.getIntExtra(RESULT_UPPER_BASIC, 0);
					iUpperTimeRisk = data.getIntExtra(RESULT_UPPER_TIME, 0);
					sLowerName = data.getStringExtra(RESULT_LOWER_NAME);
					iLowerRisk = data.getIntExtra(RESULT_LOWER_BASIC, 0);
					iLowerTimeRisk = data.getIntExtra(RESULT_LOWER_TIME, 0);

					Intent intent = new Intent(this, ResultFinal.class);

					intent.putExtra(RESULT_UPPER_NAME, sUpperName);
					intent.putExtra(RESULT_UPPER_BASIC, iUpperRisk);
					intent.putExtra(RESULT_UPPER_TIME, iUpperTimeRisk);
					intent.putExtra(RESULT_LOWER_NAME, sLowerName);
					intent.putExtra(RESULT_LOWER_BASIC, iLowerRisk);
					intent.putExtra(RESULT_LOWER_TIME, iLowerTimeRisk);
					intent.putExtra(INFO_WORK_TIME, iWorkTime);

					startActivityForResult(intent, Phase.PHASE_RESULT);
				} else if(resultCode == RESULT_CANCELED) {
					runJointSelection(uriImage);
				} else {
					Logger.error(this.getClass(), "Unknown Result -> " + resultCode);
				}
				break;
			case Phase.PHASE_RESULT:
				//______________________________ 결과 확인 종료 ______________________________ //
				if(resultCode == RESULT_OK) {
					// TODO: 2016-09-27 데이터 저장 기능 추가
					Logger.debug("Complete!");
					save();
				} else if(resultCode == RESULT_CANCELED) {
					runJointRevision();
				} else {
					Logger.error(this.getClass(), "Unknown Result -> " + resultCode);
				}
				break;
		}
	}

	/** 데이터 받을 시 데이터 기록(정보 입력, 결과 확인 종료)
	 *
	 * @param data 결과로 받은 Intent
	 */
	private void setInformation(Intent data) {
		sWorker = data.getStringExtra(INFO_WORKER);
		sCrop = data.getStringExtra(INFO_CROP);
		sTask = data.getStringExtra(INFO_TASK);
		sSubTask = data.getStringExtra(INFO_SUB_TASK);
		sLocation = data.getStringExtra(INFO_LOCATION);
		iWorkTime = data.getIntExtra(INFO_WORK_TIME, 0);
		sAssessor = data.getStringExtra(INFO_ASSESSOR);		//입력 받은 정보 추출
	}

	/** 이미지 선택 창 실행
	 *
	 */
	private void runImageSelection() {
		Intent intentAP = new Intent(Intent.ACTION_PICK);
		intentAP.setType("image/*");
		startActivityForResult(intentAP, Phase.PHASE_IMAGE_LOADING);
	}

	/** 정보 입력 재실행
	 *
	 */
	private void rerunInputInformation() {
		Intent intent = new Intent(this, TaskInfo.class);

		intent.putExtra(INFO_WORKER, sWorker);
		intent.putExtra(INFO_CROP, sCrop);
		intent.putExtra(INFO_TASK, sTask);
		intent.putExtra(INFO_SUB_TASK, sSubTask);
		intent.putExtra(INFO_LOCATION, sLocation);
		intent.putExtra(INFO_WORK_TIME, iWorkTime);
		intent.putExtra(INFO_ASSESSOR, sAssessor);

		startActivityForResult(intent, Phase.PHASE_INPUT_INFORMATION);		//정보 입력 단계로 다시 되돌아 감
	}

	/** 관절, 자세 선택, 결과 확인 Phase 실행
	 *
	 * @param image 선택된 이미지 주소소	 */
	private void runJointSelection(Uri image) {
		//사진 선택 이후로는 (관절-자세-결과) 세 단계를 묶음으로 실행 됨
		uriImage = image;
		Intent intent = new Intent(this, JointSelection.class);
		intent.putExtra(URI_SELECTED_IMAGE, uriImage);
		startActivityForResult(intent, Phase.PHASE_JOINT_SELECTION);        // 결과 창 실행. 자세 선택, 관절 선택이 차례로 한꺼번에 실행
	}

	private void runJointRevision() {
		Intent intent = new Intent(this, JointRevision.class);

		intent.putExtra(JOINT_HEAD, listJoints[0]);
		intent.putExtra(JOINT_SHOULDER, listJoints[1]);
		intent.putExtra(JOINT_ELBOW, listJoints[2]);
		intent.putExtra(JOINT_WRIST, listJoints[3]);
		intent.putExtra(JOINT_WAIST, listJoints[4]);
		intent.putExtra(JOINT_KNEE, listJoints[5]);
		intent.putExtra(JOINT_FOOT, listJoints[6]);
		intent.putExtra(URI_SELECTED_IMAGE, uriImage);
		intent.putExtra(NUMBER_IMAGE_ROTATE, iRotateCount);
		intent.putExtra(INFO_WORK_TIME, iWorkTime);

		startActivityForResult(intent, Phase.PHASE_REVISION);
	}

	private void save() {
		String str = "작업자,작목,지역,작업,세부작업,유지시간,분석자,분석일자,상지자세,상지 위험도,상지 시간 위험도,하지자세,하지 위험도,하지 시간 위험도,종합 위험도,종합 시간 위험도\n\r";
		str += sWorker + "," + sCrop + "," + sLocation + "," + sTask + "," + sSubTask + "," + iWorkTime + "," + sAssessor + ","
				+ sUpperName + "," + iUpperRisk + "," + iUpperTimeRisk + "," + sLowerName + "," + iLowerRisk + "," + iLowerTimeRisk + ","
				+ 0 + "," + 0;

		FileManager.writeFile("save.csv", str);
	}
}
