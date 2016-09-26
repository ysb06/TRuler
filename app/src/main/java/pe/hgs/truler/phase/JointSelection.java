package pe.hgs.truler.phase;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import pe.hgs.truler.R;
import pe.hgs.truler.tools.ImageLoader;
import pe.hgs.truler.tools.ergonomics.Joint;
import pe.hgs.truler.tools.view.JointView;
import pe.hgs.truler.tools.view.JointViewListener;
import pe.hgs.truler.tools.Logger;

public class JointSelection extends AppCompatActivity implements JointViewListener, View.OnClickListener {

	private Intent result;
	private Bitmap bitmapSelectedImage;
	private Uri uriImagePath;

	private Button btFinish;
	private TextView txtGuide;
	private ImageView ivScreen;
	private JointView cvDotLayer;

	private boolean isSelecting = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joint_setting);
		setTitle("관절 선택");
		result = new Intent();

		btFinish = (Button) findViewById(R.id.btFinish);
		btFinish.setOnClickListener(this);
		txtGuide = (TextView) findViewById(R.id.textGuide);
		ivScreen = (ImageView) findViewById(R.id.iScreen);
		cvDotLayer = (JointView) findViewById(R.id.cView)	;		//관절 선택 표시 뷰 초기화
		cvDotLayer.addListener(this);

		if(getIntent().getIntExtra("TaskInfo_06_WorkTime", -1) == -1) {
			Intent intentTI = new Intent(this, TaskInfo.class);
			startActivityForResult(intentTI, Phase.PHASE_INPUT_INFORMATION);
		} else {
			Intent prevResult = new Intent();
			prevResult.putExtras(getIntent().getExtras());
			onActivityResult(Phase.PHASE_INPUT_INFORMATION, 11, prevResult);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(bitmapSelectedImage != null)
			bitmapSelectedImage.recycle();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == Phase.PHASE_INPUT_INFORMATION && resultCode >= 10) {
			result = data;
			setResult(RESULT_CANCELED, result);

			if(resultCode == 10) {

				//// TODO: 2016-08-19 그림을 내부 저장소에 저장할 수 있게 할 것-----------------------!!
				Logger.debug("카메라 선택");
				Calendar cal = Calendar.getInstance();
				String str = cal.get(Calendar.YEAR) + "" + cal.get(Calendar.MONTH) + "" + cal.get(Calendar.DATE) + ".png";

				Intent intentAIC = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				//intentAIC.putExtra(MediaStore.EXTRA_OUTPUT, uriImagePath);
				startActivityForResult(intentAIC, Phase.PHASE_CAMERA);

			} else if(resultCode == 11) {

				Logger.debug("저장소 선택");
				Intent intentAP = new Intent(Intent.ACTION_PICK);
				intentAP.setType("image/*");
				startActivityForResult(intentAP, Phase.PHASE_IMAGE_LOADING);

			} else {
				Logger.error("JointSelection -> 올바른 Request. 그러나 처리되지 않음");
			}
		} else if(requestCode == Phase.PHASE_INPUT_INFORMATION && resultCode == RESULT_CANCELED) {
			finish();
		} else if(requestCode == Phase.PHASE_IMAGE_LOADING && resultCode == RESULT_OK) {
			uriImagePath = data.getData();
			Logger.debug(uriImagePath.toString());
			ImageLoader loader = new ImageLoader(this.getContentResolver(), uriImagePath);
			bitmapSelectedImage = loader.getImage();
			ivScreen.setImageBitmap(bitmapSelectedImage);
		} else if(requestCode == Phase.PHASE_IMAGE_LOADING && resultCode == RESULT_CANCELED) {
			finish();
		} else if(requestCode == Phase.PHASE_CAMERA && resultCode == RESULT_OK) {
			//// TODO: 2016-08-19 그림을 내부 저장소에 저장하여 파일 경로를 불러오고 URI를 획득할 것 ---------!!
			/*
			bitmapSelectedImage = (Bitmap)data.getExtras().get("data");
			ivScreen.setImageBitmap(bitmapSelectedImage);
			//*/
			Intent intentAP = new Intent(Intent.ACTION_PICK);
			intentAP.setType("image/*");
			startActivityForResult(intentAP, Phase.PHASE_IMAGE_LOADING);
		} else if(requestCode == Phase.PHASE_CAMERA && resultCode == RESULT_CANCELED) {		//카메라 촬영이 취소된 경우
			Intent intentAP = new Intent(Intent.ACTION_PICK);
			intentAP.setType("image/*");
			startActivityForResult(intentAP, Phase.PHASE_IMAGE_LOADING);
		} else {
			Logger.error("JointSelection -> Request 처리되지 않음");
		}
	}

	@Override
	public void onJointSettingCompleted() {

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Logger.debug("JS onConfigurationChanged");
	}

	@Override
	public void onJointSelected(Joint joint, int count) {
		switch (count) {
			case 1:
				txtGuide.setText(R.string.layout_jointselection_textview_guide_02);
				joint.setPoint(Joint.JPoint.HEAD);
				btFinish.setVisibility(View.INVISIBLE);
				break;
			case 2:
				txtGuide.setText(R.string.layout_jointselection_textview_guide_03);
				joint.setPoint(Joint.JPoint.SHOULDER);
				break;
			case 3:
				txtGuide.setText(R.string.layout_jointselection_textview_guide_04);
				joint.setPoint(Joint.JPoint.ELBOW);
				break;
			case 4:
				txtGuide.setText(R.string.layout_jointselection_textview_guide_05);
				joint.setPoint(Joint.JPoint.WRIST);
				break;
			case 5:
				txtGuide.setText(R.string.layout_jointselection_textview_guide_06);
				joint.setPoint(Joint.JPoint.WAIST);
				break;
			case 6:
				txtGuide.setText(R.string.layout_jointselection_textview_guide_07);
				joint.setPoint(Joint.JPoint.KNEE);
				break;
			case 7:
				txtGuide.setText(R.string.layout_jointselection_textview_guide_08);
				joint.setPoint(Joint.JPoint.FOOT);
				btFinish.setVisibility(View.VISIBLE);
				btFinish.setText(R.string.layout_jointselection_button_finish);
				isSelecting = false;
				cvDotLayer.setInputAvailable(false);
				break;
			default:
				Logger.error("Joint Selection -> Joint Count 이상");
				break;
		}
	}

	@Override
	public void onClick(View view) {

		if(isSelecting) {
			rotateTarget();
		} else {
			Joint[] list = cvDotLayer.getJointsList();
			result.putExtra("JointSelection_01_Head", list[0]);
			result.putExtra("JointSelection_02_Shoulder", list[1]);
			result.putExtra("JointSelection_03_Elbow", list[2]);
			result.putExtra("JointSelection_04_Wrist", list[3]);
			result.putExtra("JointSelection_05_Waist", list[4]);
			result.putExtra("JointSelection_06_Knee", list[5]);
			result.putExtra("JointSelection_07_Foot", list[6]);
			result.putExtra("JointSelection_08_List", list);            // TODO: 2016-08-15 추후 이것만 사용할 것인지 위에 따로따로 저장된 것만 사용할 것인지 정할 것
			result.putExtra("JointSelection_09_Image_Path", uriImagePath);

			setResult(RESULT_OK, result);
			ivScreen.setImageDrawable(null);
			finish();
		}
	}

	// TODO: 2016-09-24 따로 여기에서만 사용하는 이미지 클래스를 만들 것. 그리고 이 메서드 그리고 ImageLoader의 rotate메서드를 합칠 것
	/** 왼쪽 선택한 이미지를 회전 시킨다
	 *
	 */
	private void rotateTarget() {
		Matrix matrix = new Matrix();
		matrix.postRotate(90);

		Bitmap bitmapNew = Bitmap.createBitmap(bitmapSelectedImage , 0, 0, bitmapSelectedImage.getWidth(), bitmapSelectedImage.getHeight(), matrix, true);
		bitmapSelectedImage.recycle();
		bitmapSelectedImage = bitmapNew;
		ivScreen.setImageBitmap(bitmapSelectedImage);
	}
}
