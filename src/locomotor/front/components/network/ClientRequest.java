package locomotor.front.components.network;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import java.io.InputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.util.concurrent.CompletableFuture;
import java.util.Scanner;
import java.util.TreeMap;

public class ClientRequest {
	String _hostname;

	public ClientRequest(String hostname) {
		_hostname = hostname;
	}

	public CompletableFuture<InputStream> requestBinary(String endpoint, TreeMap<String, String> parameters) {

	}

	public CompletableFuture<JsonObject> requestJson(String endpoint, TreeMap<String, String> parameters) {
		return CompletableFuture.supplyAsync(new Supplier<JsonObject>() {
			public JsonObject get() {
				URL url = new URL(_hostname + endpoint);
				HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();

				Scanner scanner = new Scanner(connection.getInputStream(), "utf-8").useDelimiter("\\A");
				String response;
				if(scanner.hasNext()) {
					response = scanner.next();
				}
				else {
					response = Json.Object().toString(); // empty object
				}

				return Json.parse(response).asObject();
			}
		});
	}
}