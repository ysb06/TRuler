package pe.hgs.truler.phase.analysis.final_result;

/**
 * Created by ysb06 on 2016-08-09.
 */
public class JCAngle {

	private JCLine jcl1;
	private JCLine jcl2;

	private int iMidJoint;
	private int iSideJoint1;
	private int iSideJoint2;

	private double dRadian = 0;

	public JCAngle(JCLine jcl1, JCLine jcl2) throws NotAdjoinedLineException {
		this.jcl1 = jcl1;
		this.jcl2 = jcl2;

		analyseJoint();
		dRadian = getMidAngle();
	}

	private void analyseJoint() throws NotAdjoinedLineException {
		int[] iJPoint = { jcl1.getPoint1(), jcl1.getPoint2(), jcl2.getPoint1(), jcl2.getPoint2() };

		if(iJPoint[0] == iJPoint[2]) {
			iMidJoint = iJPoint[0];
			iSideJoint1 = iJPoint[1];
			iSideJoint2 = iJPoint[3];
		} else if(iJPoint[0] == iJPoint[3]) {
			iMidJoint = iJPoint[0];
			iSideJoint1 = iJPoint[1];
			iSideJoint2 = iJPoint[2];
		} else if(iJPoint[1] == iJPoint[2]) {
			iMidJoint = iJPoint[1];
			iSideJoint1 = iJPoint[0];
			iSideJoint2 = iJPoint[3];
		} else if(iJPoint[1] == iJPoint[3]) {
			iMidJoint = iJPoint[1];
			iSideJoint1 = iJPoint[0];
			iSideJoint2 = iJPoint[2];
		} else {
			throw new NotAdjoinedLineException();
		}
	}

	/** atan2를 이용한 각도 계산, http://tibyte.kr/217참조
	 *
	 * @return
	 */
	private double getMidAngle() {
		double x1 = jcl1.getPointX(iSideJoint1) - jcl1.getPointX(iMidJoint);
		double y1 = jcl1.getPointY(iSideJoint1) - jcl1.getPointY(iMidJoint);
		double x2 = jcl2.getPointX(iSideJoint2) - jcl1.getPointX(iMidJoint);
		double y2 = jcl2.getPointY(iSideJoint2) - jcl1.getPointY(iMidJoint);;

		return Math.atan2(y1 * x2 - x1 * y2, x1 * x2 + y1 * y2);
	}

	/** 계산된 각도 반환
	 *
	 * @return 두 선이 이루는 각도(Radian)
	 */
	public double getAngle() {
		return dRadian;
	}
}
