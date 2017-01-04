package locomotor.components.logging;

import java.util.ArrayDeque;

import locomotor.components.Pair;

/**
 * @todo.
 */
public class ErrorContext {
	
	/**
	 * Is there an error.
	 */
	private boolean _isError;

	/**
	 * Custom stacktrace.
	 */
	private ArrayDeque<Pair<String, Logging>> _stackTrace;

	/**
	 * Constructs the object.
	 */
	public ErrorContext() {

		_isError = false;
		_stackTrace = new ArrayDeque<Pair<String, Logging>>();

	}

	/**
	 * Add a trace in the stacktrace.
	 *
	 * @param      method  The method
	 * @param      log     The log
	 */
	public void add(String method, Logging log) {
		_isError = _isError || log._errorState;
		_stackTrace.add(new Pair<String, Logging>(method, log));
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return     String representation of the object.
	 */
	public String toString() {
		String stringTrace = "";
		for (Pair<String, Logging> trace : _stackTrace) {
			stringTrace += "Method: " + trace.getLeft() + "\n";
			stringTrace += trace.getRight().toString() + "\n\n";
		}
		return stringTrace;
	}	

}