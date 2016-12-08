package locomotor.components.network;

import com.eclipsesource.json.JsonObject;
import com.sun.net.httpserver.HttpExchange;

public class NetworkJsonResponse extends NetworkResponse {

	public NetworkJsonResponse(HttpExchange exchange) {
		super(exchange);
	}

	public void success(JsonObject object) {

	}
}