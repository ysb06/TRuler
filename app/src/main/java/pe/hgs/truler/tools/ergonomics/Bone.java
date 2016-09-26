package pe.hgs.truler.tools.ergonomics;

import pe.hgs.truler.tools.Logger;

/**
 * Created by ysb06 on 2016-08-15.
 */
public class Bone {

	protected Joint joint1;
	protected Joint joint2;

	public Bone(Joint joint1, Joint joint2) {
		this.joint1 = joint1;
		this.joint2 = joint2;
	}

	private Joint getBinedJoint(Bone bone) {
		if(this.equals(bone)) {
			Logger.warn("Comparing same bone");
			return joint1;
		}
		if(joint1.equals(bone.joint1) || joint1.equals(bone.joint2)) {
			return joint1;
		} else if(joint2.equals(bone.joint1) || joint2.equals(bone.joint2)) {
			return joint2;
		} else {
			Logger.warn("Bones are not adjoined");
			return null;
		}
	}

	public double getAngleWith(Bone bone) {

		Joint j1;
		Joint midJ = getBinedJoint(bone);
		Joint j2;

		if(midJ == null) {		//뼈들이 결합하지 않음
			return 0;
		}
		j1 = getOtherSideJoint(midJ);
		j2 = bone.getOtherSideJoint(midJ);

		double x1 = j1.getX() - midJ.getX();
		double y1 = j1.getY() - midJ.getY();
		double x2 = j2.getX() - midJ.getX();
		double y2 = j2.getY() - midJ.getY();

		return Math.atan2(y1 * x2 - x1 * y2, x1 * x2 + y1 * y2);
	}

	/** 길이 계산 반환
	 *
	 * @return 주 Joint 사이의 거리
	 */
	public double getLength() {
		return Math.sqrt(Math.pow((joint2.getX() - joint1.getX()), 2) + Math.pow((joint2.getY() - joint1.getY()), 2));
	}

	public Joint getOtherSideJoint(Joint joint) {
		if(joint.equals(joint1))
			return joint2;
		else if(joint.equals(joint2))
			return joint1;
		else {
			Logger.warn("The joint is not part of this");
			return null;
		}
	}
}
