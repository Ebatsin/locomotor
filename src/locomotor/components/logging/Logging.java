package locomotor.components.logging;

/**
 * Simply wrap logging information (method, parameters or message).
 */
public class Logging {
	
	/**
	 * The general message to be displayed to the user.
	 */
	private String _message;

	/**
	 * Is there an error.	
	 */
	protected boolean _errorState;

	/**
	 * The context the general message.
	 */
	private String _context;

	/**
	 * Constructs the logging object.
	 *
	 * @param      message     The message
	 * @param      errorState  The error state
	 * @param      context     The context
	 */
	public Logging(String message, boolean errorState, String context) {

		_message = message;
		_errorState = errorState;
		_context = context;

	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return     String representation of the object.
	 */
	public String toString() {
		return _message;
	}

}