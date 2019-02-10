package pe.hgs.truler.tools.ergonomics;

/**
 * Created by ysb06 on 2016-11-16.
 */
public class ErgoDatabase {
	private static ErgoDatabase ourInstance = new ErgoDatabase();

	public static ErgoDatabase getInstance() {
		return ourInstance;
	}

	private ErgoDatabase() {
	}
}
