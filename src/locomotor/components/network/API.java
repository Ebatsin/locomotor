package locomotor.components.network;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

/**
 * Network interface shown to the client.
 */
public class API {
	static public void createHooks(NetworkHandler nh) {
		nh.createEndpoint("/api/test", new IEndpointHandler() {
			public void handle(NetworkData data, NetworkResponseFactory response) {
				if(data.isValid()) {
					System.out.println("Paramètres reçus : " + data.getParametersName());

					if(!data.isDefined("test1")) {
						response.getJsonContext().failure(NetworkResponse.ErrorCode.BAD_REQUEST, "Le paramètre `test1` n'a pas été trouvé. Il est obligatoire pour cette requête");
						return;
					}
				}
				else {
					response.getJsonContext().failure(NetworkResponse.ErrorCode.BAD_REQUEST, "La requête doit être au format POST pour être lue par le serveur");
					return;
				}

				response.getJsonContext().success(Json.object()
						.add("message", "Bienvenue à toi")
						.add("token", "12345"));
			}
		});
	}
}