package locomotor.front.user;

import com.eclipsesource.json.JsonObject;
import java.util.TreeMap;
import locomotor.front.components.network.ClientRequest;

public class Main {
	public static void main(String[] args) {		
		System.setProperty("javax.net.ssl.trustStore", "ClientKey.pfx");
		System.setProperty("javax.net.ssl.trustStorePassword","motdepasse");

		TreeMap<String, String> params = new TreeMap<String, String>();
		params.put("paramName", "paramValue");

		ClientRequest cr = new ClientRequest("https://localhost:8000/");
		cr.requestJson("api/test", params).thenAccept(new Consumer<JsonObject>() {
			public void accept(JsonObject obj) {
				if(!obj.get("token") == null) {
					System.out.println("meeh");
				}
			}
		});
	}
}