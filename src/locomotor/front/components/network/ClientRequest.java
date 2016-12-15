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

	public CompletableFuture<BinaryObject> requestBinary(String endpoint) {
		return CompletableFuture.supplyAsync(new Supplier<BinaryObject>() {
			public BinaryObject get() {
				InputStream inStream;
				String mimeType = null;

				_params.send(_hostname + endpoint);

				try { // binary response
					inStream = _params.getURLConnection().getInputStream();

					// find the MIME-TYPE returned by the server
					int n = 1;
					String headerName = "";
					while((headerName = _params.getURLConnection().getHeaderFieldKey(n)) != null || n == 0) {
						if(headerName.equals("Content-type")) {
							mimeType = _params.getURLConnection().getHeaderField(n);
						}
						++n;
					}

					if(mimeType == null) {
						return new BinaryObject("Non specified MIME-TYPE for the image");
					}
					return new BinaryObject(inStream, mimeType);
				}
				catch(Exception exception) { // json error message
					inStream = _params.getURLConnection().getErrorStream();
					Scanner scanner = new Scanner(inStream, "utf-8").useDelimiter("\\A");
					String response;
					if(scanner.hasNext()) {
						response = scanner.next();
						JsonObject obj = Json.parse(response).asObject();
						if(obj.get("message") != null) {
							return new BinaryObject(obj.get("message").asString());
						}
					}
				}

				return new BinaryObject("Unknown error");
			}
		});
	}
}