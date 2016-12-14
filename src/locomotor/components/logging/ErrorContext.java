package locomotor.components.logging;

import java.util.ArrayDeque;

import locomotor.components.Pair;

public class ErrorContext {
	
	private boolean _isError;

	private ArrayDeque<Pair<String, Logging>> _stackTrace;

	public ErrorContext() {

		_isError = false;
		_stackTrace = new ArrayDeque<Pair<String, Logging>>();

	}

	public void add(String method, Logging log) {
		// @todo: extract boolean value from Logging and OR with _isError
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