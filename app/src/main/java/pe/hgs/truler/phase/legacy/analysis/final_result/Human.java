package pe.hgs.truler.phase.legacy.analysis.final_result;

import pe.hgs.truler.phase.legacy.analysis.JointSetting;

/**
 * Created by ysb06 on 2016-08-09.
 */
public class Human {

	public static final int HE_SH = 0;
	public static final int SH_EL = 1;
	public static final int EL_WR = 2;
	public static final int SH_WA = 3;
	public static final int WA_KN = 4;
	public static final int KN_FO = 5;

	public static final int HE_SH_EL = 0;
	public static final int SH_EL_WR = 1;
	public static final int EL_SH_WA = 2;
	public static final int SH_WA_KN = 3;
	public static final int WA_KN_FO = 4;

	private JCLine[] jclBones;
	private JCAngle[] jcaJoints;


	public Human(float[] xList, float[] yList) {
		jclBones = new JCLine[6];
		jclBones[HE_SH] = new JCLine(JointSetting.HEAD, JointSetting.SHOULDER, xList, yList);
		jclBones[SH_EL] = new JCLine(JointSetting.SHOULDER, JointSetting.ELBOW, xList, yList);
		jclBones[EL_WR] = new JCLine(JointSetting.ELBOW, JointSetting.WRIST, xList, yList);
		jclBones[SH_WA] = new JCLine(JointSetting.SHOULDER, JointSetting.WAIST, xList, yList);
		jclBones[WA_KN] = new JCLine(JointSetting.WAIST, JointSetting.KNEE, xList, yList);
		jclBones[KN_FO] = new JCLine(JointSetting.KNEE, JointSetting.FOOT, xList, yList);

		jcaJoints = new JCAngle[5];
		try {
			jcaJoints[HE_SH_EL] = new JCAngle(jclBones[HE_SH], jclBones[SH_EL]);
			jcaJoints[SH_EL_WR] = new JCAngle(jclBones[SH_EL], jclBones[EL_WR]);
			jcaJoints[EL_SH_WA] = new JCAngle(jclBones[SH_EL], jclBones[SH_WA]);
			jcaJoints[SH_WA_KN] = new JCAngle(jclBones[SH_WA], jclBones[WA_KN]);
			jcaJoints[WA_KN_FO] = new JCAngle(jclBones[WA_KN], jclBones[KN_FO]);
		} catch (NotAdjoinedLineException e) {
			e.printStackTrace();
		}
	}

	public double[] getAngles() {
		double[] temp = new double[jcaJoints.length];
		for(int i = 0; i < temp.length; i++) {
			temp[i] = jcaJoints[i].getAngle();
		}

		return temp;
	}
}
