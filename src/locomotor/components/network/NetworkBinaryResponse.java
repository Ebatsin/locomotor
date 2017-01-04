package locomotor.components.network;

import com.sun.net.httpserver.HttpExchange;

/**
 * @todo.
 */
public class NetworkBinaryResponse extends NetworkResponse {
	
	/**
	 * @todo.
	 */
	HttpExchange _exchange;

	/**
	 * @todo.
	 */
	public NetworkBinaryResponse(HttpExchange exchange) {
		super(exchange);
	}

}