package locomotor.components.network;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import locomotor.components.MutableInteger;

public abstract class NetworkResponse {	
	HttpExchange _exchange;

	public enum ErrorCode {
		BAD_REQUEST(400), // the server will not process the request due to a malformed request (parameters missing, ...)
		UNAUTHORIZED_ACCESS(401), // the user need to authenticate
		FORBIDDEN_ACCESS(403), // the user can not access this part
		NOT_FOUND(404), // the data was not found
		LENGTH_REQUIRED(411); // the request must specify its length

		private final int _id;

		ErrorCode(int id) {
			_id = id;
		}

		public int getValue() {
			return _id;
		}
	}

	public NetworkResponse(HttpExchange exchange) {
		_exchange = exchange;
	}

	/**
	 * Send the client an error message. The message is formatted in JSON.
	 * @param error The HTTP error code to use
	 * @param message The message to send in the JSON object, describing the error to the user
	 */
	public void failure(ErrorCode error, String message) {
		JsonObject obj = Json.object().add("success", "false").add("message", message);
		MutableInteger length = new MutableInteger();
		ByteArrayOutputStream out = toByteArrayOutputStream(obj, length);
		sendAnswer(out, "application/json", error.getValue(), length.intValue());
	}

	/**
	 * Convert a JSON object to an ByteArrayOutputStream
	 * @param obj The object to be converted
	 * @return A newly created ByteArrayOutputStream
	 */
	private ByteArrayOutputStream toByteArrayOutputStream(JsonObject obj) {
		return toByteArrayOutputStream(obj, null);
	}

	/**
	 * Convert a JSON object to an ByteArrayOutputStream and stores the length of the generated object
	 * @param obj The object to be converted
	 * @param length The object in which to store the length of the ByteArrayOutputStream
	 * @return A newly created ByteArrayOutputStream
	 */
	private ByteArrayOutputStream toByteArrayOutputStream(JsonObject obj, MutableInteger length) {
		String json = obj.toString();
		return toByteArrayOutputStream(json, length);
	}

	/**
	 * Convert a String object to an ByteArrayOutputStream
	 * @param string The string to be converted
	 * @return A newly created ByteArrayOutputStream
	 */
	private ByteArrayOutputStream toByteArrayOutputStream(String string) {
		return toByteArrayOutputStream(string, null);
	}

	/**
	 * Convert a String object to an ByteArrayOutputStream
	 * @param string The string to be converted
	 * @param length The object in which to store the length of the ByteArrayOutputStream
	 * @return A newly created ByteArrayOutputStream
	 */
	private ByteArrayOutputStream toByteArrayOutputStream(String string, MutableInteger length) {
		byte[] data = string.getBytes(StandardCharsets.UTF_8);
		ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length);
		baos.write(data, 0, data.length);
		if(length != null) {
			length.set(data.length);
		}
		return baos;
	}


	/**
	 * Send the response to the client.
	 * @param out The content of the message to send
	 * @param contentType The MIME type of the content sent
	 * @param code The HTTP status code to send
	 * @param length The length of the message sent. If unknown, can be set to 0
	 */
	private void sendAnswer(ByteArrayOutputStream out, String contentType, int code, int length) {
		Headers headers = _exchange.getResponseHeaders();
		if(contentType != null) {
			headers.set("Content-Type", contentType);
		}

		try {
			_exchange.sendResponseHeaders(code, length);

			OutputStream bodyResponse = _exchange.getResponseBody();
			out.writeTo(bodyResponse);
			bodyResponse.close();
		}
		catch(IOException exception) {
			System.out.println("Error : unable to send the response to the user");
		}
	}
}