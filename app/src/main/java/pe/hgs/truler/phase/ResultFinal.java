package pe.hgs.truler.phase;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import pe.hgs.truler.R;
import pe.hgs.truler.tools.Logger;
import pe.hgs.truler.tools.view.ResultView;

public class ResultFinal extends AppCompatActivity implements View.OnClickListener {

	private enum State { SUMMARY, UPPER, LOWER }

	private String sUpperName = "";
	private int iUpperRisk = 0;
	private int iUpperTimeRisk = 0;

	private String sLowerName = "";
	private int iLowerRisk = 0;
	private int iLowerTimeRisk = 0;

	private int iWorkTime = 0;

	private State state = State.SUMMARY;
	private int iCurrentOrientation = 0;
	private Button btState01;
	private Button btState02;
	private ResultView rvResult;

	private TextView textRiskName;
	private TextView[] textRiskTypes;
	private TextView[] textResults;
	private Button btNext;
	private Button btPrev;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_final_upper);

		Intent itJointRevision = new Intent(this, JointRevision.class);
		startActivityForResult(itJointRevision, Phase.PHASE_REVISION);

		btState01 = (Button) findViewById(R.id.button_rf_status_01);
		btState02 = (Button) findViewById(R.id.button_rf_status_02);
		btState01.setOnClickListener(this);
		btState02.setOnClickListener(this);
		rvResult = (ResultView) findViewById(R.id.image_rf_result);
		/*
		textRiskName = getTextView(R.id.text_rf_temp_guide_08);
		textRiskTypes = new TextView[4];
		textRiskTypes[0] = getTextView(R.id.text_rf_temp_guide_03);
		textRiskTypes[1] = getTextView(R.id.text_rf_temp_guide_04);
		textRiskTypes[2] = getTextView(R.id.text_rf_temp_guide_05);
		textRiskTypes[3] = getTextView(R.id.text_rf_temp_guide_06);
		textResults = new TextView[4];
		textResults[0] = getTextView(R.id.text_rf_temp_result_01);
		textResults[1] = getTextView(R.id.text_rf_temp_result_02);
		textResults[2] = getTextView(R.id.text_rf_temp_result_03);
		textResults[3] = getTextView(R.id.text_rf_temp_result_04);
		btNext = (Button) findViewById(R.id.button_rf_temp_next);
		btPrev = (Button) findViewById(R.id.button_rf_temp_prev);
		btNext.setOnClickListener(this);
		btPrev.setOnClickListener(this);
		//*/
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == Phase.PHASE_REVISION && resultCode == RESULT_OK) {
			sUpperName = data.getStringExtra("JointRevision_05_Name_Upper");
			iUpperRisk = data.getIntExtra("JointRevision_01_UpperBasic", 0);
			iUpperTimeRisk = data.getIntExtra("JointRevision_02_UpperTime", 0);

			sLowerName = data.getStringExtra("JointRevision_06_Name_Lower");
			iLowerRisk = data.getIntExtra("JointRevision_03_LowerBasic", 0);
			iLowerTimeRisk = data.getIntExtra("JointRevision_04_LowerTime", 0);

			iWorkTime = data.getIntExtra("TaskInfo_06_WorkTime", 0);

			Logger.debug("Final Selection -> " + sUpperName + " [Lv." + iUpperRisk + ", " + iUpperTimeRisk + "],\t" + sLowerName + " [Lv." + iLowerRisk + ", " + iLowerTimeRisk + "]	for " + iWorkTime + " min.");
			//수정할 부분
			/*
			textRiskName.setText(sUpperName);
			textRiskTypes[iUpperRisk - 1].setBackgroundColor(Color.RED);
			textResults[iUpperRisk - 1].setText("위험수준 " + iUpperTimeRisk + "단계");
			//*/
			setStateAsSummary(this.getResources().getConfiguration().orientation);
		} else if(requestCode == Phase.PHASE_REVISION && resultCode == RESULT_CANCELED) {
			finish();
		}
	}

	private TextView getTextView(int id) {
		return (TextView) findViewById(id);
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
			case R.id.button_rf_status_01:
				switch (state) {
					case UPPER:
					case LOWER:
						setStateAsSummary(this.getResources().getConfiguration().orientation);
						break;
					case SUMMARY:
						setStateAsUpper(this.getResources().getConfiguration().orientation);
						break;
				}
				break;
			case R.id.button_rf_status_02:
				switch (state) {
					case SUMMARY:
					case UPPER:
						setStateAsLower(this.getResources().getConfiguration().orientation);
						break;
					case LOWER:
						setStateAsUpper(this.getResources().getConfiguration().orientation);
						break;
				}
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

	private void setStateAsSummary(int orientation) {
		state = State.SUMMARY;
		btState01.setText(R.string.layout_result_button_upper);
		btState02.setText(R.string.layout_result_button_lower);
		if(orientation == Configuration.ORIENTATION_PORTRAIT) {
			rvResult.setImageResource(R.drawable.tables);
		} else if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
			rvResult.setImageResource(R.drawable.tables2);
		} else {
			Logger.error("Unknown Orientation");
		}
		rvResult.showSummaryMark(iUpperRisk, iLowerRisk, orientation);
	}

	private void setStateAsUpper(int orientation) {
		state = State.UPPER;
		btState01.setText(R.string.layout_result_button_summary);
		btState02.setText(R.string.layout_result_button_lower);
		if(orientation == Configuration.ORIENTATION_PORTRAIT) {
			rvResult.setImageResource(R.drawable.tableu);
		} else if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
			rvResult.setImageResource(R.drawable.tableu2);
		} else {
			Logger.error("Unknown Orientation");
		}
		rvResult.showUpperMark(sUpperName, iUpperTimeRisk, orientation);
	}

	private void setStateAsLower(int orientation) {
		state = State.LOWER;
		btState01.setText(R.string.layout_result_button_summary);
		btState02.setText(R.string.layout_result_button_upper);
		if(orientation == Configuration.ORIENTATION_PORTRAIT) {
			rvResult.setImageResource(R.drawable.tablel);
		} else if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
			rvResult.setImageResource(R.drawable.tablel2);
		} else {
			Logger.error("Unknown Orientation -> " + orientation);
		}
		rvResult.showLowerMark(sLowerName, iLowerTimeRisk, orientation);
	}
}
