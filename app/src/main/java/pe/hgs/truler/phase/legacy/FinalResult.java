package pe.hgs.truler.phase.legacy;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import pe.hgs.truler.R;
import pe.hgs.truler.phase.Phase;
import pe.hgs.truler.phase.subphase.PreventionGuide;
import pe.hgs.truler.phase.subphase.PreventionVideo;
import pe.hgs.truler.tools.LogWindow;
import pe.hgs.truler.tools.Logger;

public class FinalResult extends AppCompatActivity implements View.OnClickListener, Phase {

	private Fragment fragmentScreen01;
	private Fragment fragmentScreen02;
	private Fragment fragmentScreen03;
	private Fragment fragmentScreen04;

	private TextView textComment;
	private Button button01;
	private Button button02;
	private Button button03;
	private Button button04;
	private Button button05;
	private Button button06;

	private String sUpperName = "";
	private int iUpperRisk = 0;
	private int iUpperTimeRisk = 0;
	private String sLowerName = "";
	private int iLowerRisk = 0;
	private int iLowerTimeRisk = 0;

	private int iWorkTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_final_result);
		setTitle("결과");

		fragmentScreen01 = new FinalResult01();
		fragmentScreen02 = new FinalResult02();
		FragmentManager manager = getFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(fragmentScreen01, "Summary");
		transaction.add(fragmentScreen02, "Specified");

		manager.popBackStack("Specified", 0);
		transaction.replace(R.id.fragment_fr_result, fragmentScreen02);
		transaction.commit();


		textComment = (TextView) findViewById(R.id.text_fr_comment);
		button01 = (Button) findViewById(R.id.button_fr_summary_basic);
		button02 = (Button) findViewById(R.id.button_fr_summary_time);
		button03 = (Button) findViewById(R.id.button_fr_upper);
		button04 = (Button) findViewById(R.id.button_fr_lower);
		button05 = (Button) findViewById(R.id.button_fr_law);
		button06 = (Button) findViewById(R.id.button_fr_exercise);
		button01.setOnClickListener(this);
		button02.setOnClickListener(this);
		button03.setOnClickListener(this);
		button04.setOnClickListener(this);
		button05.setOnClickListener(this);
		button06.setOnClickListener(this);

		sUpperName = getIntent().getStringExtra(RESULT_UPPER_NAME);
		iUpperRisk = getIntent().getIntExtra(RESULT_UPPER_BASIC, 0);
		iUpperTimeRisk = getIntent().getIntExtra(RESULT_UPPER_TIME, 0);
		sLowerName = getIntent().getStringExtra(RESULT_LOWER_NAME);
		iLowerRisk = getIntent().getIntExtra(RESULT_LOWER_BASIC, 0);
		iLowerTimeRisk = getIntent().getIntExtra(RESULT_LOWER_TIME, 0);

		iWorkTime = getIntent().getIntExtra(INFO_WORK_TIME, 0);

		Logger.debug("Final Selection -> " + sUpperName + " [Lv." + iUpperRisk + ", " + iUpperTimeRisk + "],\t" + sLowerName + " [Lv." + iLowerRisk + ", " + iLowerTimeRisk + "]	for " + iWorkTime + " min.");
		setComment();
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
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.button_fr_summary_basic:
				changeScreen(fragmentScreen01);
				break;
			case R.id.button_fr_summary_time:
				changeScreen(fragmentScreen01);
				break;
			case R.id.button_fr_upper:
				changeScreen(fragmentScreen02);
				break;
			case R.id.button_fr_lower:
				changeScreen(fragmentScreen02);
				break;
			case R.id.button_fr_law:
				Intent intentLaw = new Intent(this, PreventionGuide.class);
				startActivity(intentLaw);
				break;
			case R.id.button_fr_exercise:
				Intent intentExercise = new Intent(this, PreventionVideo.class);
				startActivity(intentExercise);
				break;
			default:
				break;
		}
	}

	private void changeScreen(Fragment fragment) {

		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		// Replace whatever is in the fragment_container view with this fragment,
		// and add the transaction to the back stack
		transaction.replace(R.id.fragment_fr_result, fragment);
		transaction.addToBackStack(null);

		// Commit the transaction
		transaction.commit();
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
