package locomotor.components.logging;

import java.util.ArrayDeque;

import locomotor.components.Pair;

/**
 * Contains the path that lead to program to the current point.
 * If an error is generated, the stacktrace can show the context that possibly raised that error.
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
	 * Push a trace on top of the stacktrace.
	 *
	 * @param      method  The method
	 * @param      log     The log
	 */
	public void push(String method, Logging log) {
		_isError = _isError || log._errorState;
		_stackTrace.push(new Pair<String, Logging>(method, log));
	}

	/**
	 * Remove the last trace from the stacktrace.
	 */
	public Pair<String, Logging> pop() {
		if (!_stackTrace.isEmpty()) {
			return _stackTrace.pop();
		}
		return null;
	}

	/**
	 * Determines if empty.
	 *
	 * @return     True if empty, False otherwise.
	 */
	public boolean isEmpty() {
		return  _stackTrace.isEmpty();
	}

	/**
	 * Determines if error.
	 *
	 * @return     True if error, False otherwise.
	 */
	protected boolean isError() {
		return _isError;
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