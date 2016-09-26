package pe.hgs.truler.phase;

/** 페이즈 관련 신호. 페이즈 간 구분이나 각 페이즈에서 리턴 값 전송 시 사용되는 코드
 * Created by ysb06 on 2016-08-07.
 */
public interface Phase {

    int PHASE_INPUT_INFORMATION = 0;
    int PHASE_CAMERA = 1;
    int PHASE_IMAGE_LOADING = 2;
    int PHASE_JOINT_SELECTION = 3;
    int PHASE_REVISION = 4;
    int PHASE_RESULT = 5;
    int PHASE_MAIN = 6;
}
