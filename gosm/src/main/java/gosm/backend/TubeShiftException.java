package gosm.backend;

public class TubeShiftException extends RuntimeException {

	public TubeShiftException(String msg) {
		super(msg);
	}
	
	public TubeShiftException(String msg, Throwable e) {
		super(msg,e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1511634871883299976L;

}
