package locomotor.components.network;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import locomotor.core.jwt.JWTH;

/**
 * Network interface shown to the client.
 */
public class API {
	static public void createHooks(NetworkHandler nh) {
		nh.createEndpoint("/api/test", new IEndpointHandler() {
			public void handle(NetworkData data, NetworkResponseFactory response) {
				if(data.isValid()) {
					System.out.println("Paramètres reçus : " + data.getParametersName());

					if(!data.isDefined("token")) {
						response.getJsonContext().failure(NetworkResponse.ErrorCode.BAD_REQUEST, "Le paramètre `token` n'a pas été trouvé. Il est obligatoire pour cette requête");
						return;
					}
					
					String token = data.getAsString("token");
					JWTH jwt = JWTH.getInstance();
					jwt.checkToken(token);
				}
				else {
					response.getJsonContext().failure(NetworkResponse.ErrorCode.BAD_REQUEST, "La requête doit être au format POST pour être lue par le serveur");
					return;
				}

				response.getJsonContext().success(Json.object()
						.add("message", "Bienvenue à toi"));
			}
		});
		nh.createEndpoint("/api/user/auth", new IEndpointHandler() {
			public void handle(NetworkData data, NetworkResponseFactory response) {
				if(data.isValid()) {
					System.out.println("Paramètres reçus : " + data.getParametersName());
				}
				else {
					response.getJsonContext().failure(NetworkResponse.ErrorCode.BAD_REQUEST, "La requête doit être au format POST pour être lue par le serveur");
					return;
				}

				// get tokens
				JWTH jwt = JWTH.getInstance();
		        String longToken = jwt.createLongToken("123456", false);
		        String shortToken = jwt.createShortToken("123456", false);

				response.getJsonContext().success(Json.object()
						.add("short-term-token", shortToken)
						.add("long-term-token", longToken));
			}
		});
	}
}