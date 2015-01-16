/**
 * 
 */
package parser.exceptions;

/**
 * @author pavellitvinko
 *
 */
public class RTFReaderException extends Exception {

	private static final long serialVersionUID = -1653267578409049421L;

	public RTFReaderException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public RTFReaderException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public RTFReaderException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RTFReaderException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public RTFReaderException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
