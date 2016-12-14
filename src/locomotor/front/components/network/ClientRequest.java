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

public class ClientRequest {
	String _hostname;
	FileUpload _params;

	public ClientRequest(String hostname) {
		_hostname = hostname;
		_params = new FileUpload();
	}

	public CompletableFuture<InputStream> requestBinary(String endpoint, TreeMap<String, String> parameters) {
		return CompletableFuture.supplyAsync(new Supplier<InputStream>() {
			public InputStream get() {
				return new DataInputStream(null);
			}
		});
	}

	public void addParam(String name, String value) {
		_params.add(name, value);
	}

	public void addParam(String name, InputStream value) {
		_params.add(name, value);
	}

	public CompletableFuture<JsonObject> requestJson(String endpoint) {
		return CompletableFuture.supplyAsync(new Supplier<JsonObject>() {
			public JsonObject get() {
				try {
					_params.send(_hostname + endpoint);

					Scanner scanner = new Scanner(_params.getURLConnection().getInputStream(), "utf-8").useDelimiter("\\A");
					String response;
					if(scanner.hasNext()) {
						response = scanner.next();
					}
					else {
						response = Json.object().toString(); // empty object
					}

					return Json.parse(response).asObject();
				}
				catch(IOException exception) {
					System.out.println("une erreur est apparue");
					System.out.println(exception.toString());
					return Json.object();
				}
			}
		});
	}
}