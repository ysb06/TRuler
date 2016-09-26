package pe.hgs.truler;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pe.hgs.truler.phase.Phase;
import pe.hgs.truler.phase.ResultFinal;
import pe.hgs.truler.phase.legacy.analysis.SubAssessment;
import pe.hgs.truler.phase.legacy.others.MainMenu;
import pe.hgs.truler.tools.Logger;

/** 액티비티 전환 관리 */
public class MainActivity extends AppCompatActivity {

	private String sTempFilePath;
	private Uri uriTempMediaPath;
	private float[] fxTemp;
	private float[] fyTemp;

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

		if(resultCode == RESULT_OK) {
			Intent itPhaseLast = new Intent(this, ResultFinal.class);
			startActivity(itPhaseLast);
		} else if(resultCode == RESULT_CANCELED) {

		}
	}



















	/***********************---------------------------------------------------------------------
	 * 메인메뉴(구현X)	-> 프로필 선택	 -> 사진 촬영		-> 관절 포인트 선택		<-> 자세 선택 평가		-> 결과 분석	-> 메인 메뉴(구현X)
	 * 									 -> 사진 선택		->
	 * 									<-> 프로필 생성
	 ***********************

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Phase.PHASE_INPUT_INFORMATION:										//프로필 작성 화면 종료 시
				//*
				Intent itIS = new Intent(Intent.ACTION_PICK);
				itIS.setType("image/*");
				startActivityForResult(itIS, Phase.PHASE_IMAGE_LOADING);

                break;

			case Phase.PHASE_CAMERA:													//카메라 촬영 완료 시
			case Phase.PHASE_IMAGE_LOADING:											//이미지 선택 완료 시
				Intent itJS;
				if(resultCode == -1) {
					String sImageFilePath = interpretPath(data.getData());		//아래 Todo참조
					sTempFilePath = sImageFilePath;
					uriTempMediaPath = data.getData();
					Logger.debug("Path -> " + uriTempMediaPath.getPath() + ", " + sTempFilePath);

					itJS = new Intent(this, JointSetting.class);
					itJS.putExtra("Image Media Path", data.getData());		//경로 저장(URI)
					itJS.putExtra("Image Storage Path", sImageFilePath);			//Todo: 파일 경로 저장, URI 주소 이미지 접근에 문제가 없을 시 삭제, 밑의 메서드 또한 삭제
					startActivityForResult(itJS, Phase.PHASE_JOINT_SELECTION);
				} else {		//주로 0 아무것도 선택 안했을 시 처리
					Logger.warn("No image selected");
					itJS = new Intent(this, ProfileSelection.class);
					startActivityForResult(itJS, Phase.PHASE_INPUT_INFORMATION);
				}
				break;
            case Phase.PHASE_JOINT_SELECTION:													//관절 선택 완료 시
				switch (resultCode) {
					case RESULT_OK:
						Logger.debug("Process Complete");
						fxTemp = data.getFloatArrayExtra("jpx");		//선택된 관절 데이터 X값
						fyTemp = data.getFloatArrayExtra("jpy");		//선택된 관절 데이터 Y값
						break;
					case RESULT_CANCELED:
						Logger.warn("Process Incomplete");
						break;
					default:
						Logger.error("What? " + resultCode);
						break;
				}
				startSubAssessment();
				break;
			case Phase.PHASE_REVISION:											//평가 완료 시
				switch (resultCode) {
					case RESULT_OK:
						if(data.getBooleanExtra("Assessment Result", false)) {
							Intent itResult = new Intent(this, FinalResult.class);
							itResult.putExtra("Joint Point X", fxTemp);
							itResult.putExtra("Joint Point Y", fyTemp);
							startActivityForResult(itResult, Phase.PHASE_RESULT);
						} else {
							itJS = new Intent(this, JointSetting.class);
							itJS.putExtra("Image Media Path", uriTempMediaPath);		//경로 저장(URI)
							startActivityForResult(itJS, Phase.PHASE_JOINT_SELECTION);
						}
						break;
					case RESULT_CANCELED:
						startSubAssessment();
						break;
					default:
						Logger.error("What??? " + resultCode);
						break;
				}
				break;
            default:
				Logger.warn("Runtime Warning -> (" + requestCode + ") No such defined request code");
                break;
        }
    }
	//*/

	private void startSubAssessment() {
		Intent itAssessment = new Intent(this, SubAssessment.class);
		itAssessment.putExtra("ImageUriPath", uriTempMediaPath);
		startActivityForResult(itAssessment, Phase.PHASE_REVISION);
	}

	/** Uri 미디어 형식의 사진 파일 경로를 실제 저장 경로로 변환<p/>주의 : 제대로 작동 안되는 경우가 있는 것으로 보임, URI 기반 이미지 접근에 문제가 없을 시 삭제해도 무방
	 *
	 * @param uri 대상 이미지 URI
	 * @return 대상 이미지 저장소 경로
	 */
	private String interpretPath(Uri uri) {
		String[] filePathColumn = {MediaStore.Images.Media.DATA};
		Cursor cursor = getApplicationContext().getContentResolver().query(uri, filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String picturePath = cursor.getString(columnIndex);
		cursor.close();

		return picturePath;
	}
}
