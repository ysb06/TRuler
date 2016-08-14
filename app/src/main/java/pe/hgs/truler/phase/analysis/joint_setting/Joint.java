package pe.hgs.truler.phase.analysis.joint_setting;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by ysb06 on 2016-08-08.
 */
public class Joint {

	private float fx;
	private float fy;
	private float fSize;
	private Paint pStyle;

	private boolean bVisible = false;

	public Joint() {
		fx = 0;
		fy = 0;
		fSize = 20;
		pStyle = new Paint();
		pStyle.setAntiAlias(true);
		pStyle.setStyle(Paint.Style.FILL);
		pStyle.setColor(Color.argb(255, 255, 0, 255));
	}

	public void setPosition(float x, float y) {
		fx = x;
		fy = y;
	}

	public void setSize(float size) {
		fSize = size;
	}

	public void setVisible(boolean visible) {
		bVisible = visible;
	}

	public float getX() {
		return fx;
	}

	public float getY() {
		return fy;
	}

	public void draw(Canvas canvas) {
		if(bVisible)
			canvas.drawCircle(fx, fy, fSize, pStyle);
	}


}
