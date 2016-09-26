package pe.hgs.truler.phase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import pe.hgs.truler.R;
import pe.hgs.truler.tools.ImageLoader;
import pe.hgs.truler.tools.Logger;
import pe.hgs.truler.tools.ergonomics.Joint;
import pe.hgs.truler.tools.ergonomics.BoneStructure;
import pe.hgs.truler.tools.ergonomics.Posture;
import pe.hgs.truler.tools.ergonomics.PostureRiskAnalyzer;

public class JointRevision extends AppCompatActivity implements View.OnClickListener {

	private BoneStructure bsHuman;
	private Bitmap bitTarget;
	private PostureRiskAnalyzer pra;

	private Posture postureFinalUpper;
	private Posture postureFinalLower;

	private ImageView ivTarget;
	private ImageView ivSample;
	private Button btYes;

	private Button btNo;
	private Button btNo2;

	private boolean isUpperOK = false;
	private Intent result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joint_revision);
		setTitle("자세 선택");

		ivTarget = (ImageView) findViewById(R.id.image_jr_target);
		ivSample = (ImageView) findViewById(R.id.image_jr_sample);
		btYes = (Button) findViewById(R.id.button_jr_yes);
		btYes.setOnClickListener(this);

		btNo = (Button) findViewById(R.id.button_jr_next);
		btNo2 = (Button) findViewById(R.id.button_jr_prev);
		btNo.setOnClickListener(this);
		btNo2.setOnClickListener(this);

		pra = new PostureRiskAnalyzer();

		Intent itPhase = new Intent(this, JointSelection.class);
		startActivityForResult(itPhase, Phase.PHASE_JOINT_SELECTION);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(resultCode == RESULT_OK) {
			Joint[] joints = new Joint[7];
			joints[0] = data.getParcelableExtra("JointSelection_01_Head");
			joints[1] = data.getParcelableExtra("JointSelection_02_Shoulder");
			joints[2] = data.getParcelableExtra("JointSelection_03_Elbow");
			joints[3] = data.getParcelableExtra("JointSelection_04_Wrist");
			joints[4] = data.getParcelableExtra("JointSelection_05_Waist");
			joints[5] = data.getParcelableExtra("JointSelection_06_Knee");
			joints[6] = data.getParcelableExtra("JointSelection_07_Foot");

			bsHuman = new BoneStructure(joints);

			postureFinalUpper = bsHuman.getPosture(Posture.PostureType.UPPER);
			if(!pra.isDefined(postureFinalUpper)) {
				postureFinalUpper = new Posture(Posture.PostureType.UPPER);
				Logger.warn("The upper posture is not defined. Set to default");
			}
			postureFinalLower = bsHuman.getPosture(Posture.PostureType.LOWER);
			if(!pra.isDefined(postureFinalLower)) {
				postureFinalLower = new Posture(Posture.PostureType.LOWER);
				Logger.error("The lower posture is not defined!!");
			}

			initializeComparing(postureFinalUpper);

			Uri uri = data.getParcelableExtra("JointSelection_09_Image_Path");
			if(uri != null) {
				ImageLoader loader = new ImageLoader(this.getContentResolver(), uri);
				bitTarget = loader.getImage();
				ivTarget.setImageBitmap(bitTarget);
			}

			result = data;
		} else if(resultCode == RESULT_CANCELED) {
			finish();
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			// 일단은 No 일경우 step3을 다시 하도록 할 것
			case R.id.button_jr_yes:
				if(isUpperOK) {		//최종 선택시 (하지 선택 완료)
					int iMinute = result.getIntExtra("TaskInfo_06_WorkTime", -1);
					if(iMinute == -1) {
						Logger.error("작업시간에 대한 정보 없음");
					}

					int iBasicUpperRisk = pra.getBasicRisk(postureFinalUpper);
					int iBasicUpperTimeRisk = pra.getTimeRisk(postureFinalUpper, iMinute);
					int iBasicLowerRisk = pra.getBasicRisk(postureFinalLower);
					int iBasicLowerTimeRisk = pra.getTimeRisk(postureFinalLower, iMinute);

					result.putExtra("JointRevision_01_UpperBasic", iBasicUpperRisk);
					result.putExtra("JointRevision_02_UpperTime", iBasicUpperTimeRisk);
					result.putExtra("JointRevision_03_LowerBasic", iBasicLowerRisk);
					result.putExtra("JointRevision_04_LowerTime", iBasicLowerTimeRisk);
					result.putExtra("JointRevision_05_Name_Upper", postureFinalUpper.getName());
					result.putExtra("JointRevision_06_Name_Lower", postureFinalLower.getName());

					setResult(RESULT_OK, result);
					finish();
				} else {		//상지 선택 완료 시, 하지 선택으로 넘어가야 함
					initializeComparing(postureFinalLower);
					TextView guideTitle = (TextView) findViewById(R.id.text_jr_body);
					guideTitle.setText(getText(R.string.layout_jointrevision_textview_lower));
					isUpperOK = true;
				}
				break;
			case R.id.button_jr_prev:
				if(isUpperOK) {	//하지 선택 단계에서
					postureFinalLower = pra.getPrevPosture(postureFinalLower);
					if(postureFinalLower == null) {			//하지는 절대 null 이 반환될 수 없음
						Logger.error("Lower posture is null");
						postureFinalLower = new Posture(Posture.PostureType.LOWER);
					}
					initializeComparing(postureFinalLower);
				} else {	//상지 선택 단계에서
					postureFinalUpper = pra.getPrevPosture(postureFinalUpper);		//정의되지 않은 상지 자세의 경우 이 단계에서 null 이 반환됨
					if(postureFinalUpper == null) {
						Logger.warn("Upper posture is null");
						postureFinalUpper = new Posture(Posture.PostureType.UPPER);
					}
					initializeComparing(postureFinalUpper);
				}
				break;
			case R.id.button_jr_next:		//아니오 선택 시
				if(isUpperOK) {	//하지 선택 단계에서
					postureFinalLower = pra.getNextPosture(postureFinalLower);
					if(postureFinalLower == null) {			//하지는 절대 null 이 반환될 수 없음
						Logger.error("Lower posture is null");
						postureFinalLower = new Posture(Posture.PostureType.LOWER);
					}
					initializeComparing(postureFinalLower);
				} else {	//상지 선택 단계에서
					postureFinalUpper = pra.getNextPosture(postureFinalUpper);		//정의되지 않은 상지 자세의 경우 이 단계에서 null 이 반환됨
					if(postureFinalUpper == null) {
						Logger.warn("Upper posture is null");
						postureFinalUpper = new Posture(Posture.PostureType.UPPER);
					}
					initializeComparing(postureFinalUpper);
				}
				break;	//와 같이 다음 사진을 보여주어야 함

			default:
				Logger.error("Unknown Button");
				break;
		}
	}

	private void initializeComparing(Posture posture) {
		if(pra.isDefined(posture)) {
			ivSample.setImageResource(pra.getImageID(posture));
		} else {
			Logger.error("The posture is not defined");
		}
	}
}
