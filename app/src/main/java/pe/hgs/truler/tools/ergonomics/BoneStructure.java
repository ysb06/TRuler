package pe.hgs.truler.tools.ergonomics;

import pe.hgs.truler.tools.Logger;
import pe.hgs.truler.tools.ergonomics.angle.BackAngle;
import pe.hgs.truler.tools.ergonomics.angle.ElbowAngle;
import pe.hgs.truler.tools.ergonomics.angle.FootAngle;
import pe.hgs.truler.tools.ergonomics.angle.KneeAngle;
import pe.hgs.truler.tools.ergonomics.angle.ShoulderAngle;
import pe.hgs.truler.tools.ergonomics.angle.WaistAngle;

/** 평가 대상을 나타내는 객체, 각 Joint 들이 이루는 각도 그리고 이러한 각도에 따른 자세에 대한 정보 포함.
 * Created by ysb06 on 2016-08-15.
 */
public class BoneStructure {

	public static final int HE_SH = 0;
	public static final int SH_EL = 1;
	public static final int EL_WR = 2;
	public static final int SH_WA = 3;
	public static final int WA_KN = 4;
	public static final int KN_FO = 5;
	public static final int FO_GR = 6;
	public static final int WA_GR = 7;

	public static final int HE_SH_EL = 0;
	public static final int SH_EL_WR = 1;
	public static final int EL_SH_WA = 2;
	public static final int SH_WA_KN = 3;
	public static final int WA_KN_FO = 4;
	public static final int KN_FO_GR = 5;
	public static final int SH_WA_GR = 6;

	private Bone[] bones;

	private BackAngle backAngle = BackAngle.B0;
	private ShoulderAngle shoulderAngle = ShoulderAngle.S0;
	private ElbowAngle elbowAngle = ElbowAngle.E0;

	private WaistAngle waistAngle = WaistAngle.W120;
	private KneeAngle kneeAngle = KneeAngle.K30;
	private FootAngle footAngle = FootAngle.F0;

	private Posture postureUpper;
	private Posture postureLower;

	/** 각 Joint부분들이 이루는 각도 계산을 위한 클래스
	 *
	 * @param joints 각 신체 부위 Joint 객체 배열
	 */
	public BoneStructure(Joint[] joints) {
		bones = new Bone[8];

		bones[HE_SH] = new Bone(joints[0], joints[1]);
		bones[SH_EL] = new Bone(joints[1], joints[2]);
		bones[EL_WR] = new Bone(joints[2], joints[3]);
		bones[SH_WA] = new Bone(joints[1], joints[4]);
		bones[WA_KN] = new Bone(joints[4], joints[5]);
		bones[KN_FO] = new Bone(joints[5], joints[6]);

		Joint jGround = new Joint(joints[6].getX() + 10, joints[6].getY());
		bones[FO_GR] = new Bone(joints[6], jGround);		//발과 땅이 이루는 직선

		Joint jGround2 = new Joint(joints[4].getX() + 10, joints[6].getY());
		bones[WA_GR] = new Bone(joints[4], jGround2);		//허리와 땅이 이루는 직선

		analyzeUpperPosture();
		analyzeLowerPosture();
		Logger.debug("<Back " + backAngle + ">, <Shoulder " + shoulderAngle + ">, <Elbow " + elbowAngle + ">, <Waist " + waistAngle + ">, <Knee " + kneeAngle + ">, <Foot " + footAngle + ">");
		postureUpper = new Posture(backAngle, shoulderAngle, elbowAngle);
		postureLower = new Posture(waistAngle, kneeAngle, footAngle);

		Logger.debug("Upper -> " + postureUpper.getName() + ",\tLower -> " + postureLower.getName());
	}

	public double getAngle(int angle) {
		switch (angle) {
			case HE_SH_EL:
				return bones[HE_SH].getAngleWith(bones[SH_EL]);
			case SH_EL_WR:
				return bones[SH_EL].getAngleWith(bones[EL_WR]);
			case EL_SH_WA:
				return bones[SH_EL].getAngleWith(bones[SH_WA]);
			case SH_WA_KN:
				return bones[SH_WA].getAngleWith(bones[WA_KN]);
			case WA_KN_FO:
				return bones[WA_KN].getAngleWith(bones[KN_FO]);
			case KN_FO_GR:
				return bones[KN_FO].getAngleWith(bones[FO_GR]);
			case SH_WA_GR:
				return bones[SH_WA].getAngleWith(bones[WA_GR]);
			default:
				Logger.warn("No such defined angle");
				return 0;
		}
	}

	public Posture getPosture(PostureType type) {
		switch (type) {
			case UPPER:
				return postureUpper;
			case LOWER:
				return postureLower;
			default:
				return null;
		}
	}

	/**	상지 자세 측정 및 기록 */
	private void analyzeUpperPosture() {
		//---- 상지 측정 ----//
		//---- 등, 허리 ----//
		double backAngle = Math.abs(getAngle(SH_WA_GR));
		if(backAngle < 5 * Math.PI / 8) {
			this.backAngle = BackAngle.B90;
		} else if(backAngle >= 5 * Math.PI / 8 && backAngle < 7 * Math.PI / 8) {
			this.backAngle = BackAngle.B45;
		} else if(backAngle >= 7 * Math.PI / 8 && backAngle < 3 * Math.PI / 2) {
			this.backAngle = BackAngle.B0;
		} else {
			Logger.error("Fail to analyze elbow posture");
			this.backAngle = null;
		}

		//---- 어깨 ----//
		double shoulderAngle = Math.abs(getAngle(EL_SH_WA));
		if(shoulderAngle < Math.PI / 8) {
			this.shoulderAngle = ShoulderAngle.S0;
		} else if(shoulderAngle >= Math.PI / 8 && shoulderAngle < 3 * Math.PI / 8) {
			this.shoulderAngle = ShoulderAngle.S45;
		} else if(shoulderAngle >= 3 * Math.PI / 8 && shoulderAngle < 5 * Math.PI / 8) {
			this.shoulderAngle = ShoulderAngle.S90;
		} else if(shoulderAngle >= 5 * Math.PI / 8 && shoulderAngle < Math.PI) {
			this.shoulderAngle = ShoulderAngle.S120;
		} else {
			Logger.error("Fail to analyze shoulder posture");
			this.shoulderAngle = null;
		}

		//---- 팔꿈치 ----//
		double elbowAngle = Math.abs(getAngle(SH_EL_WR));
		if(elbowAngle < 5 * Math.PI / 8) {
			this.elbowAngle = ElbowAngle.E90;
		} else if(elbowAngle >= 5 * Math.PI / 8 && elbowAngle < 7 * Math.PI / 8) {
			this.elbowAngle = ElbowAngle.E45;
		} else if(elbowAngle >= 7 * Math.PI / 8 && elbowAngle < 3 * Math.PI / 2) {
			this.elbowAngle = ElbowAngle.E0;
		} else {
			Logger.error("Fail to analyze elbow posture");
			this.elbowAngle = null;
		}
	}

	/**	하지 자세 측정 및 기록 */
	private void analyzeLowerPosture() {
		double waistAngle = Math.abs(getAngle(SH_WA_KN));
		double kneeAngle = Math.abs(getAngle(WA_KN_FO));
		double footAngle = Math.abs(getAngle(KN_FO_GR));

		//허리 각도
		if(waistAngle < 5 * Math.PI / 24) {
			this.waistAngle = WaistAngle.W30;
		} else if(waistAngle >= 5 * Math.PI / 24 && waistAngle <  7 * Math.PI / 24) {
			this.waistAngle = WaistAngle.W45;
		} else if(waistAngle >= 7 * Math.PI / 24 && waistAngle < 5 * Math.PI / 12) {
			this.waistAngle = WaistAngle.W60;
		} else if(waistAngle >= 5 * Math.PI / 12 && waistAngle < 7 * Math.PI / 12) {
			this.waistAngle = WaistAngle.W90;
		} else if(waistAngle >= 7 * Math.PI / 12 && waistAngle < 5 * Math.PI / 6) {
			this.waistAngle = WaistAngle.W120;
		} else if(waistAngle >= 5 * Math.PI / 6 && waistAngle < 15 * Math.PI / 12) {
			this.waistAngle = WaistAngle.W180;
		} else {
			this.waistAngle = null;
		}

		//무릎
		if(kneeAngle < Math.PI / 4) {
			this.kneeAngle = KneeAngle.K30;
		} else if(kneeAngle >= Math.PI / 4 && kneeAngle < 5 * Math.PI / 12) {
			this.kneeAngle = KneeAngle.K60;
		} else if(kneeAngle >= 5 * Math.PI / 12 && kneeAngle < 7 * Math.PI / 12) {
			this.kneeAngle = KneeAngle.K90;
		} else if(kneeAngle >= 7 * Math.PI / 12 && kneeAngle < 9 * Math.PI / 12) {
			this.kneeAngle = KneeAngle.K120;
		} else if(kneeAngle >= 9 * Math.PI / 12 && kneeAngle < 11 * Math.PI / 12) {
			this.kneeAngle = KneeAngle.K150;
		} else if(kneeAngle >= 11 * Math.PI / 12 && kneeAngle < 15 * Math.PI / 12) {
			this.kneeAngle = KneeAngle.K180;
		} else {
			this.kneeAngle = null;
		}

		//지면과 다리의 각도
		if(footAngle < Math.PI / 6) {
			this.footAngle = FootAngle.F0;
		} else if(footAngle >= Math.PI / 6 && footAngle < 5 * Math.PI / 12) {
			this.footAngle = FootAngle.F60;
		} else if(footAngle >= 5 * Math.PI / 12 && footAngle < 7 * Math.PI / 12) {
			this.footAngle = FootAngle.F90;
		} else if(footAngle >= 7 * Math.PI / 12 && footAngle <= Math.PI) {
			this.footAngle = FootAngle.F120;
		}

	}
}
