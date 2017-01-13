package locomotor.components.network;

import com.sun.net.httpserver.HttpExchange;

import java.io.File;

/**
 * This class is used to send a response that contains binary data to the client.
 */
public class NetworkBinaryResponse extends NetworkResponse {
	
	HttpExchange _exchange;

	public NetworkBinaryResponse(HttpExchange exchange) {
		super(exchange);
	}

	/**
	 * Send the file to the client along with a 200 success code.
	 * @file The file to send to the client
	 */
	public void success(File file) {
		sendAnswer(file, 200);
	}
}