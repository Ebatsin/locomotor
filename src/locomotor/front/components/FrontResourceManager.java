package locomotor.front.components;

import com.eclipsesource.json.JsonObject;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import locomotor.components.ResourceManager;
import locomotor.front.components.network.ClientRequest;

public class FrontResourceManager extends ResourceManager {
	protected static FrontResourceManager _instance = null;

	protected FrontResourceManager() {
		super();
		_baseURL = "resources/front/";
	}

	public static synchronized FrontResourceManager getInstance() {
		if(_instance == null) {
			_instance = new FrontResourceManager();
		}

		return _instance;
	}

	public CompletableFuture<Long> getRemoteVersion(String resource) {
		ClientRequest cr = new ClientRequest("https://localhost:8000/");
		cr.addParam("id", resource);
		return cr.requestJson("api/img/version").thenApply(new Function<JsonObject, Long>() {
			public Long apply(JsonObject obj) {
				if(obj.get("success").asString().equals("true")) {
					return obj.get("data").asObject().get("version").asLong();
				}
				else {
					System.out.println("erreur");
				}

				return new Long(0);
			}
		});
	}
}