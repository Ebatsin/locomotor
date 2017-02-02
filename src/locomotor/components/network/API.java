package locomotor.components.network;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.io.File;
import java.lang.InterruptedException;
import java.lang.Thread;

import java.util.ArrayList;
import java.util.HashMap;

import locomotor.components.Pair;
import locomotor.components.logging.ErrorHandler;
import locomotor.components.logging.Logging;
import locomotor.components.models.Booking;
import locomotor.components.models.CategoryModel;
import locomotor.components.models.Item;
import locomotor.components.models.ItemFull;
import locomotor.components.models.ItemSoft;
import locomotor.components.models.Unit;
import locomotor.components.models.Universe;
import locomotor.components.models.UserItem;
import locomotor.core.AccreditationLevel;
import locomotor.core.Comparator;
import locomotor.core.CoreResourceManager;
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
					Pair<String,AccreditationLevel> claims = DBH.getInstance().authUser(username, password);

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
					Pair<String,AccreditationLevel> claims = jwt.checkToken(longToken);
						
					// check error
					if(claims == null) {
						Pair<String, Logging> log = ErrorHandler.getInstance().pop();
						response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS, 
							log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
						return false;
					}

					// check if the user still exist in the database
					// before create the token
					if(!DBH.getInstance().userStillExists(claims.getLeft())) {
						response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS, 
							"The user does not exist", ErrorCode.DEFAULT_ERROR_CODE);
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
				Pair<String, AccreditationLevel> claims = DBH.getInstance().registerUser(
					username, 
					password, 
					AccreditationLevel.MUNDANE
				);
				
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

		nh.createEndpoint("/api/user/info", new IEndpointHandler() {
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
				Pair<String,AccreditationLevel> claims = jwt.checkToken(shortToken);
					
				// check error
				if(claims == null) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS, 
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}

				String userID = claims.getLeft();
				HashMap<String, Object> info = DBH.getInstance().getUserInfo(userID);

				// check error
				if(info == null) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.NOT_FOUND, 
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}
				
				response.getJsonContext().success(Json.object()
					.add("username", info.get("username").toString())
					.add("adminLevel", (Integer)info.get("adminLevel")));

				return true;

			}
		});

		nh.createEndpoint("/api/user/change/username", new IEndpointHandler() {
			public boolean handle(NetworkData data, NetworkResponseFactory response) {
				if(!super.handle(data, response)) {
					return false;
				}

				setExpectedParams("token");
				setExpectedParams("newUsername");
				if(!areAllParamsDefined()) {
					sendDefaultMissingParametersMessage();
					return false;
				}

				// auth with token
				String shortToken = data.getAsString("token");
				JWTH jwt = JWTH.getInstance();
				Pair<String,AccreditationLevel> claims = jwt.checkToken(shortToken);
					
				// check error
				if(claims == null) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS, 
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}

				String userID = claims.getLeft();
				String newUsername = data.getAsString("newUsername");
				boolean result = DBH.getInstance().changeUsername(userID, newUsername);

				if(!result) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.NOT_FOUND, 
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}
				
				response.getJsonContext().success(Json.object());
				return true;

			}
		});

		nh.createEndpoint("/api/user/change/password", new IEndpointHandler() {
			public boolean handle(NetworkData data, NetworkResponseFactory response) {
				if(!super.handle(data, response)) {
					return false;
				}

				setExpectedParams("token");
				setExpectedParams("oldPassword");
				setExpectedParams("newPassword");
				if(!areAllParamsDefined()) {
					sendDefaultMissingParametersMessage();
					return false;
				}

				// auth with token
				String shortToken = data.getAsString("token");
				JWTH jwt = JWTH.getInstance();
				Pair<String,AccreditationLevel> claims = jwt.checkToken(shortToken);
					
				// check error
				if(claims == null) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS, 
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}

				String userID = claims.getLeft();
				String oldPassword = data.getAsString("oldPassword");
				String newPassword = data.getAsString("newPassword");

				boolean result = DBH.getInstance().changePassword(userID, oldPassword, newPassword);

				if(!result) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.NOT_FOUND, 
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}
				
				response.getJsonContext().success(Json.object());
				return true;

			}
		});

		nh.createEndpoint("/api/user/remove", new IEndpointHandler() {
			public boolean handle(NetworkData data, NetworkResponseFactory response) {
				if(!super.handle(data, response)) {
					return false;
				}

				setExpectedParams("token");
				setExpectedParams("password");
				if(!areAllParamsDefined()) {
					sendDefaultMissingParametersMessage();
					return false;
				}

				// auth with token
				String shortToken = data.getAsString("token");
				JWTH jwt = JWTH.getInstance();
				Pair<String,AccreditationLevel> claims = jwt.checkToken(shortToken);
					
				// check error
				if(claims == null) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS, 
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}

				String userID = claims.getLeft();
				String password = data.getAsString("password");

				boolean result = DBH.getInstance().removeUser(userID, password);

				if(!result) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.NOT_FOUND, 
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}
				
				response.getJsonContext().success(Json.object());
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
				Pair<String,AccreditationLevel> claims = jwt.checkToken(shortToken);
					
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

		nh.createEndpoint("/api/unit/get-all", new IEndpointHandler() {
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
				Pair<String,AccreditationLevel> claims = jwt.checkToken(shortToken);
					
				// check error
				if(claims == null) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS, 
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}
				
				JsonArray units = Json.array();
				for(Unit unit : DBH.getInstance().getAllUnits()) {
					units.add(unit.toJSON());
				}
				
				response.getJsonContext().success(Json.object()
					.add("units", units));

				return true;
			}
		});

		nh.createEndpoint("/api/universe/get", new IEndpointHandler() {
			public boolean handle(NetworkData data, NetworkResponseFactory response) {
				if(!super.handle(data, response)) {
					return false;
				}

				setExpectedParams("token");
				setExpectedParams("id");
				if(!areAllParamsDefined()) {
					sendDefaultMissingParametersMessage();
					return false;
				}

				// auth with token
				String shortToken = data.getAsString("token");
				JWTH jwt = JWTH.getInstance();
				Pair<String,AccreditationLevel> claims = jwt.checkToken(shortToken);
					
				// check error
				if(claims == null) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS, 
						log.getRight().toString(), ErrorCode.DISPLAY_MESSAGE);
					return false;
				}
				
				// get the universe
				String universeID = data.getAsString("id");
				Universe universe = DBH.getInstance().getUniverse(universeID);
				
				// check error
				if(universe == null) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.NOT_FOUND, 
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}

				response.getJsonContext().success(Json.object()
					.add("universe", universe.toJSON()));
				return true;
			}
		});

		nh.createEndpoint("/api/item/get", new IEndpointHandler() {
			public boolean handle(NetworkData data, NetworkResponseFactory response) {
				if(!super.handle(data, response)) {
					return false;
				}

				setExpectedParams("token");
				setExpectedParams("id");
				if(!areAllParamsDefined()) {
					sendDefaultMissingParametersMessage();
					return false;
				}

				// auth with token
				String shortToken = data.getAsString("token");
				JWTH jwt = JWTH.getInstance();
				Pair<String,AccreditationLevel> claims = jwt.checkToken(shortToken);
					
				// check error
				if(claims == null) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS, 
						log.getRight().toString(), ErrorCode.DISPLAY_MESSAGE);
					return false;
				}
				
				// get the item
				String itemID = data.getAsString("id");
				ArrayList<CategoryModel> catModel = DBH.getInstance().getCategoriesModel();
				ItemFull itemFull = DBH.getInstance().getItem(itemID, catModel);
				
				// check error
				if(itemFull == null) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.NOT_FOUND, 
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}

				response.getJsonContext().success(Json.object()
					.add("item", itemFull.toJSON()));
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
				Pair<String,AccreditationLevel> claims = jwt.checkToken(shortToken);
					
				// check error
				if(claims == null) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.NOT_FOUND, 
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}
				
				// get criterias
				String criterias = data.getAsString("criterias");
				JsonValue userCriterias = Json.parse(criterias);

				// get model
				ArrayList<CategoryModel> catModel = DBH.getInstance().getCategoriesModel();
				
				// create user criterias from json
				UserItem ui;
				try {
					ui = UserItem.fromJSON(userCriterias, catModel);
				}
				catch (Exception ex) { // error
					response.getJsonContext().failure(NetworkResponse.ErrorCode.BAD_REQUEST, 
						"An error occured while parsing user criterias", ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}

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

		nh.createEndpoint("/api/resource/version", new IEndpointHandler() {
			public boolean handle(NetworkData data, NetworkResponseFactory response) {
				if(!super.handle(data, response)) {
					return false;
				}

				setExpectedParams("token");
				setExpectedParams("id");
				if(!areAllParamsDefined()) {
					sendDefaultMissingParametersMessage();
					return false;
				}

				// auth with token
				String shortToken = data.getAsString("token");
				JWTH jwt = JWTH.getInstance();
				Pair<String,AccreditationLevel> claims = jwt.checkToken(shortToken);
					
				// check error
				if(claims == null) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.NOT_FOUND, 
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}

				String id = data.getAsString("id");		
				CoreResourceManager crm = CoreResourceManager.getInstance();
				
				if(!crm.exists(id)) {
					
					response.getJsonContext().failure(NetworkResponse.ErrorCode.NOT_FOUND, 
						"The file requested does not exist", ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}
				
				response.getJsonContext().success(Json.object()
					.add("version", crm.getVersion(id)));
				return true;
				
			}
		});

		nh.createEndpoint("/api/resource/get", new IEndpointHandler() {
			public boolean handle(NetworkData data, NetworkResponseFactory response) {
				if(!super.handle(data, response)) {
					return false;
				}

				setExpectedParams("token");
				setExpectedParams("id");
				if(!areAllParamsDefined()) {
					sendDefaultMissingParametersMessage();
					return false;
				}

				// auth with token
				String shortToken = data.getAsString("token");
				JWTH jwt = JWTH.getInstance();
				Pair<String,AccreditationLevel> claims = jwt.checkToken(shortToken);
					
				// check error
				if(claims == null) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.NOT_FOUND, 
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}

				File file = new File("resources/core/" + data.getAsString("id"));

				if(!file.exists() || file.isDirectory()) {

					response.getJsonContext().failure(NetworkResponse.ErrorCode.NOT_FOUND, 
						"The file requested does not exist", ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}

				response.getBinaryContext().success(file);
				return true;
			}
		});

		nh.createEndpoint("/api/booking/add", new IEndpointHandler() {
			public boolean handle(NetworkData data, NetworkResponseFactory response) {
				if(!super.handle(data, response)) {
					return false;
				}

				setExpectedParams("token");
				setExpectedParams("id");
				setExpectedParams("startDate");
				setExpectedParams("endDate");
				setExpectedParams("quantity");

				if(!areAllParamsDefined()) {
					sendDefaultMissingParametersMessage();
					return false;
				}

				// auth with token
				String shortToken = data.getAsString("token");
				JWTH jwt = JWTH.getInstance();
				Pair<String,AccreditationLevel> claims = jwt.checkToken(shortToken);
					
				// check error
				if(claims == null) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS, 
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}

				// check validity
				int quantity;
				long startDate;
				long endDate;
				try {
					quantity = Integer.parseInt(data.getAsString("quantity"));
					startDate = Long.parseLong(data.getAsString("startDate"));
					endDate = Long.parseLong(data.getAsString("endDate"));
				}
				catch (Exception ex) {
					String message = "At least one of the following parameter is malformated: ";
					message +=  "`quantity`, `startDate`, `endDate`";
					response.getJsonContext().failure(NetworkResponse.ErrorCode.BAD_REQUEST, 
						message, ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}

				String userID = claims.getLeft();
				String bookingID = DBH.getInstance().addBooking(
					userID, 
					data.getAsString("id"), 
					quantity, 
					startDate, 
					endDate
				);
				
				// check error
				if(bookingID == null) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.NOT_FOUND, 
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}

				response.getJsonContext().success(Json.object());

				return true;
			}
		});

		nh.createEndpoint("/api/booking/get-all", new IEndpointHandler() {
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
				Pair<String,AccreditationLevel> claims = jwt.checkToken(shortToken);
					
				// check error
				if(claims == null) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS, 
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}

				String userID = claims.getLeft();
				
				JsonArray bookings = Json.array();
				for(Booking booking : DBH.getInstance().getAllBooking(userID)) {
					bookings.add(booking.toJSON());
				}
				
				response.getJsonContext().success(Json.object()
					.add("bookings", bookings));

				return true;
			}
		});

		nh.createEndpoint("/api/booking/remove", new IEndpointHandler() {
			public boolean handle(NetworkData data, NetworkResponseFactory response) {
				if(!super.handle(data, response)) {
					return false;
				}

				setExpectedParams("token");
				setExpectedParams("id");

				if(!areAllParamsDefined()) {
					sendDefaultMissingParametersMessage();
					return false;
				}

				// auth with token
				String shortToken = data.getAsString("token");
				JWTH jwt = JWTH.getInstance();
				Pair<String,AccreditationLevel> claims = jwt.checkToken(shortToken);
					
				// check error
				if(claims == null) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS, 
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}

				String userID = claims.getLeft();
				
				boolean success = DBH.getInstance().removeBooking(userID, data.getAsString("id"));

				// check error
				if(!success) {
					String message = "This booking does not exist";
					response.getJsonContext().failure(NetworkResponse.ErrorCode.NOT_FOUND, 
						message, ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}
				
				response.getJsonContext().success(Json.object());

				return true;
			}
		});

		nh.createEndpoint("/api/admin/auth", new IEndpointHandler() {
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
					
					Pair<String,AccreditationLevel> claims = DBH.getInstance().authAdmin(username, password);

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
					Pair<String,AccreditationLevel> claims = jwt.checkToken(longToken);
						
					// check error
					if(claims == null) {
						Pair<String, Logging> log = ErrorHandler.getInstance().pop();
						response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS, 
							log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
						return false;
					}

					// check if the user still exist in the database
					// and have still the rights
					// before create the token
					if(!DBH.getInstance().adminStillExists(claims.getLeft())) {
						Pair<String, Logging> log = ErrorHandler.getInstance().pop();
						response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS, 
							log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
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

		nh.createEndpoint("/api/admin/item/get-all", new IEndpointHandler() {
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
				Pair<String,AccreditationLevel> claims = jwt.checkToken(shortToken);
					
				// check error
				if(claims == null) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS, 
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}

				// check rights
				if(!AccreditationLevel.isAdmin(claims.getRight())) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS, 
						"You don't have the rights", ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}
				
				JsonArray items = Json.array();
				for(ItemSoft item : DBH.getInstance().getAllItemsQuick()) {
					items.add(item.toJSON());
				}
				
				response.getJsonContext().success(Json.object()
					.add("items", items));

				return true;
			}
		});

		nh.createEndpoint("/api/admin/item/add", new IEndpointHandler() {
			public boolean handle(NetworkData data, NetworkResponseFactory response) {
				if(!super.handle(data, response)) {
					return false;
				}

				setExpectedParams("token");
				setExpectedParams("item");
				if(!areAllParamsDefined()) {
					sendDefaultMissingParametersMessage();
					return false;
				}

				// auth with token
				String shortToken = data.getAsString("token");
				JWTH jwt = JWTH.getInstance();
				Pair<String,AccreditationLevel> claims = jwt.checkToken(shortToken);
					
				// check error
				if(claims == null) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS, 
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}

				// check rights
				if(!AccreditationLevel.isAdmin(claims.getRight())) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.UNAUTHORIZED_ACCESS, 
						"You don't have the rights", ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}
				
				// get item
				String item = data.getAsString("item");
				JsonValue itemJSON = Json.parse(item);

				// get model
				ArrayList<CategoryModel> catModel = DBH.getInstance().getCategoriesModel();
				
				// create item from json
				ItemFull itemF;
				try {
					itemF = ItemFull.fromJSON(itemJSON, catModel);
				}
				catch (Exception ex) { // error
					response.getJsonContext().failure(NetworkResponse.ErrorCode.BAD_REQUEST, 
						"An error occured while parsing item data", ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}

				// add the item to the DB
				boolean result = DBH.getInstance().addItem(itemF);

				// check error
				if(!result) {
					Pair<String, Logging> log = ErrorHandler.getInstance().pop();
					response.getJsonContext().failure(NetworkResponse.ErrorCode.NOT_FOUND, 
						log.getRight().toString(), ErrorCode.DEFAULT_ERROR_CODE);
					return false;
				}
				
				response.getJsonContext().success(Json.object());

				return true;
			}
		});
	}
}