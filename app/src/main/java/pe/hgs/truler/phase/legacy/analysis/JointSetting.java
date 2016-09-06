package pe.hgs.truler.phase.legacy.analysis;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import pe.hgs.truler.R;
import pe.hgs.truler.phase.legacy.analysis.joint_setting.Joint;
import pe.hgs.truler.tools.view.JointView;
import pe.hgs.truler.tools.view.JointViewListener;
import pe.hgs.truler.tools.ImageLoader;
import pe.hgs.truler.tools.Logger;

/** 관절 부분 선택 단계
 *
 */
public class JointSetting extends AppCompatActivity implements JointViewListener, View.OnClickListener {

	public static final int HEAD = 0;
	public static final int SHOULDER = 1;
	public static final int ELBOW = 2;
	public static final int WRIST = 3;
	public static final int WAIST = 4;
	public static final int KNEE = 5;
	public static final int FOOT = 6;

	private Joint[] jointPoints;

	private Button btFinish;
	private TextView txtGuide;
	private ImageView ivScreen;
	private ImageLoader img = null;
	private JointView cvDotLayer;

	private String sImgPath;		//URI 기반 이미지 파일 접근에 문제가 없을 시 삭제해도 무방
	private Uri sImgUriPath;

	private int iNextInput = 0;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Logger.debug("OnCreate Called");
        setContentView(R.layout.activity_joint_setting);

		sImgPath = getIntent().getStringExtra("Image Storage Path");		//이미지 파일 경로, sImgPath 변수 주석 참조
		sImgUriPath = getIntent().getParcelableExtra("Image Media Path");

		btFinish = (Button) findViewById(R.id.btFinish);
		btFinish.setOnClickListener(this);
		txtGuide = (TextView) findViewById(R.id.textGuide);
		ivScreen = (ImageView) findViewById(R.id.iScreen);
		cvDotLayer = (JointView) findViewById(R.id.cView)	;		//관절 선택 표시 뷰 초기화
		cvDotLayer.setOnTouchListener(cvDotLayer);		//터치 리스너 추가


		initScreen();
		initJointPoint();
		//cvDotLayer.setJointPoint(jointPoints);
		cvDotLayer.addListener(this);
    }

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Logger.debug("OnConfigurationChanged Called");
		ivScreen.setImageURI(null);
		img.dispose();

		initScreen();
	}

	private void initScreen() {
		img = new ImageLoader(this.getContentResolver(), sImgUriPath);
		ivScreen.setImageBitmap(img.getImage());
	}

	private void initJointPoint() {
		jointPoints = new Joint[7];
		for(int i = 0; i < jointPoints.length; i++) {
			jointPoints[i] = new Joint();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		Logger.debug("OnPause Called");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Logger.debug("OnStop Called");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Logger.debug("Closing");
		ivScreen.setImageURI(null);
		img.dispose();
	}

	@Override
	public void onJointSettingCompleted() {
		//// TODO: 2016-08-08 추후 입력 취소 기능을 넣을 수 있음
		iNextInput++;
		//cvDotLayer.requestJointPoint(iNextInput);
		switch (iNextInput) {
			case SHOULDER:
				txtGuide.setText(R.string.layout_jointselection_textview_guide_02);
				break;
			case ELBOW:
				txtGuide.setText(R.string.layout_jointselection_textview_guide_03);
				break;
			case WRIST:
				txtGuide.setText(R.string.layout_jointselection_textview_guide_04);
				break;
			case WAIST:
				txtGuide.setText(R.string.layout_jointselection_textview_guide_05);
				break;
			case KNEE:
				txtGuide.setText(R.string.layout_jointselection_textview_guide_06);
				break;
			case FOOT:
				txtGuide.setText(R.string.layout_jointselection_textview_guide_07);
				break;
			default:
				if(iNextInput > 6) {
					txtGuide.setText(R.string.layout_jointselection_textview_guide_08);
					btFinish.setVisibility(View.VISIBLE);
				}
				else
					Logger.error("iNextInput in wrong number");
				break;
		}
	}

	@Override
	public void onJointSelected(pe.hgs.truler.tools.container.Joint joint, int count) {

	}

	@Override
	public void onClick(View view) {
		if(view.equals(btFinish)) {
			Logger.debug("Finished");
			Intent result = new Intent();
			result.putExtra("jpx", getXList(jointPoints));
			result.putExtra("jpy", getYList(jointPoints));
			setResult(RESULT_OK, result);

			finish();
		}
	}

	private float[] getXList(Joint[] jpList) {
		float[] xs = new float[jpList.length];
		for(int i = 0; i < xs.length; i++) {
			xs[i] = jpList[i].getX();
		}
		return xs;
	}

	private float[] getYList(Joint[] jpList) {
		float[] ys = new float[jpList.length];
		for(int i = 0; i < ys.length; i++) {
			ys[i] = jpList[i].getY();
		}
		return ys;
	}
}