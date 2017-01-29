package locomotor.components.network;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.io.File;
import java.lang.InterruptedException;
import java.lang.Thread;

import java.util.ArrayList;

import locomotor.components.Pair;
import locomotor.components.logging.ErrorHandler;
import locomotor.components.logging.Logging;
import locomotor.components.models.CategoryModel;
import locomotor.components.models.Item;
import locomotor.components.models.UserItem;
import locomotor.core.Comparator;
import locomotor.core.DBH;
import locomotor.core.jwt.JWTH;

/**
 * Network interface shown to the client.
 */
public class API {

	/**
	 * Contains all the usefull error codes that the server can return in case of error.
	 */
	public enum ErrorCode {
		/**
		* This error code is used when the error message is enough informations to send the client.
		* Wether the message is to be displayed or not to the user is let at the client's discretion.
		*/
		DEFAULT_ERROR_CODE(0),

		/**
		* This error code is used to notify the client that the error message must be displayed.
		*/
		DISPLAY_MESSAGE(1),

		/**
		* The short token used with the request is not valid (or no longer valid).
		*/
		INVALID_SHORT_TOKEN(2);
		
		/**
		 * The identifier.
		 */
		private final int _id;

		/**
		 * Constructs the object.
		 *
		 * @param      id    The identifier
		 */
		ErrorCode(int id) {
			_id = id;
		}

		/**
		 * Gets the value.
		 *
		 * @return     The value.
		 */
		public int getValue() {
			return _id;
		}
	}

	/**
	* Adds all the endpoints defined in this method to a NetworkHandler.
	* @param nh The NetworkHandler to which the endpoint will be attached
	*/
	public static void createHooks(NetworkHandler nh) {
		nh.createEndpoint("/api/user/auth", new IEndpointHandler() {
			public boolean handle(NetworkData data, NetworkResponseFactory response) {
				if(!super.handle(data, response)) {
					return false;
				}

				JWTH jwt = JWTH.getInstance();

				setExpectedParams("username", "password");
				if(areAllParamsDefined()) { // login with username & password
					// check user exist and good password
					String username = data.getAsString("username");
					String password = data.getAsString("password");
					Pair<String,Integer> claims = DBH.getInstance().authUser(username, password);

					// check error
					if (claims == null) {
						Pair<String, Logging> log = ErrorHandler.getInstance().pop();
						response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS, 
							log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
						return false;
					}
					String longToken = jwt.createLongToken(claims.getLeft(), claims.getRight());
					String shortToken = jwt.createShortToken(claims.getLeft(), claims.getRight());

					response.getJsonContext().success(Json.object()
						.add("short-term-token", shortToken)
						.add("long-term-token", longToken));

					return true;
				}
				else if(data.isDefined("token")) { // login with token
					// auth with token
					String longToken = data.getAsString("token");
					Pair<String,Integer> claims = jwt.checkToken(longToken);
					
					// check error
					if(claims == null) {
						Pair<String, Logging> log = ErrorHandler.getInstance().pop();
						response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS, 
							log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
						return false;
					}

					// check if the user still exist in the database
					// before create the token
					if(!DBH.getInstance().usernameAlreadyTaken(claims.getLeft())) {
						response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS, 
							"The token is no longer valid", ErrorCode.DEFAULT_ERROR_CODE);
						return false;
					}

					String shortToken = jwt.createShortToken(claims.getLeft(), claims.getRight());
					response.getJsonContext().success(Json.object()
						.add("short-term-token", shortToken));

					return false;
				}
				else {
					String errorMessage = "This endpoint needs either the parameters `username` and `password`"
						+ " to be defined or only the parameter `token`.";
					response.getJsonContext().failure(NetworkResponse.ErrorCode.BAD_REQUEST, errorMessage, 
						ErrorCode.DISPLAY_MESSAGE);
					return false;
				}
			}
		});

		nh.createEndpoint("/api/user/register", new IEndpointHandler() {
			public boolean handle(NetworkData data, NetworkResponseFactory response) {
				if(!super.handle(data, response)) {
					return false;
				}

				setExpectedParams("username", "password");
				if(!areAllParamsDefined()) {
					sendDefaultMissingParametersMessage();
					return false;
				}

				JWTH jwt = JWTH.getInstance();						
				// check user exist
				String username = data.getAsString("username");
				String password = data.getAsString("password");
				Pair<String,Integer> claims = DBH.getInstance().registerUser(username, password, 0);
				
				// check error
				if (claims == null) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS,
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}
				
				String shortToken = jwt.createShortToken(claims.getLeft(), claims.getRight());
				String longToken = jwt.createLongToken(claims.getLeft(), claims.getRight());

				response.getJsonContext().success(Json.object()
					.add("short-term-token", shortToken)
					.add("long-term-token", longToken));

				return true;
			}
		});

		nh.createEndpoint("/api/model/get", new IEndpointHandler() {
			public boolean handle(NetworkData data, NetworkResponseFactory response) {
				if(!super.handle(data, response)) {
					return false;
				}

				setExpectedParams("token");
				if(!areAllParamsDefined()) {
					sendDefaultMissingParametersMessage();
					return false;
				}

				// auth with token
				String shortToken = data.getAsString("token");
				JWTH jwt = JWTH.getInstance();
				Pair<String,Integer> claims = jwt.checkToken(shortToken);
					
				// check error
				if(claims == null) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS, 
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}
				
				JsonArray model = Json.array();
				for(CategoryModel catM : DBH.getInstance().getCategoriesModel()) {
					model.add(catM.toJSON());
				}
				
				response.getJsonContext().success(Json.object()
					.add("model", model));

				return true;
			}
		});

		nh.createEndpoint("/api/search", new IEndpointHandler() {
			public boolean handle(NetworkData data, NetworkResponseFactory response) {
				if(!super.handle(data, response)) {
					return false;
				}

				setExpectedParams("token");
				setExpectedParams("criterias");
				if(!areAllParamsDefined()) {
					sendDefaultMissingParametersMessage();
					return false;
				}

				// auth with token
				String shortToken = data.getAsString("token");
				JWTH jwt = JWTH.getInstance();
				Pair<String,Integer> claims = jwt.checkToken(shortToken);
					
				// check error
				if(claims == null) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS, 
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}
				
				// get criterias
				String criterias = data.getAsString("criterias");
				JsonValue userCriterias = Json.parse(criterias);

				// get model
				ArrayList<CategoryModel> catModel = DBH.getInstance().getCategoriesModel();
				
				// create user criterias from json
				UserItem ui = UserItem.fromJSON(userCriterias, catModel);

				// retrieve all items
				ArrayList<Item> items = DBH.getInstance().getItems(catModel);

				// comparator
				Comparator comp = new Comparator(catModel, ui);
				JsonArray results = comp.computeGradeOfItems(items);

				System.out.println("Sending results found");
				
				response.getJsonContext().success(Json.object()
					.add("results", results));

				return true;
			}
		});

		nh.createEndpoint("/img/get", new IEndpointHandler() {
			public boolean handle(NetworkData data, NetworkResponseFactory response) {
				if(!super.handle(data, response)) {
					return false;
				}

				setExpectedParams("name");
				if(!areAllParamsDefined()) {
					sendDefaultMissingParametersMessage();
					return false;
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
						+ "resources/core/images/" + data.getAsString("name") + "`", ErrorCode.DEFAULT_ERROR_CODE);
				}

				return true;
			}
		});
	}
}