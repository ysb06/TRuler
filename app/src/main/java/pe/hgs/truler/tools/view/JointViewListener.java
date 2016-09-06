package pe.hgs.truler.tools.view;

import pe.hgs.truler.tools.container.Joint;

/**
 * Created by ysb06 on 2016-08-08.
 */
public interface JointViewListener {
	@Deprecated
	void onJointSettingCompleted();
	void onJointSelected(Joint joint, int count);
}
