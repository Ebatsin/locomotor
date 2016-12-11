package locomotor.front.components.network;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import java.io.InputStream;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import java.util.Scanner;
import java.util.TreeMap;


import java.io.DataInputStream; // @todo remove it
import java.io.IOException;

public class ClientRequest {
	String _hostname;

	public ClientRequest(String hostname) {
		_hostname = hostname;
	}

	public CompletableFuture<InputStream> requestBinary(String endpoint, TreeMap<String, String> parameters) {
		return CompletableFuture.supplyAsync(new Supplier<InputStream>() {
			public InputStream get() {
				return new DataInputStream(null);
			}
		});
	}

	public CompletableFuture<JsonObject> requestJson(String endpoint, TreeMap<String, String> parameters) {
		return CompletableFuture.supplyAsync(new Supplier<JsonObject>() {
			public JsonObject get() {
				try {
					System.out.println("contruction de la requête");
					URL url = new URL(_hostname + endpoint);
					System.out.println("construction de la connection");
					HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();

					System.out.println("ouverture de la connection");

					Scanner scanner = new Scanner(connection.getInputStream(), "utf-8").useDelimiter("\\A");
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