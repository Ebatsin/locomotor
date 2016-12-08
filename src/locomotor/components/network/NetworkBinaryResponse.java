package locomotor.components.network;

import com.sun.net.httpserver.HttpExchange;

public class NetworkBinaryResponse extends NetworkResponse {
	HttpExchange _exchange;

	public NetworkBinaryResponse(HttpExchange exchange) {
		super(exchange);
	}
}