package locomotor.components.logging;

import java.util.TreeMap;

/**
 * Singleton class for handling error that can be raised while performing actions.
 */
public class ErrorHandler {

	/**
	 * Singleton error handler object, with lazy instanciation.
	 */
	private static ErrorHandler _eh = new ErrorHandler();
	
	/**
	 * The context for each thread (request).
	 */
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
	public static ErrorHandler getInstance() {
		return _eh;
	}

	/**
	 * Add a thread/request.
	 *
	 * @param      pid   The pid of the thread running the request
	 *
	 * @return     The ErrorContext object, our custom stacktrace
	 */
	private ErrorContext add(long pid) {
		ErrorContext ec = new ErrorContext();
		_context.put(pid, ec);
		return ec;
	}

	/**
	 * Get a thread/request.
	 *
	 * @param      pid   The pid of the thread running the request
	 *
	 * @return     The ErrorContext object, our custom stacktrace
	 */
	public ErrorContext get(long pid) {
		ErrorContext ec = _context.get(pid);
		// already exist
		if (ec != null) {
			return ec;
		}
		// nope, then create
		return add(pid);
	}

	/**
	 * Remove the thread/request.
	 *
	 * @param      pid   The pid of the thread running the request
	 */
	public void remove(long pid) {
		_context.remove(pid);
	}

}