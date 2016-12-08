package locomotor.components.network;

import com.sun.net.httpserver.HttpExchange;

public class NetworkResponseFactory {
	HttpExchange _exchange;

	public NetworkResponseFactory(HttpExchange exchange) {
		_exchange = exchange;
	}

	public NetworkBinaryResponse getBinaryContext() {
		return new NetworkBinaryResponse(_exchange);
	}

	public NetworkJsonResponse getJsonContext() {
		return new NetworkJsonResponse(_exchange);
	}
}