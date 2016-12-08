package locomotor.components.network;

import com.sun.net.httpserver.HttpExchange;

public class NetworkResponseFactory {
	HttpExchange _exchange;

	public NetworkResponseFactory(HttpExchange exchange) {
		_exchange = exchange;
	}
}