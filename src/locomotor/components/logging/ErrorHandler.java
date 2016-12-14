package locomotor.components.logging;

import java.util.TreeMap;

public class ErrorHandler {

	private static ErrorHandler eh = null;
	
	private TreeMap<Long, ErrorContext> _context;

	/**
	 * Private constructor to prevent any other class to instantiate.
	 */
	private ErrorHandler() {
		_context = new TreeMap<Long, ErrorContext>();
	}

	/**
	 * Gets the instance.
	 *
	 * @return     The instance.
	 */
	public static synchronized ErrorHandler getInstance() {
		if(eh == null) {
			eh = new ErrorHandler();
		}
		return eh;
	}

	public ErrorContext add(long pid) {
		ErrorContext ec = new ErrorContext();
		_context.put(pid, ec);
		return ec;
	}

	public void remove(long pid) {
		_context.remove(pid);
	}

}