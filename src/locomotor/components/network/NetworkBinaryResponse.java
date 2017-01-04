package locomotor.components.network;

import com.sun.net.httpserver.HttpExchange;

import java.io.File;

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

	/**
	 * @todo.
	 */
	public void success(File file) {
		sendAnswer(file, 200);
	}
}