package locomotor.components.network;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import java.io.ByteArrayOutputStream;
import locomotor.components.MutableInteger;

/**
 * @todo.
 */
public class NetworkJsonResponse extends NetworkResponse {

	/**
	 * @todo.
	 */
	public NetworkJsonResponse(HttpExchange exchange) {
		super(exchange);
	}

	/**
	 * @todo.
	 */
	public void success(JsonObject object) {
		JsonObject root = Json.object().add("success", "true").add("data", object);
		MutableInteger length = new MutableInteger();
		ByteArrayOutputStream out = toByteArrayOutputStream(root, length);
		sendAnswer(out, "application/json", 200, length.intValue());
	}
}