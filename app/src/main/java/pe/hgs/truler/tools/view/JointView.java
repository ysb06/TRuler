package pe.hgs.truler.tools.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import pe.hgs.truler.tools.Logger;
import pe.hgs.truler.tools.container.Joint;

/** 관절 선택 부분 표시 View
 * Created by ysb06 on 2016-08-08.
 */
public class JointView extends View implements View.OnTouchListener {

	private ArrayList<JointViewListener> listeners;
	private ArrayList<Joint> joints;
	private boolean isInputAvailable = true;

	public JointView(Context context) {
		super(context);
		listeners = new ArrayList<>();
		joints = new ArrayList<>();
		setOnTouchListener(this);

	}

	public JointView(Context context, AttributeSet attrs) {
		super(context, attrs);
		listeners = new ArrayList<>();
		joints = new ArrayList<>();
		setOnTouchListener(this);
	}

	public JointView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		listeners = new ArrayList<>();
		joints = new ArrayList<>();
		setOnTouchListener(this);
	}

	public void addListener(JointViewListener listener) {
		listeners.add(listener);
	}

	public void setInputAvailable(boolean available) {
		isInputAvailable = available;
	}

	public Joint[] getJointsList() {
		Joint[] list = joints.toArray(new Joint[0]);
		return list;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(joints != null)
			for(Joint joint : joints) {
				if(joint != null)
					joint.draw(canvas);
			}
	}

	@Override
	public boolean onTouch(View view, MotionEvent motionEvent) {
		if(motionEvent.getAction() == MotionEvent.ACTION_UP && isInputAvailable) {
			Logger.debug("Event : " + motionEvent.getX() + ", " + motionEvent.getY());
			//Todo: 터치와 실제 그려지는 부분에 미세한 차이가 있음. 각도 측정에는 문제가 없지만 추후 문제 방지를 위해 조정이 필요

			Joint joint = new Joint(motionEvent.getX(), motionEvent.getY());
			joints.add(joint);

			this.invalidate();
			for (JointViewListener listener : listeners) {
				listener.onJointSelected(joint, joints.size());
			}
		}
		return true;
	}
}