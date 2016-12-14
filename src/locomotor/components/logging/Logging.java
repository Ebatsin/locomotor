package locomotor.components.logging;

public class Logging {
	
	private String _message;

	private boolean _errorState;

	private String _context;

	public Logging(String message, boolean errorState, String context) {

		_message = message;
		_errorState = errorState;
		_context = context;

	}

	public String toString() {
		return "Message: " + _message + "\n" + "Context: " + _context;
	}

}