package locomotor.components.network;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import java.io.File;
import java.lang.InterruptedException;
import java.lang.Thread;

import locomotor.components.logging.ErrorContext;
import locomotor.components.logging.ErrorHandler;
import locomotor.core.jwt.JWTH;

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
					System.out.println("Thread PID = " + Thread.currentThread().getId());
					if(!data.isDefined("token")) {
						response.getJsonContext().failure(NetworkResponse.ErrorCode.BAD_REQUEST, "Le paramètre `token` n'a pas été trouvé. Il est obligatoire pour cette requête");
						return;
					}
					
					String token = data.getAsString("token");
					JWTH jwt = JWTH.getInstance();
					jwt.checkToken(token);
				}
				else {
					response.getJsonContext().failure(NetworkResponse.ErrorCode.BAD_REQUEST, 
						"La requête doit être au format POST pour être lue par le serveur");
					return;
				}

				response.getJsonContext().success(Json.object()
						.add("message", "Bienvenue à toi"));
			}
		});
		nh.createEndpoint("/api/user/auth", new IEndpointHandler() {
			public void handle(NetworkData data, NetworkResponseFactory response) {

				System.out.println("Thread PID = " + Thread.currentThread().getId());
				System.out.println("Sleep");

				if(data.isValid()) {
					System.out.println("Paramètres reçus : " + data.getParametersName());
				}
				else {
					response.getJsonContext().failure(NetworkResponse.ErrorCode.BAD_REQUEST, "La requête doit être au format POST pour être lue par le serveur");
					return;
				}

				// create error context
				ErrorHandler eh = ErrorHandler.getInstance();
				ErrorContext ec = eh.get(Thread.currentThread().getId());

				// get tokens
				JWTH jwt = JWTH.getInstance();
				String longToken = jwt.createLongToken("123456", false);
				String shortToken = jwt.createShortToken("123456", false);

				// print and remove context error
				System.out.println(ec);
				eh.remove(Thread.currentThread().getId());

				response.getJsonContext().success(Json.object()
						.add("short-term-token", shortToken)
						.add("long-term-token", longToken));
			}
		});

		nh.createEndpoint("/img/get", new IEndpointHandler() {
			public void handle(NetworkData data, NetworkResponseFactory response) {
				System.out.println("Thread PID image = " + Thread.currentThread().getId());
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