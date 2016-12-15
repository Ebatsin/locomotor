package locomotor.components.network;

import com.sun.net.httpserver.HttpExchange;
import java.io.File;

public class NetworkBinaryResponse extends NetworkResponse {
	HttpExchange _exchange;

	public NetworkBinaryResponse(HttpExchange exchange) {
		super(exchange);
	}

	public void success(File file) {
		sendAnswer(file, 200);
	}
}