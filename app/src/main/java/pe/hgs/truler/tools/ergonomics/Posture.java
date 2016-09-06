package pe.hgs.truler.tools.ergonomics;

import pe.hgs.truler.tools.Logger;
import pe.hgs.truler.tools.ergonomics.angle.BackAngle;
import pe.hgs.truler.tools.ergonomics.angle.ElbowAngle;
import pe.hgs.truler.tools.ergonomics.angle.FootAngle;
import pe.hgs.truler.tools.ergonomics.angle.KneeAngle;
import pe.hgs.truler.tools.ergonomics.angle.ShoulderAngle;

/**
 * Created by ysb06 on 2016-08-19.
 */
public class Posture {

	public enum PostureType { UPPER, LOWER }

	private PostureType pType = PostureType.UPPER;
	private String pName = "";

	public Posture(PostureType type) {
		pType = type;
		switch (pType) {
			case UPPER:
				pName = PostureRiskAnalyzer.NAME_UPPER_POSTURE[0];
				break;
			case LOWER:
				pName = PostureRiskAnalyzer.NAME_LOWER_POSTURE[0];
				break;
		}
	}

	protected Posture(PostureType type, String name) {
		pType = type;
		pName = name;
	}

	public Posture(BackAngle backAngle, ShoulderAngle shoulderAngle, ElbowAngle elbowAngle) {
		pType = PostureType.UPPER;
		pName = backAngle + "-" + shoulderAngle + "-" + elbowAngle;
	}

	public Posture(KneeAngle kneeAngle, FootAngle footAngle) {
		pType = PostureType.LOWER;

		switch (footAngle) {
			case F120:
				pName = "SC0";
				break;
			case F90:			//Sitting
				switch (kneeAngle) {
					case K30:
					case K60:
						pName = "SC20";
						break;
					case K90:
						pName = "SC40";
						break;
					case K180:
						pName = "STD";
						break;
					default:
						Logger.warn("앉는 자세에서 알 수 없는 무릎 각도");
						pName = "SC40";
						break;
				}
				break;
			case F60:		//Standing or Squatting
				switch (kneeAngle) {
					case K180:
						Logger.warn("선 자세에서 알 수 없는 무릎 각도");
						pName = "STD";
						break;
					case K150:
						pName = "KF150";
						break;
					case K120:
						pName = "KF120";
						break;
					case K90:
						pName = "KF90";
						break;
					case K60:
						pName = "KF60";
						break;
					case K30:
						pName = "KF30";
						break;
				}
				break;
			case F0:
				if(kneeAngle == KneeAngle.K30)
					pName = "KF30C";
				else
					pName = "KNL_1";
				break;
		}
	}

	public PostureType getType() {
		return pType;
	}

	public void setType(PostureType pType) {
		this.pType = pType;
	}

	public String getName() {
		return pName;
	}

	public void setName(String pName) {
		this.pName = pName;
	}
}
