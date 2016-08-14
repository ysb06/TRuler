package pe.hgs.truler.phase;

/** 페이즈 관련 신호. 페이즈 간 구분이나 각 페이즈에서 리턴 값 전송 시 사용되는 코드
 * Created by ysb06 on 2016-08-07.
 */
public interface PhaseSignal {

    int PHASE_ANL_PROFILE_SELECTING = 0;
    int PHASE_ANL_CAMERA = 1;
    int PHASE_ANL_IMAGE_LOADING = 2;
    int PHASE_ANL_JOINT = 3;
    int PHASE_ANL_SUB_ASSESSMENT = 4;
    int PHASE_ANL_RESULT = 5;
}
