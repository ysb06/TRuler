package pe.hgs.truler.phase.analysis.final_result;

/**
 * Created by ysb06 on 2016-08-09.
 */
public class NotAdjoinedLineException extends Exception {

	public NotAdjoinedLineException() {
		super("두 선이 인접하지 않고 있습니다. 서로 붙어 있는 선을 선택해야 합니다.");
	}
}
