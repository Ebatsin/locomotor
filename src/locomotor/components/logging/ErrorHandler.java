package locomotor.components.logging;

import java.lang.Thread;
import java.util.TreeMap;

import locomotor.components.Pair;

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
	 * @return     The ErrorContext object, our custom stacktrace
	 */
	private ErrorContext add() {
		ErrorContext ec = new ErrorContext();
		_context.put(Thread.currentThread().getId(), ec);
		return ec;
	}

	/**
	 * Get a thread/request.
	 *
	 * @return     The ErrorContext object, our custom stacktrace
	 */
	public ErrorContext get() {
		ErrorContext ec = _context.get(Thread.currentThread().getId());
		// already exist
		if (ec != null) {
			return ec;
		}
		// nope, then create
		return add();
	}

	/**
	 * Remove the current thread/request.
	 */
	public void remove() {
		_context.remove(Thread.currentThread().getId());
	}

	/**
	 * Push a log onto the stacktrace.
	 *
	 * @param      method   The method
	 * @param      isError  Indicates if error
	 * @param      general  The general message
	 * @param      details  The details message
	 */
	public void push(String method, boolean isError, String general, String details) {
		ErrorContext ec = _context.get(Thread.currentThread().getId());
		ec.push(method, new Logging(general, isError, details));
	}

	/**
	 * Pop the last log from the stacktrace.
	 *
	 * @return     The message/details log couple
	 */
	public Pair<String, Logging> pop() {
		ErrorContext ec = _context.get(Thread.currentThread().getId());
		if (!ec.isEmpty()) {
			return ec.pop();
		}
		return null;
	}

	/**
	 * Determines if error.
	 *
	 * @return     True if error, False otherwise.
	 */
	public boolean isError() {
		ErrorContext ec = _context.get(Thread.currentThread().getId());
		return ec.isError();
	}

}