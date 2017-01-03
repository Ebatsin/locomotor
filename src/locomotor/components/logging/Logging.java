package locomotor.components.logging;

/**
 * @todo
 */
public class Logging {
	
	/**
	 * The general message to be displayed to the user.
	 */
	private String _message;

	/**
	 * Error or not?
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

	public String toString() {
		return "Message: " + _message + "\n" + "Context: " + _context;
	}

}