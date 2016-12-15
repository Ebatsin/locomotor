package locomotor.components.network;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import java.io.ByteArrayOutputStream;
import locomotor.components.MutableInteger;

public class NetworkJsonResponse extends NetworkResponse {

	public NetworkJsonResponse(HttpExchange exchange) {
		super(exchange);
	}

	/**
	* Send the client an JSON object. Mark the exchange as a success.
	* @param object The JSON object to send the user
	*/
	public void success(JsonObject object) {
		JsonObject root = Json.object().add("success", "true").add("data", object);
		MutableInteger length = new MutableInteger();
		ByteArrayOutputStream out = toByteArrayOutputStream(root, length);
		sendAnswer(out, "application/json", 200, length.intValue());
	}
}