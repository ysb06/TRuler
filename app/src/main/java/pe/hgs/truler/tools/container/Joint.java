package pe.hgs.truler.tools.container;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ysb06 on 2016-08-15.
 */
public class Joint implements Parcelable {

	public static final Creator<Joint> CREATOR = new Creator<Joint>() {
		@Override
		public Joint createFromParcel(Parcel in) {
			return new Joint(in);
		}

		@Override
		public Joint[] newArray(int size) {
			return new Joint[size];
		}
	};

	public enum JPoint { HEAD, SHOULDER, ELBOW, WRIST, WAIST, KNEE, FOOT }

	private float fx = 0;
	private float fy = 0;
	private boolean bVisible = true;
	private float fSize;
	private JPoint jPoint;

	private Paint pStyle;

	public Joint(float x, float y) {
		fx = x;
		fy = y;
		fSize = 20;
		pStyle = new Paint();
		pStyle.setAntiAlias(true);
		pStyle.setStyle(Paint.Style.FILL);
		pStyle.setColor(Color.argb(255, 255, 0, 255));
	}

	private Joint(Parcel in) {
		fx = in.readFloat();
		fy = in.readFloat();
		bVisible = in.readByte() != 0;
		fSize = in.readFloat();

		pStyle = new Paint();
		pStyle.setAntiAlias(true);
		pStyle.setStyle(Paint.Style.FILL);
		pStyle.setColor(Color.argb(255, 255, 0, 255));
	}

	public void draw(Canvas canvas) {
		if(bVisible)
			canvas.drawCircle(fx, fy, fSize, pStyle);
	}

	public float getX() {
		return fx;
	}

	public float getY() {
		return fy;
	}

	public void setPoint(JPoint point) {
		jPoint = point;
	}

	public JPoint getPoint() {
		return jPoint;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeFloat(fx);
		parcel.writeFloat(fy);
		if(bVisible)
			parcel.writeByte((byte) 1);
		else
			parcel.writeByte((byte) 0);
		parcel.writeFloat(fSize);
	}
}
