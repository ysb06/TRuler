package pe.hgs.truler.tools.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import pe.hgs.truler.tools.Logger;
import pe.hgs.truler.tools.ergonomics.PostureRiskAnalyzer;

/**
 * Created by ysb06 on 2016-09-04.
 */
public class ResultView extends ImageView {

	private Paint paintMark;
	private float fMarkX = 0;
	private float fMarkY = 0;
	private float fMarkSize = 30f;

	float fMarkOX = 0;
	float fMarkOY = 0;
	float fMarkIntervalX = 0;
	float fMarkIntervalY = 0;
	private boolean isMarkVisible = false;

	public ResultView(Context context) {
		super(context);
		initialize();
	}

	public ResultView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize();
	}

	public ResultView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initialize();
	}

	private void initialize() {
		paintMark = new Paint();
		paintMark.setColor(Color.BLUE);
		paintMark.setStyle(Paint.Style.STROKE);
		paintMark.setStrokeWidth(7.5f);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(isMarkVisible) {
			canvas.drawCircle(fMarkX, fMarkY, fMarkSize, paintMark);
		}
	}

	public void showSummaryMark(int riskUpper, int riskLower, int orientation) {
		if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
			fMarkOX = 470f;
			fMarkOY = 177f;
			fMarkIntervalX = 86f;
			fMarkIntervalY = 57f;
			fMarkSize = 30f;
		} else if(orientation == Configuration.ORIENTATION_PORTRAIT) {
			fMarkOX = 340f;
			fMarkOY = 390f;
			fMarkIntervalX = 107f;
			fMarkIntervalY = 145f;
			fMarkSize = 45f;
		} else {
			Logger.error("Unknown Orientation in View");
		}

		isMarkVisible = true;
		fMarkX = fMarkOX + fMarkIntervalX * (4 - riskUpper);
		fMarkY = fMarkOY + fMarkIntervalY * (4 - riskLower);

		invalidate();
	}

	public void showUpperMark(String namePos, int riskTimeUpper, int orientation) {
		int poscode = 0;
		if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
			fMarkOX = 393f;
			fMarkOY = 165f;
			fMarkIntervalX = 41f;
			fMarkIntervalY = 57f;
		} else if(orientation == Configuration.ORIENTATION_PORTRAIT) {
			fMarkOX = 73f;
			fMarkOY = 320f;
			fMarkIntervalX = 47.8f;
			fMarkIntervalY = 150f;
		} else {
			Logger.error("Unknown Orientation in View");
		}

		for(int i = 0; i < PostureRiskAnalyzer.NAME_UPPER_POSTURE.length; i++) {
			if(namePos.equals(PostureRiskAnalyzer.NAME_UPPER_POSTURE[i])) {
				poscode = i;
			}
		}

		isMarkVisible = true;
		fMarkX = fMarkOX + fMarkIntervalX * poscode;
		fMarkY = fMarkOY + fMarkIntervalY * (riskTimeUpper - 1);
		fMarkSize= 30f;

		invalidate();
	}

	public void showLowerMark(String namePos, int riskTimeLower, int orientation) {
		int poscode = 0;
		if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
			fMarkOX = 396;
			fMarkOY = 165;
			fMarkIntervalX = 44;
			fMarkIntervalY = 57;
		} else if(orientation == Configuration.ORIENTATION_PORTRAIT) {
			fMarkOX = 78f;
			fMarkOY = 320f;
			fMarkIntervalX = 51.1f;
			fMarkIntervalY = 150f;
		} else {
			Logger.error("Unknown Orientation in View");
		}


		for(int i = 0; i < PostureRiskAnalyzer.NAME_LOWER_POSTURE.length; i++) {
			if(namePos.equals(PostureRiskAnalyzer.NAME_LOWER_POSTURE[i])) {
				poscode = i;
			}
		}

		isMarkVisible = true;
		fMarkX = fMarkOX + fMarkIntervalX * poscode;
		fMarkY = fMarkOY + fMarkIntervalY * (riskTimeLower - 1);
		fMarkSize = 30f;

		invalidate();
	}

	public void hideMark() {
		isMarkVisible = false;
		invalidate();
	}
}
