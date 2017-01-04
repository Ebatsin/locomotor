package locomotor.front.components.network;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import java.io.DataInputStream; // @todo remove it
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import javax.net.ssl.HttpsURLConnection;

/**
 * @todo.
 */
public class ClientRequest {
	
	/**
	 * @todo.
	 */
	String _hostname;

	/**
	 * @todo.
	 */
	FileUpload _params;

	/**
	 * Constructs the object.
	 *
	 * @param      hostname  The hostname
	 */
	public ClientRequest(String hostname) {
		_hostname = hostname;
		_params = new FileUpload();
	}

	/**
	 * @todo.
	 */
	public CompletableFuture<InputStream> requestBinary(String endpoint, TreeMap<String, String> parameters) {
		return CompletableFuture.supplyAsync(new Supplier<InputStream>() {
			public InputStream get() {
				return new DataInputStream(null);
			}
		});
	}

	/**
	 * Adds a parameter.
	 *
	 * @param      name   The name
	 * @param      value  The value
	 */
	public void addParam(String name, String value) {
		_params.add(name, value);
	}

	/**
	 * Adds a parameter.
	 *
	 * @param      name   The name
	 * @param      value  The value
	 */
	public void addParam(String name, InputStream value) {
		_params.add(name, value);
	}

	/**
	 * @todo.
	 */
	public CompletableFuture<JsonObject> requestJson(String endpoint) {
		return CompletableFuture.supplyAsync(new Supplier<JsonObject>() {
			public JsonObject get() {
				InputStream inStream;

				_params.send(_hostname + endpoint);
				try {
					inStream = _params.getURLConnection().getInputStream();
				}
				catch(Exception exception) {
					inStream = _params.getURLConnection().getErrorStream();
				}
				
				Scanner scanner = new Scanner(inStream, "utf-8").useDelimiter("\\A");
				String response;
				if(scanner.hasNext()) {
					response = scanner.next();
				}
				else {
					response = Json.object().toString(); // empty object
				}

				return Json.parse(response).asObject();
			}
		});
	}
}