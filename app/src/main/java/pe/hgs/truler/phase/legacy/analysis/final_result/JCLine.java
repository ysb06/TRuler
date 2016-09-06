package pe.hgs.truler.phase.legacy.analysis.final_result;

import java.util.NoSuchElementException;

/**
 * Created by ysb06 on 2016-08-09.
 */
public class JCLine {

	private int iJoint1 = 0;
	private double dx1 = 0;
	private double dy1 = 0;
	private int iJoint2 = 0;
	private double dx2 = 0;
	private double dy2 = 0;

	private double dLength = 0;

	public JCLine(int joint1, int joint2, float[] xList, float[] yList) {
		iJoint1 = joint1;
		dx1 = (double) xList[joint1];
		dy1 = (double) yList[joint1];
		iJoint2 = joint2;
		dx2 = (double) xList[joint2];
		dy2 = (double) yList[joint2];

		dLength = Math.sqrt(Math.pow((dx2 - dx1), 2) + Math.pow((dy2 - dy1), 2));
		//Logger.debug("JC Line is initialized : [" + iJoint1+"](" + dx1 + ", " + dy1 + ")  [" + iJoint2 + "](" + dx2 + ", " + dy2 + ") -> " + dLength);
	}

	public int getPoint1() {
		return iJoint1;
	}

	public int getPoint2() {
		return iJoint2;
	}

	public double getPointX(int point) throws NoSuchElementException {
		if(iJoint1 == point)
			return dx1;
		else if(iJoint2 == point)
			return dx2;
		else
			throw new NoSuchElementException();
	}

	public double getPointY(int point) throws NoSuchElementException {
		if(iJoint1 == point)
			return dy1;
		else if(iJoint2 == point)
			return dy2;
		else
			throw new NoSuchElementException();
	}

	public double getPoint1X() {
		return dx1;
	}

	public double getPoint2X() {
		return dx2;
	}

	public double getPoint1Y() {
		return dy1;
	}

	public double getPoint2Y() {
		return dy2;
	}

}
