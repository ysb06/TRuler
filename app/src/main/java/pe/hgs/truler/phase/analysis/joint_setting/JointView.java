package pe.hgs.truler.phase.analysis.joint_setting;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import pe.hgs.truler.tools.Logger;

/** 관절 선택 부분 표시 View
 * Created by ysb06 on 2016-08-08.
 */
public class JointView extends View implements View.OnTouchListener {

	private ArrayList<JointViewListener> listeners;
	private Joint[] jointPoint;
	private int iRequestedPoint = 0;

	public JointView(Context context) {
		super(context);
		listeners = new ArrayList<>();
	}

	public JointView(Context context, AttributeSet attrs) {
		super(context, attrs);
		listeners = new ArrayList<>();
	}

	public JointView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		listeners = new ArrayList<>();
	}

	public void setListener(JointViewListener listener) {
		listeners.add(listener);
	}

	public void setJointPoint(Joint[] joints) {
		jointPoint = joints;
	}

	public void requestJointPoint(int joint) {
		iRequestedPoint = joint;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(jointPoint != null)
			for(Joint joint : jointPoint) {
				joint.draw(canvas);
			}
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
			Logger.debug("Event : " + motionEvent.getX() + ", " + motionEvent.getY());
			//Todo: 터치와 실제 그려지는 부분에 미세한 차이가 있음. 각도 측정에는 문제가 없지만 추후 문제 방지를 위해 조정이 필요

			if (iRequestedPoint < jointPoint.length) {
				jointPoint[iRequestedPoint].setPosition(motionEvent.getX(), motionEvent.getY());
				jointPoint[iRequestedPoint].setVisible(true);
			}

			this.invalidate();
			for (JointViewListener listener : listeners) {
				listener.onJointSettingCompleted();
			}
		}
		return true;
	}
}