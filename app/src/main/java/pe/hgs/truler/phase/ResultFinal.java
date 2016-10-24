package pe.hgs.truler.phase;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pe.hgs.truler.R;
import pe.hgs.truler.phase.subphase.PreventionGuide;
import pe.hgs.truler.phase.subphase.PreventionVideo;
import pe.hgs.truler.tools.LogWindow;
import pe.hgs.truler.tools.Logger;
import pe.hgs.truler.tools.view.ResultView;

public class ResultFinal extends AppCompatActivity implements View.OnClickListener, Phase {

	private enum State { SUMMARY, UPPER, LOWER }

	private String sUpperName = "";
	private int iUpperRisk = 0;
	private int iUpperTimeRisk = 0;

	private String sLowerName = "";
	private int iLowerRisk = 0;
	private int iLowerTimeRisk = 0;

	private int iWorkTime = 0;

	private State state = State.SUMMARY;
	private Button btState01;
	private Button btState02;
	private Button btState03;
	private Button btLaw;
	private Button btexer;
	private ResultView rvResult;
	private TextView textComment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_final);

		btState01 = (Button) findViewById(R.id.button_rf_status_01);
		btState02 = (Button) findViewById(R.id.button_rf_status_02);
		btState03 = (Button) findViewById(R.id.button_rf_status_03);
		btState01.setOnClickListener(this);
		btState02.setOnClickListener(this);
		btState03.setOnClickListener(this);
		btLaw = (Button) findViewById(R.id.button_rf_upper_law);
		btexer = (Button) findViewById(R.id.button_rf_upper_exercise);
		btLaw.setOnClickListener(this);
		btexer.setOnClickListener(this);
		rvResult = (ResultView) findViewById(R.id.image_rf_result);
		textComment = (TextView) findViewById(R.id.text_rf_comment);

		sUpperName = getIntent().getStringExtra(RESULT_UPPER_NAME);
		iUpperRisk = getIntent().getIntExtra(RESULT_UPPER_BASIC, 0);
		iUpperTimeRisk = getIntent().getIntExtra(RESULT_UPPER_TIME, 0);

		sLowerName = getIntent().getStringExtra(RESULT_LOWER_NAME);
		iLowerRisk = getIntent().getIntExtra(RESULT_LOWER_BASIC, 0);
		iLowerTimeRisk = getIntent().getIntExtra(RESULT_LOWER_TIME, 0);

		iWorkTime = getIntent().getIntExtra(INFO_WORK_TIME, 0);

		Logger.debug("Final Selection -> " + sUpperName + " [Lv." + iUpperRisk + ", " + iUpperTimeRisk + "],\t" + sLowerName + " [Lv." + iLowerRisk + ", " + iLowerTimeRisk + "]	for " + iWorkTime + " min.");
		//수정할 부분
			/*
			textRiskName.setText(sUpperName);
			textRiskTypes[iUpperRisk - 1].setBackgroundColor(Color.RED);
			textResults[iUpperRisk - 1].setText("위험수준 " + iUpperTimeRisk + "단계");
			//*/
		setStateAsSummary(this.getResources().getConfiguration().orientation);
		setComment();
		initializeButtons(btState01);

		//진짜 결과 창 실행
		Intent intent = new Intent(this, ResultFinal01.class);

		intent.putExtra(RESULT_UPPER_NAME, sUpperName);
		intent.putExtra(RESULT_UPPER_BASIC, iUpperRisk);
		intent.putExtra(RESULT_UPPER_TIME, iUpperTimeRisk);
		intent.putExtra(RESULT_LOWER_NAME, sLowerName);
		intent.putExtra(RESULT_LOWER_BASIC, iLowerRisk);
		intent.putExtra(RESULT_LOWER_TIME, iLowerTimeRisk);
		intent.putExtra(INFO_WORK_TIME, iWorkTime);

		startActivityForResult(intent, PHASE_RESULT);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		switch (state) {
			case SUMMARY:
				setStateAsSummary(newConfig.orientation);
				break;
			case UPPER:
				setStateAsUpper(newConfig.orientation);
				break;
			case LOWER:
				setStateAsLower(newConfig.orientation);
				break;
			default:
				Logger.error("Unknown State");
				break;
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.resultfinal, menu);
		setTitle("결과");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_rf_show:
				Intent itLog = new Intent(this, LogWindow.class);
				startActivity(itLog);
				break;
			case R.id.menu_rf_test:
				//커스텀 뷰 테스트 코드

				break;
			case R.id.menu_rf_finish:
				setResult(RESULT_OK);
				finish();
				break;
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		setResult(RESULT_OK);
		finish();
	}

	private TextView getTextView(int id) {
		return (TextView) findViewById(id);
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
			case R.id.button_rf_status_01:
				setStateAsSummary(this.getResources().getConfiguration().orientation);
				initializeButtons(btState01);
				break;
			case R.id.button_rf_status_02:
				setStateAsUpper(this.getResources().getConfiguration().orientation);
				initializeButtons(btState02);
				break;
			case R.id.button_rf_status_03:
				setStateAsLower(this.getResources().getConfiguration().orientation);
				initializeButtons(btState03);
				break;
			case R.id.button_rf_upper_law:
				Intent itGuide = new Intent(this, PreventionGuide.class);
				startActivity(itGuide);
				break;
			case R.id.button_rf_upper_exercise:
				Intent itVideo = new Intent(this, PreventionVideo.class);
				startActivity(itVideo);
				break;
			default:
				Logger.error("Unknown Button");
				break;
		}



		/* Temp 사용시 사용 코드
		for(TextView txt : textResults) {
			txt.setText("");
		}
		for(TextView txt : textRiskTypes) {
			txt.setBackgroundColor(Color.WHITE);
		}

		switch (view.getId()) {
			case R.id.button_rf_temp_next:
				textRiskName.setText(sLowerName);
				textRiskTypes[iLowerRisk - 1].setBackgroundColor(Color.RED);
				textResults[iLowerRisk - 1].setText("위험수준 " + iLowerTimeRisk + "단계");
				break;
			case R.id.button_rf_temp_prev:
				textRiskName.setText(sUpperName);
				textRiskTypes[iUpperRisk - 1].setBackgroundColor(Color.RED);
				textResults[iUpperRisk - 1].setText("위험수준 " + iUpperTimeRisk + "단계");
				break;
			default:
				break;

		}
		//*/
	}

	private void initializeButtons(Button button) {
		btState01.setBackgroundColor(Color.GRAY);
		btState01.setTextColor(Color.BLACK);
		btState02.setBackgroundColor(Color.GRAY);
		btState02.setTextColor(Color.BLACK);
		btState03.setBackgroundColor(Color.GRAY);
		btState03.setTextColor(Color.BLACK);

		button.setBackgroundColor(Color.DKGRAY);
		button.setTextColor(Color.WHITE);
	}

	private void setStateAsSummary(int orientation) {
		state = State.SUMMARY;
		if(orientation == Configuration.ORIENTATION_PORTRAIT) {
			rvResult.setImageResource(R.drawable.tables);
		} else if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
			rvResult.setImageResource(R.drawable.tables2);
		} else {
			Logger.error("Unknown Orientation");
		}
		rvResult.setToSummary(iUpperRisk, iLowerRisk, orientation);
	}

	private void setStateAsUpper(int orientation) {
		state = State.UPPER;
		if(orientation == Configuration.ORIENTATION_PORTRAIT) {
			rvResult.setImageResource(R.drawable.tableu);
		} else if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
			rvResult.setImageResource(R.drawable.tableu2);
		} else {
			Logger.error("Unknown Orientation");
		}
		rvResult.setToUpper(sUpperName, iUpperTimeRisk, orientation);
	}

	private void setStateAsLower(int orientation) {
		state = State.LOWER;
		if(orientation == Configuration.ORIENTATION_PORTRAIT) {
			rvResult.setImageResource(R.drawable.tablel);
		} else if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
			rvResult.setImageResource(R.drawable.tablel2);
		} else {
			Logger.error("Unknown Orientation -> " + orientation);
		}
		rvResult.setToLower(sLowerName, iLowerTimeRisk, orientation);
	}


	private void setComment() {
		String str = "상지 : ";
		if(sUpperName.equals("B0-S0-E45") || sUpperName.equals("B0-S0-E90")) {	//팔꿈치 부담 작업
			str += getString(R.string.comment_upper_01);
 		} else if(sUpperName.equals("B0-S45-E0") || sUpperName.equals("B0-S45-E90") || sUpperName.equals("B0-S120-E0")) { //어깨 부담 작업
			str += getString(R.string.comment_upper_02);
		} else if(sUpperName.equals("B0-S45-E45") || sUpperName.equals("B0-S90-E45") || sUpperName.equals("B0-S90-E90")) { //어깨, 팔꿈치 부담 작업
			str += getString(R.string.comment_upper_03);
		} else if(sUpperName.equals("B45-S45-E0") || sUpperName.equals("B90-S90-E0")) {	//허리 부담 작업
			str += getString(R.string.comment_upper_04);
		} else if(sUpperName.equals("B45-S45-E45") || sUpperName.equals("B45-S90-E0") || sUpperName.equals("B45-S90-E45")) { //허리, 어깨 부담 작업
			str += getString(R.string.comment_upper_05);
		} else if(sUpperName.equals("B90-S90-E45")) {
			str += getString(R.string.comment_upper_06);
		} else {
			Logger.warn("Unknown posture name : " + sUpperName);
		}
		str += "\n하지 : ";
		if(sLowerName.equals("KF150") || sLowerName.equals("KF120")) {	//무릎 부담 작업
			str += getString(R.string.comment_lower_01);
		} else if(sLowerName.equals("KF90") || sLowerName.equals("KF60") || sLowerName.equals("KF30") || sLowerName.equals("KNL_1") || sLowerName.equals("KNL_2")) { //무릎, 허리 부담 작업
			str += getString(R.string.comment_lower_02);
		} else if(sLowerName.equals("SC0")) { //허리 부담 작업
			str += getString(R.string.comment_lower_03);
		} else if(sLowerName.equals("KF30C")) {	//발목 부담 작업
			str += getString(R.string.comment_lower_04);
		} else if(sLowerName.equals("STD") || sLowerName.equals("SC40") || sLowerName.equals("SC20") || sLowerName.equals("SF_CRS")) { //없음
			str += "부담 없음";
		} else {
			Logger.warn("Unknown posture name : " + sLowerName);
		}
		textComment.setText(str);
	}
}
