package locomotor.components.logging;

import java.util.ArrayDeque;

import locomotor.components.Pair;

/**
 * @todo.
 */
public class ErrorContext {
	
	/**
	 * Is there an error?
	 */
	private boolean _isError;

	/**
	 * Custom stacktrace.
	 */
	private ArrayDeque<Pair<String, Logging>> _stackTrace;

	public ErrorContext() {

		_isError = false;
		_stackTrace = new ArrayDeque<Pair<String, Logging>>();

	}

	public void add(String method, Logging log) {
		// @todo: extract boolean value from Logging and OR with _isError
		_isError = _isError || log._errorState;
		_stackTrace.add(new Pair<String, Logging>(method, log));
	}

	public String toString() {
		String s = "";
		for (Pair<String, Logging> trace : _stackTrace) {
			s += "Method: " + trace.getLeft() + "\n";
			s += trace.getRight().toString() + "\n\n";
		}
		return s;
	}	

}