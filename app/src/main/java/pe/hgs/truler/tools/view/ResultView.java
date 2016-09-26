package pe.hgs.truler.tools.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.ImageView;

import pe.hgs.truler.R;
import pe.hgs.truler.tools.Logger;
import pe.hgs.truler.tools.ergonomics.PostureRiskAnalyzer;

/** 이미지 뷰에서 두가지 기능 추가<p>
 * 1. 상태에 따라 그림에 마크 표시<br>
 * 2. 핀치 투 줌<p>
 *
 * Created by ysb06 on 2016-09-04.
 */
public class ResultView extends ImageView {

	private static final float MARK_SIZE = 30f;
	private static final float ZOOM_SPEED = 0.015f;
	private static final float MOVE_SPEED = 12f;

	private Paint paintMark;
	private Matrix matrix;
	private float fZoomScale = 0;
	private float fImageX = 0;
	private float fImageY = 0;
	private float fImageXMin = -1000;
	private float fImageXMax = 10;
	private float fImageYMin = -1000;
	private float fImageYMax = 10;

	public ResultView(Context context) {
		super(context);
		initializeMark();
		matrix = new Matrix();
	}

	public ResultView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeMark();
		matrix = new Matrix();
	}

	public ResultView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initializeMark();
		matrix = new Matrix();
	}

	float fPrevPinch = 0;
	float fPrevTouchX = 0;
	float fPrevTouchY = 0;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_MOVE:
				float fx1 = event.getX();
				float fy1 = event.getY();
				float fx2 = event.getX(event.getPointerCount() - 1);
				float fy2 = event.getY(event.getPointerCount() - 1);

				float fCurrentPinch = (fx2 - fx1) * (fx2 - fx1) + (fy2 - fy1) * (fy2 - fy1);
				float fcx = (fx1 + fx2) / 2;
				float fcy = (fy1 + fy2) / 2;

				if(event.getPointerCount() > 1) {
					onPinch(fCurrentPinch - fPrevPinch, fcx - fPrevTouchX, fcy - fPrevTouchY);
				}
				fPrevPinch = fCurrentPinch;
				fPrevTouchX = fcx;
				fPrevTouchY = fcy;

				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				break;
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_DOWN:
				fx1 = event.getX();
				fy1 = event.getY();
				fx2 = event.getX(event.getPointerCount() - 1);
				fy2 = event.getY(event.getPointerCount() - 1);

				fPrevTouchX = (fx1 + fx2) / 2;
				fPrevTouchY = (fy1 + fy2) / 2;
				break;
			default:
				Logger.warn("Unknown Touch Case: " + (event.getAction() & MotionEvent.ACTION_MASK));
				break;
		}
		return true;
	}

	/** 핀치가 발생했을 때 실행
	 *
	 * @param pinch 핀치 확대 혹은 축소 크기 제곱
	 * @param xMove x 핀치 위치 변화
	 * @param yMove y 핀치 위치 변화
	 */
	private void onPinch(float pinch, float xMove, float yMove) {
		if(fImageX < fImageXMin)
			fImageX = fImageXMin;
		if(fImageX > fImageXMax)
			fImageX = fImageXMax;

		fImageX += xMove;

		if(fImageY < fImageYMin)
			fImageY = fImageYMin;
		if(fImageY > fImageYMax)
			fImageY = fImageYMax;

		fImageY += yMove;

		//Logger.debug("Pinch : " + pinch);
		if(pinch < -2500) {
			if(fZoomScale > 0.4)
				fZoomScale -= ZOOM_SPEED;
		} else if(pinch > 2500) {
			if(fZoomScale < 1.5)
				fZoomScale += ZOOM_SPEED;
		}

		//Logger.debug("Xmove : " + fImageX + ", Ymove : " + fImageY);

		matrix.setScale(fZoomScale, fZoomScale);
		matrix.postTranslate(fImageX, fImageY);

		setImageMatrix(matrix);
		invalidate();
	}



	//------------------------------마크 기능------------------------------------------//
	private void initializeMark() {
		paintMark = new Paint();
		paintMark.setColor(Color.BLUE);
		paintMark.setStyle(Paint.Style.STROKE);
		paintMark.setStrokeWidth(7.5f);
	}

	public void setToSummary(int upperRisk, int lowerRisk, int orientation) {
		float fMarkOX = 0;
		float fMarkOY = 0;
		float fMarkIntervalX = 0;
		float fMarkIntervalY = 0;

		Bitmap src;

		switch (orientation) {
			case Configuration.ORIENTATION_PORTRAIT:
				fMarkOX = 390;
				fMarkOY = 410;
				fMarkIntervalX = 120;
				fMarkIntervalY = 160;
				src = BitmapFactory.decodeResource(getResources(), R.drawable.tables);
				break;
			case Configuration.ORIENTATION_LANDSCAPE:
				fMarkOX = 460;
				fMarkOY = 300;
				fMarkIntervalX = 120;
				fMarkIntervalY = 80;
				src = BitmapFactory.decodeResource(getResources(), R.drawable.tables2);
				break;
			default:
				Logger.error(this.getClass(), "No defined orientation");
				return;
		}

		reimage(src, fMarkOX + (fMarkIntervalX * (4 - upperRisk)), fMarkOY + (fMarkIntervalY * (4 - lowerRisk)), MARK_SIZE);
		src.recycle();
	}

	public void setToUpper(String posture, int risk, int orientation) {
		float fMarkOX = 0;
		float fMarkOY = 0;
		float fMarkIntervalX = 0;
		float fMarkIntervalY = 0;

		Bitmap src;

		switch (orientation) {
			case Configuration.ORIENTATION_PORTRAIT:
				fMarkOX = 160;
				fMarkOY = 550;
				fMarkIntervalX = 109;
				fMarkIntervalY = 340;
				src = BitmapFactory.decodeResource(getResources(), R.drawable.tableu);
				break;
			case Configuration.ORIENTATION_LANDSCAPE:
				fMarkOX = 160;
				fMarkOY = 450;
				fMarkIntervalX = 109;
				fMarkIntervalY = 150;
				src = BitmapFactory.decodeResource(getResources(), R.drawable.tableu2);
				break;
			default:
				Logger.error(this.getClass(), "No defined orientation");
				return;
		}
		int pos = PostureRiskAnalyzer.getOrder(posture);

		reimage(src, fMarkOX + (fMarkIntervalX * pos), fMarkOY + (fMarkIntervalY * (risk - 1)), MARK_SIZE);
		src.recycle();
	}

	public void setToLower(String posture, int risk, int orientation) {
		float fMarkOX = 0;
		float fMarkOY = 0;
		float fMarkIntervalX = 0;
		float fMarkIntervalY = 0;

		Bitmap src;

		switch (orientation) {
			case Configuration.ORIENTATION_PORTRAIT:
				fMarkOX = 170;
				fMarkOY = 550;
				fMarkIntervalX = 117;
				fMarkIntervalY = 340;
				src = BitmapFactory.decodeResource(getResources(), R.drawable.tablel);
				break;
			case Configuration.ORIENTATION_LANDSCAPE:
				fMarkOX = 172;
				fMarkOY = 450;
				fMarkIntervalX = 115;
				fMarkIntervalY = 150;
				src = BitmapFactory.decodeResource(getResources(), R.drawable.tablel2);
				break;
			default:
				Logger.error(this.getClass(), "No defined orientation");
				return;
		}

		int pos = PostureRiskAnalyzer.getOrder(posture);

		reimage(src, fMarkOX + (fMarkIntervalX * pos), fMarkOY + (fMarkIntervalY * (risk - 1)), MARK_SIZE);
		src.recycle();
	}

	private void reimage(Bitmap src, float fx, float fy, float size) {
		Drawable d = getDrawable();
		if (d instanceof BitmapDrawable) {
			Bitmap b = ((BitmapDrawable)d).getBitmap();
			b.recycle();
		} // 현재로서는 BitmapDrawable 이외의 drawable 들에 대한 직접적인 메모리 해제는 불가능하다.
		d.setCallback(null);

		Bitmap target = src.copy(Bitmap.Config.ARGB_8888, true);
		Canvas canvas = new Canvas(target);
		canvas.setDensity(Bitmap.DENSITY_NONE);

		canvas.drawCircle(fx, fy, size, paintMark);
		setImage(target);
	}

	//--------------- 마크 기능 끝 ---------------------//


	/** 이미지 변경 및 초기화
	 *
	 * @param bitmap 변경할 이미지
	 */
	private void setImage(Bitmap bitmap) {
		setImageBitmap(bitmap);

		DisplayMetrics dm = getResources().getDisplayMetrics();
		int width = dm.widthPixels;
		int height = dm.heightPixels;

		//Logger.debug("vw: " + this.getWidth() + ", vh: " + this.getHeight());
		//Logger.debug("iw: " + bitmap.getWidth() + ", ih: " + bitmap.getHeight());

		fZoomScale = (float) width / (float) bitmap.getWidth();

		matrix.setScale(fZoomScale, fZoomScale);
		this.setImageMatrix(matrix);
	}
}
