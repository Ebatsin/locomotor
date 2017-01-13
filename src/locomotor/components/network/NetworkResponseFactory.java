package locomotor.components.network;

import com.sun.net.httpserver.HttpExchange;

/**
 * Front-end to create network response objects.
 */
public class NetworkResponseFactory {
	
	HttpExchange _exchange;

	/**
	 * Constructs the object.
	 *
	 * @param      exchange  The exchange
	 */
	public NetworkResponseFactory(HttpExchange exchange) {
		_exchange = exchange;
	}

	/**
	 * Gets the binary context.
	 *
	 * @return     The binary context.
	 */
	public NetworkBinaryResponse getBinaryContext() {
		return new NetworkBinaryResponse(_exchange);
	}

	/**
	 * Gets the json context.
	 *
	 * @return     The json context.
	 */
	public NetworkJsonResponse getJsonContext() {
		return new NetworkJsonResponse(_exchange);
	}
}