package pe.hgs.truler.phase;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import pe.hgs.truler.R;
import pe.hgs.truler.phase.subphase.PreventionVideo;
import pe.hgs.truler.tools.DateLoader;
import pe.hgs.truler.tools.FileManager;
import pe.hgs.truler.tools.LogWindow;
import pe.hgs.truler.tools.Logger;
import pe.hgs.truler.tools.ergonomics.ErgoDatabase;
import pe.hgs.truler.tools.ergonomics.PostureAnalyzer;
import pe.hgs.truler.tools.view.ResultView;

import static pe.hgs.truler.phase.Phase.*;

/**
 * 종합 결과 NT <-> T
 */
public class ResultFinal01 extends AppCompatActivity implements View.OnClickListener {

	private static final float MARK_SUMMARY_O_POINT_X = 230f;
	private static final float MARK_SUMMARY_O_POINT_Y = 153f;
	private static final float MARK_SUMMARY_INTERVAL_X = 72.5f;
	private static final float MARK_SUMMARY_INTERVAL_Y = 45f;

	private static final float MARK_UPPER_O_POINT_X = 78f;
	private static final float MARK_UPPER_O_POINT_Y = 192.5f;
	private static final float MARK_UPPER_INTERVAL_X = 60f;
	private static final float MARK_UPPER_INTERVAL_Y = 50f;

	private static final float MARK_LOWER_O_POINT_X = 82f;
	private static final float MARK_LOWER_O_POINT_Y = 192.5f;
	private static final float MARK_LOWER_INTERVAL_X = 65;
	private static final float MARK_LOWER_INTERVAL_Y = 50f;


	private String sUpperName = "";
	private int iUpperRisk = 0;
	private int iUpperTimeRisk = 0;
	private String sLowerName = "";
	private int iLowerRisk = 0;
	private int iLowerTimeRisk = 0;

	private String sWorker = "";
	private String sCrop = "";
	private String sTask = "";
	private String sSubTask = "";
	private String sLocation = "";
	private int iWorkTime = 0;
	private String sAssessor = "";
	private String sWeight = "";
	private String sActPoint = "";
	private String sNeckBand = "";

	private TextView textComment;
	private TextView textUpperRisk;
	private TextView textLowerRisk;
	private Button buttonSummary;
	private Button buttonTimeSummary;
	private Button buttonUpper;
	private Button buttonLower;
	private Button buttonLaw;
	private Button buttonVideo;
	private ResultView resultView;
	private ImageView imagePosture;

	private Bitmap bitmapNormalResult;
	private Bitmap bitmapTimeResult;
	private Bitmap bitmapUpperResult;
	private Bitmap bitmapLowerResult;

	//위험 부위
	private boolean bDangerEl = false;    //팔꿈치
	private boolean bDangerSh = false;    //어깨
	private boolean bDangerWa = false;    //허리
	private boolean bDangerKn = false;    //무릎
	private boolean bDangerAn = false;    //발목

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_final01);
		PostureAnalyzer pa = new PostureAnalyzer();

		sUpperName = getIntent().getStringExtra(RESULT_UPPER_NAME);
		iUpperRisk = getIntent().getIntExtra(RESULT_UPPER_BASIC, 0);
		iUpperTimeRisk = getIntent().getIntExtra(RESULT_UPPER_TIME, 0);
		sLowerName = getIntent().getStringExtra(RESULT_LOWER_NAME);
		iLowerRisk = getIntent().getIntExtra(RESULT_LOWER_BASIC, 0);
		iLowerTimeRisk = getIntent().getIntExtra(RESULT_LOWER_TIME, 0);

		sWorker = getIntent().getStringExtra(INFO_WORKER);
		sCrop = getIntent().getStringExtra(INFO_CROP);
		sTask = getIntent().getStringExtra(INFO_TASK);
		sSubTask = getIntent().getStringExtra(INFO_SUB_TASK);
		sLocation = getIntent().getStringExtra(INFO_LOCATION);
		iWorkTime = getIntent().getIntExtra(INFO_WORK_TIME, 0);
		sAssessor = getIntent().getStringExtra(INFO_ASSESSOR);
		sWeight = getIntent().getStringExtra(INFO_WEIGHT);
		sActPoint = getIntent().getStringExtra(INFO_ACT_POINT);
		sNeckBand = getIntent().getStringExtra(INFO_NECK_BAND);

		textComment = (TextView) findViewById(R.id.text_rf1_comment);
		textUpperRisk = (TextView) findViewById(R.id.text_rf1_upper_risk);
		textLowerRisk = (TextView) findViewById(R.id.text_rf1_lower_risk);
		buttonSummary = (Button) findViewById(R.id.button_rf1_summary);
		buttonTimeSummary = (Button) findViewById(R.id.button_rf1_summary_time);
		buttonUpper = (Button) findViewById(R.id.button_rf1_upper);
		buttonLower = (Button) findViewById(R.id.button_rf1_lower);
		buttonLaw = (Button) findViewById(R.id.button_rf1_save);
		buttonVideo = (Button) findViewById(R.id.button_rf1_exercise);
		resultView = (ResultView) findViewById(R.id.result_rf1);
		imagePosture = (ImageView) findViewById(R.id.image_rf1_upper);

		buttonSummary.setOnClickListener(this);
		buttonTimeSummary.setOnClickListener(this);
		buttonUpper.setOnClickListener(this);
		buttonLower.setOnClickListener(this);
		buttonLaw.setOnClickListener(this);
		buttonVideo.setOnClickListener(this);

		ErgoDatabase database = ErgoDatabase.getInstance();
		imagePosture.setImageResource(database.getPostureImageID(this, sUpperName, sLowerName));

		Bitmap temp = BitmapFactory.decodeResource(getResources(), R.drawable.tables2);
		bitmapNormalResult = temp.copy(temp.getConfig(), true);
		bitmapTimeResult = temp.copy(temp.getConfig(), true);
		temp.recycle();
		Bitmap temp02 = BitmapFactory.decodeResource(getResources(), R.drawable.tableuv2u);
		bitmapUpperResult = temp02.copy(temp.getConfig(), true);
		temp02.recycle();
		Bitmap temp03 = BitmapFactory.decodeResource(getResources(), R.drawable.tableuv2l);
		bitmapLowerResult = temp03.copy(temp.getConfig(), true);
		temp03.recycle();

		//종합 결과에 표시
		markResult(bitmapNormalResult, MARK_SUMMARY_O_POINT_X + MARK_SUMMARY_INTERVAL_X * (4 - iUpperRisk), MARK_SUMMARY_O_POINT_Y + MARK_SUMMARY_INTERVAL_Y * (4 - iLowerRisk));
		markResult(bitmapTimeResult, MARK_SUMMARY_O_POINT_X + MARK_SUMMARY_INTERVAL_X * (4 - iUpperTimeRisk), MARK_SUMMARY_O_POINT_Y + MARK_SUMMARY_INTERVAL_Y * (4 - iLowerTimeRisk));
		//상세 결과에 표시
		markResult(bitmapUpperResult, MARK_UPPER_O_POINT_X + MARK_UPPER_INTERVAL_X * pa.getOrder(sUpperName), MARK_UPPER_O_POINT_Y + MARK_UPPER_INTERVAL_Y * (iUpperTimeRisk - 1));
		markResult(bitmapLowerResult, MARK_LOWER_O_POINT_X + MARK_LOWER_INTERVAL_X * pa.getOrder(sLowerName), MARK_LOWER_O_POINT_Y + MARK_LOWER_INTERVAL_Y * (iLowerTimeRisk - 1));

		initializeButtons(buttonSummary);
		setRiskNormal();
		setComment();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		bitmapNormalResult.recycle();
		bitmapTimeResult.recycle();
		bitmapUpperResult.recycle();
		bitmapLowerResult.recycle();
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
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.button_rf1_summary:
				setRiskNormal();
				initializeButtons((Button) v);
				break;
			case R.id.button_rf1_summary_time:
				setRiskTime();
				initializeButtons((Button) v);
				break;
			case R.id.button_rf1_upper:
				setRiskUpper();
				initializeButtons((Button) v);
				break;
			case R.id.button_rf1_lower:
				setRiskLower();
				initializeButtons((Button) v);
				break;
			case R.id.button_rf1_save:
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
					requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
				save();
				Toast.makeText(this,"저장되었습니다 -> /storage/emulated/0/Context/",Toast.LENGTH_SHORT).show();
				break;
			case R.id.button_rf1_exercise:
				Intent intent = new Intent(this, PreventionVideo.class);
				intent.putExtra(PreventionVideo.DANGER_ELBOW, bDangerEl);
				intent.putExtra(PreventionVideo.DANGER_SHOULDER, bDangerSh);
				intent.putExtra(PreventionVideo.DANGER_WAIST, bDangerWa);
				intent.putExtra(PreventionVideo.DANGER_KNEE, bDangerKn);
				intent.putExtra(PreventionVideo.DANGER_ANKLE, bDangerAn);
				startActivity(intent);
				break;
			default:
				break;
		}
	}


	/**
	 * 일반 위험 표시 모드로 전환
	 */
	private void setRiskNormal() {
		setTitle("결과 종합");
		textUpperRisk.setText("" + iUpperRisk);
		textLowerRisk.setText("" + iLowerRisk);
		resultView.setImageBitmap(bitmapNormalResult);
	}

	/**
	 * 시간 고려 위험 표시 모드로 전환
	 */
	private void setRiskTime() {
		setTitle("결과 종합(시간)");
		textUpperRisk.setText("" + iUpperTimeRisk);
		textLowerRisk.setText("" + iLowerTimeRisk);
		resultView.setImageBitmap(bitmapTimeResult);
	}

	/**
	 * 상지 위험 표시 모드로 전환
	 */
	private void setRiskUpper() {
		setTitle("결과 상지");
		textUpperRisk.setText("" + iUpperTimeRisk);
		textLowerRisk.setText("" + iLowerTimeRisk);
		resultView.setImageBitmap(bitmapUpperResult);

		Matrix matrix = new Matrix(resultView.getImageMatrix());
		matrix.setScale(0.5f, 0.5f);
		resultView.setImageMatrix(matrix);
	}

	/**
	 * 하지 위험 표시 모드로 전환
	 */
	private void setRiskLower() {
		setTitle("결과 하지");
		textUpperRisk.setText("" + iUpperTimeRisk);
		textLowerRisk.setText("" + iLowerTimeRisk);
		resultView.setImageBitmap(bitmapLowerResult);

		Matrix matrix = new Matrix(resultView.getImageMatrix());
		matrix.setScale(0.5f, 0.5f);
		resultView.setImageMatrix(matrix);
	}

	/**
	 * 그림에 동그라미 표시
	 *
	 * @param src 동그라미를 표시한 그림
	 * @param x   동그라미 x 위치
	 * @param y   동그라미 y 위치
	 */
	private void markResult(Bitmap src, float x, float y) {

		Canvas canvas = new Canvas(src);
		float fScale = (float) src.getDensity() / 160f;

		Paint paintMark = new Paint();
		paintMark.setColor(Color.BLUE);
		paintMark.setStyle(Paint.Style.STROKE);
		paintMark.setStrokeWidth(4f * fScale);

		canvas.drawCircle(x * fScale, y * fScale, 15f * fScale, paintMark);
		Logger.debug("Draw: " + (x * fScale) + ", " + (y * fScale) + " -> " + src.getDensity() + ", " + fScale);
	}

	/**
	 * 코멘트 설정
	 */
	private void setComment() {

		String str = "상지: ";
		if (sUpperName.equals("B0-S0-E45") || sUpperName.equals("B0-S0-E90")) {    //팔꿈치 부담 작업
			str += getString(R.string.comment_upper_01);
			bDangerEl = true;
		} else if (sUpperName.equals("B0-S45-E0") || sUpperName.equals("B0-S45-E90") || sUpperName.equals("B0-S120-E0")) { //어깨 부담 작업
			str += getString(R.string.comment_upper_02);
			bDangerSh = true;
		} else if (sUpperName.equals("B0-S45-E45") || sUpperName.equals("B0-S90-E45") || sUpperName.equals("B0-S90-E90")) { //어깨, 팔꿈치 부담 작업
			str += getString(R.string.comment_upper_03);
			bDangerSh = true;
			bDangerEl = true;
		} else if (sUpperName.equals("B45-S45-E0") || sUpperName.equals("B90-S90-E0")) {    //허리 부담 작업
			str += getString(R.string.comment_upper_04);
			bDangerWa = true;
		} else if (sUpperName.equals("B45-S45-E45") || sUpperName.equals("B45-S90-E0") || sUpperName.equals("B45-S90-E45")) { //허리, 어깨 부담 작업
			str += getString(R.string.comment_upper_05);
			bDangerSh = true;
			bDangerWa = true;
		} else if (sUpperName.equals("B90-S90-E45")) {
			str += getString(R.string.comment_upper_06);
			bDangerWa = true;
			bDangerEl = true;
		} else {
			Logger.warn("Unknown posture name : " + sUpperName);
		}
		str += "\n하지: ";
		if (sLowerName.equals("KF150") || sLowerName.equals("KF120")) {    //무릎 부담 작업
			str += getString(R.string.comment_lower_01);
			bDangerKn = true;
		} else if (sLowerName.equals("KF90") || sLowerName.equals("KF60") || sLowerName.equals("KF30") || sLowerName.equals("KNL_1") || sLowerName.equals("KNL_2")) { //무릎, 허리 부담 작업
			str += getString(R.string.comment_lower_02);
			bDangerKn = true;
			bDangerWa = true;
		} else if (sLowerName.equals("SC0")) { //허리 부담 작업
			str += getString(R.string.comment_lower_03);
			bDangerWa = true;
		} else if (sLowerName.equals("KF30C")) {    //발목 부담 작업
			str += getString(R.string.comment_lower_04);
			bDangerAn = true;
		} else if (sLowerName.equals("STD") || sLowerName.equals("SC40") || sLowerName.equals("SC20") || sLowerName.equals("SF_CRS")) { //없음
			str += "부담 없음";
		} else {
			Logger.warn("Unknown posture name : " + sLowerName);
		}
		textComment.setText(str);
	}

	/**
	 * 버튼 초기화 및 색상 변경
	 *
	 * @param v 활성화 할 버튼 객체
	 */
	private void initializeButtons(Button v) {
		int color = Color.parseColor("#ffd6d7d7");

		buttonSummary.setBackgroundColor(color);
		buttonSummary.setTextColor(Color.BLACK);
		buttonTimeSummary.setBackgroundColor(color);
		buttonTimeSummary.setTextColor(Color.BLACK);
		buttonUpper.setBackgroundColor(color);
		buttonUpper.setTextColor(Color.BLACK);
		buttonLower.setBackgroundColor(color);
		buttonLower.setTextColor(Color.BLACK);

		v.setBackgroundColor(Color.GRAY);
		v.setTextColor(Color.WHITE);
	}

	private void save() {
		Logger.debug(sWorker + sCrop + sTask + sSubTask + sLocation + iWorkTime + sAssessor);

		String str = "작업자,지역,작목,작업,세부작업,무게,드는 부위,유지시간,분석자,분석일자,상지자세,상지위험도,상지시간위험도,하지자세,하지위험도,하지시간위험도,종합위험도,종합시간위험도,목젖힘 정도\n";
		str +=  sWorker + ","		//작업자
				+ sLocation + ","		//지역
				+ sCrop + ","		//작목
				+ sTask + ","		//작업
				+ sSubTask + ","		//세부작업
				+ sWeight + ","		//무게
				+ sActPoint + ","		//드는부위
				+ iWorkTime + ","		//유지시간
				+ sAssessor + ","		//분석자
				+ DateLoader.getToday() + ","		//분석일자
				+ sUpperName + ","		//상지자세
				+ iUpperRisk + "," 		//상지위험
				+ iUpperTimeRisk + "," 	//상지시간
				+ sLowerName + "," 		//하지자세
				+ iLowerRisk + "," 		//하지위험
				+ iLowerTimeRisk + ","	//하지시간
				+ getCombineRisk(iUpperRisk, iLowerRisk) + ","					//종합위험
				+ getCombineRisk(iUpperTimeRisk, iLowerTimeRisk) + ","			//종합시간
				+ sNeckBand;

		FileManager.writeFile("save.csv", str);
	}

	private int getCombineRisk(int upper, int lower) {
		int iRisk = 0;

		switch (upper) {
			case 1:
				if(lower > 1) {
					iRisk = 4;
				} else if(lower == 1) {
					iRisk = 3;
				} else {
					iRisk = 0;
				}
				break;
			case 2:
				if(lower < 4) {
					iRisk = 3;
				} else if(lower == 4) {
					iRisk = 4;
				} else {
					iRisk = 0;
				}
				break;
			case 3:
				if(lower < 3) {
					iRisk = 2;
				} else if(lower == 3) {
					iRisk = 3;
				} else if(lower == 4) {
					iRisk = 4;
				} else {
					iRisk = 0;
				}
				break;
			case 4:
				if(lower > 2) {
					iRisk = 3;
				} else if(lower == 2) {
					iRisk = 2;
				} else if(lower == 1) {
					iRisk = 1;
				} else {
					iRisk = 0;
				}
				break;
			default:
				iRisk = 0;
				break;
		}
		return iRisk;
	}
}
