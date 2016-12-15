package locomotor.components.network;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import java.io.File;

/**
 * Network interface shown to the client.
 */
public class API {

	/**
	* Adds all the endpoints defined in this method to a NetworkHandler.
	* @param nh The NetworkHandler to which the endpoint will be attached
	*/
	public static void createHooks(NetworkHandler nh) {
		nh.createEndpoint("/api/test", new IEndpointHandler() {
			public void handle(NetworkData data, NetworkResponseFactory response) {
				if(data.isValid()) {
					System.out.println("Paramètres reçus : " + data.getParametersName());

					if(!data.isDefined("test1")) {
						response.getJsonContext().failure(NetworkResponse.ErrorCode.BAD_REQUEST, 
							"Le paramètre `test1` n'a pas été trouvé. Il est obligatoire pour cette requête");
						return;
					}
				}
				else {
					response.getJsonContext().failure(NetworkResponse.ErrorCode.BAD_REQUEST, 
						"La requête doit être au format POST pour être lue par le serveur");
					return;
				}

				response.getJsonContext().success(Json.object()
						.add("message", "Bienvenue à toi")
						.add("token", "12345"));
			}
		});

		nh.createEndpoint("/img/get", new IEndpointHandler() {
			public void handle(NetworkData data, NetworkResponseFactory response) {
				if(data.isValid()) {
					if(!data.isDefined("name")) {
						response.getJsonContext().failure(NetworkResponse.ErrorCode.BAD_REQUEST, 
							"Le paramètre `name` n'a pas été trouvé. Il est obligatoire pour cette requête");
						return;
					}
				}
				else {
					response.getJsonContext().failure(NetworkResponse.ErrorCode.BAD_REQUEST, 
						"La requête doit être au format POST pour être lue par le serveur");
					return;
				}

				File file = new File("resources/core/images/" + data.getAsString("name"));
				System.out.print("Fichier demandé : " + data.getAsString("name"));

				if(file.exists() && !file.isDirectory()) {
					System.out.println(", le fichier existe");
					response.getBinaryContext().success(file);
				}
				else {
					System.out.println(", le fichier n'existe pas");
					response.getJsonContext().failure(NetworkResponse.ErrorCode.NOT_FOUND, 
						"L'image demandée n'a pas pu être trouvée : `" 
						+ "resources/core/images/" + data.getAsString("name") + "`");
				}
			}
		});
	}
}