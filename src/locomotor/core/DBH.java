package locomotor.core;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.ne;
import static com.mongodb.client.model.Updates.set;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;

import locomotor.components.Pair;

import locomotor.components.logging.ErrorHandler;

import locomotor.components.models.Booking;
import locomotor.components.models.CategoryModel;
import locomotor.components.models.Criteria;
import locomotor.components.models.CriteriaModel;
import locomotor.components.models.Item;
import locomotor.components.models.ItemCategory;
import locomotor.components.models.ItemCategoryFull;
import locomotor.components.models.ItemCriteria;
import locomotor.components.models.ItemCriteriaFull;
import locomotor.components.models.ItemFull;
import locomotor.components.models.ItemSoft;
import locomotor.components.models.Unit;
import locomotor.components.models.UnitAlt;
import locomotor.components.models.Universe;

import locomotor.components.types.CEnumItemType;
import locomotor.components.types.CEnumUniverseType;
import locomotor.components.types.CEnumUserType;
import locomotor.components.types.CItemType;
import locomotor.components.types.CUniverseType;
import locomotor.components.types.TypeFactory;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 * Singleton class for handling database connection and requests.
 */
public class DBH {

	/**
	 * Singleton database handler object, with lazy instanciation.
	 */
	private static DBH db = null;

	/**
	 * Singleton client object, with lazy instanciation.
	 */
	private static MongoClient mc = null;

	/**
	 * Singleton database object, with lazy instanciation.
	 */
	private static MongoDatabase md;

	/**
	 * Private constructor to prevent any other class to instantiate.
	 */
	private DBH() {}

	/**
	 * Gets the instance.
	 *
	 * @return     The instance.
	 */
	public static synchronized DBH getInstance() {
		if(db == null) {
			db = new DBH();
		}
		return db;
	}

	/**
	 * Connect to the default database.
	 */
	public static synchronized void connect() {
		if(mc == null) {
			mc = new MongoClient();
		}
		else {
			// disconnect first
			disconnect();
			mc = new MongoClient();
		}
		System.out.println("Connected to the database");
	}

	/**
	 * Connect to the database.
	 *
	 * @param      ip    The IP address
	 * @param      port  The port
	 */
	public static synchronized void connect(String ip, int port) {
		if(mc == null) {
			mc = new MongoClient(ip, port);
		}
		else {
			// disconnect first
			disconnect();
			mc = new MongoClient(ip, port);
		}
		System.out.println("Connected to the database");
	}

	/**
	 * Disconnect from the database.
	 */
	public static synchronized void disconnect() {
		mc.close();
		mc = null;
		System.out.println("Disconnected from the database");
	}

	/**
	 * Connects to database.
	 *
	 * @param      databaseName  The database name
	 */
	public static synchronized void connectToDatabase(String databaseName) {
		md = mc.getDatabase(databaseName);
	}

	/**
	 * Gets the categories model.
	 *
	 * @return     The categories model.
	 */
	public static ArrayList<CategoryModel> getCategoriesModel() {
		ArrayList<CategoryModel> listCatMod = new ArrayList<CategoryModel>();
		
		FindIterable<Document> catMod = md.getCollection("categoryModel").find();
		
		System.out.println("Retrieving the model categories");

		catMod.forEach(new Block<Document>() {
			@Override
			public void apply(final Document doc) {
				
				String id = doc.getObjectId("_id").toString();
				String name = doc.getString("name");
				ArrayList<Document> critIt = (ArrayList<Document>)doc.get("criteria");

				ArrayList<CriteriaModel> criteriasMod = new ArrayList<CriteriaModel>();

				// System.out.println("Retrieving the criterias of " + name);
				
				TypeFactory typeFactory = new TypeFactory();

				for(Document crit : critIt) {

					// System.out.println("Retrieving the criteria " + crit.getString("name"));

					CUniverseType universe = typeFactory.getUniverse(CEnumUniverseType.valueOf(
						crit.getInteger("universeType")), crit.get("universe"));

					CriteriaModel criteria = new CriteriaModel(
						crit.getObjectId("_id").toString(),
						crit.getString("name"),
						crit.getString("question"),
						crit.getBoolean("isComparable"),
						CEnumUniverseType.valueOf(crit.getInteger("universeType")),
						CEnumItemType.valueOf(crit.getInteger("itemType")),
						CEnumUserType.valueOf(crit.getInteger("userType")),
						crit.get("unit").toString(),
						universe
					);

					criteriasMod.add(criteria);

					// System.out.print(criteria);

				}

				CategoryModel catModel = new CategoryModel(id, name, criteriasMod);

				listCatMod.add(catModel);

			}
		});

		return listCatMod;
	}

	/**
	 * Check if the username is already taken by another user.
	 *
	 * @param      username  The username
	 *
	 * @return     True if username is taken, false otherwise.
	 */
	public static boolean usernameAlreadyTaken(String username) {
		MongoCollection<Document> users = md.getCollection("users");
		Document userAlreadyExists = users.find(eq("username", username)).first();
		return (userAlreadyExists != null);
	}

	/**
	 * Check if the user still exists
	 *
	 * @param      id    The identifier
	 *
	 * @return     True if exists, false otherwise.
	 */
	public static boolean userStillExists(String id) {
		MongoCollection<Document> users = md.getCollection("users");
		BasicDBObject queryUser = new BasicDBObject();
		queryUser.put("_id", new ObjectId(id));
		Document userExists = users.find(queryUser).first();
		return (userExists != null);
	}

	/**
	 * Register an user.
	 *
	 * @param      username       The username
	 * @param      password       The password
	 * @param      accreditation  The accreditation level
	 *
	 * @return     The ObjectID of the user and his accreditation level
	 */
	public static Pair<String, AccreditationLevel> registerUser(
		String username, 
		String password, 
		AccreditationLevel accreditation) {		
		// check already exist
		if (DBH.getInstance().usernameAlreadyTaken(username)) {
			ErrorHandler.getInstance().push("registerUser", true, "The username is already taken by another user", "");
			return null;
		}
			
		Document user = new Document();
		user.append("username", username.trim());
		user.append("password", "");
		user.append("isAdmin", accreditation.getValue());
		user.append("notifications", new ArrayList<Document>());
		user.append("bookings", new ArrayList<Document>());
		MongoCollection<Document> users = md.getCollection("users");
		users.insertOne(user);
			
		ObjectId id = user.getObjectId("_id");
		String passwordHash = "";
			
		try {

			passwordHash = PasswordStorage.createHash(password + id.toString());

		}
		catch(Exception ex) {
	
			// delete the user from the database
			users.deleteOne(eq("_id", id));
			String message = "An error occurred while processing the registration. Please retry.";
			ErrorHandler.getInstance().push("registerUser", true, message, "");
			return null;

		}
		
		// add hashed password
		users.updateOne(eq("_id", id), set("password", passwordHash));

		return new Pair(id.toString(), AccreditationLevel.valueOf(user.getInteger("isAdmin").intValue()));
	}

	/**
	 * Authentificate an user with his username and password.
	 *
	 * @param      username  The username
	 * @param      password  The password
	 *
	 * @return     The ObjectID of the user and his role
	 */
	public static Pair<String,AccreditationLevel> authUser(String username, String password) {

		MongoCollection<Document> users = md.getCollection("users");
		Document user = users.find(eq("username", username)).first();
		
		// no user with that username
		if (user == null) {
			String messageGen = "The username or the password is not correct";
			String messageCont = "The username does not exist";
			ErrorHandler.getInstance().push("authUser", true, messageGen, messageCont);
			return null;
		}
		
		ObjectId id = user.getObjectId("_id");
		String correctHash = user.getString("password");
		Boolean isPasswordValid = null;

		try {
			
			isPasswordValid = PasswordStorage.verifyPassword(password + id.toString(), correctHash);
		
		}
		catch(Exception ex) {
			
			// bad password
			String message = "Error while hashing the password";
			ErrorHandler.getInstance().push("authUser", true, message, "");
			return null;

		}

		if (!isPasswordValid) {
			String messageGen = "The username or the password is not correct";
			String messageCont = "The password is not correct";
			ErrorHandler.getInstance().push("authUser", true, messageGen, messageCont);
			return null;
		}

		return new Pair(id.toString(), AccreditationLevel.valueOf(user.getInteger("isAdmin").intValue()));

	}

	/**
	 * Gets the user information.
	 *
	 * @param      userID  The user id
	 *
	 * @return     The user information.
	 */
	public static HashMap<String, Object> getUserInfo(String userID) {
		MongoCollection<Document> users = md.getCollection("users");
		
		// filter for query
		Bson filter = Filters.eq("_id", new ObjectId(userID));
		Document user = users.find(filter).first();

		if (user == null) {
			String messageGen = "This user does not exist";
			String messageCont = "The identifier is not valid";
			ErrorHandler.getInstance().push("userNotExist", true, messageGen, messageCont);
			return null;
		}

		HashMap<String, Object> info = new HashMap();
		info.put("username", user.getString("username"));
		info.put("adminLevel", user.getInteger("isAdmin"));
		return info;
	}

	/**
	 * Change the username of the user.
	 *
	 * @param      userID       The user id
	 * @param      newUsername  The new username
	 *
	 * @return     True if succeed, false otherwise.
	 */
	public static boolean changeUsername(String userID, String newUsername) {
		MongoCollection<Document> users = md.getCollection("users");
		// filter for query
		Bson filter = Filters.eq("_id", new ObjectId(userID));
		Document user = users.find(filter).first();

		if(user == null) {
			String messageGen = "This user does not exist";
			String messageCont = "The identifier is not valid";
			ErrorHandler.getInstance().push("userNotExist", true, messageGen, messageCont);
			return false;
		}

		// check if username if available
		if(usernameAlreadyTaken(newUsername)) {
			String messageGen = "This username is not available";
			String messageCont = "This username is not available";
			ErrorHandler.getInstance().push("usernameNotAvailable", true, messageGen, messageCont);
			return false;
		}

		// update
		Document update = new Document("$set", new Document("username", newUsername));
		users.updateOne(filter, update);
		return true;
	}

	/**
	 * Change the password of the user.
	 *
	 * @param      userID       The user id
	 * @param      oldPassword  The old password
	 * @param      newPassword  The new password
	 *
	 * @return     True if succeed, false otherwise.
	 */
	public static boolean changePassword(String userID, String oldPassword, String newPassword) {
		MongoCollection<Document> users = md.getCollection("users");
		// filter for query
		Bson filter = Filters.eq("_id", new ObjectId(userID));
		Document user = users.find(filter).first();

		if(user == null) {
			String messageGen = "This user does not exist";
			String messageCont = "The identifier is not valid";
			ErrorHandler.getInstance().push("userNotExist", true, messageGen, messageCont);
			return false;
		}

		// check if old password is valid
		String correctHash = user.getString("password");
		Boolean isOldPasswordValid = null;

		try {
			
			isOldPasswordValid = PasswordStorage.verifyPassword(oldPassword + userID, correctHash);
		
		}
		catch(Exception ex) {
			
			// bad password
			String message = "Error while hashing the password";
			ErrorHandler.getInstance().push("changePassword", true, message, "");
			return false;

		}

		if (!isOldPasswordValid) {
			String messageGen = "The old password is not correct";
			String messageCont = "The old password is not correct";
			ErrorHandler.getInstance().push("changePassword", true, messageGen, messageCont);
			return false;
		}

		// old password is ok, hash the new password and store it
		String passwordHash = "";
		try {

			passwordHash = PasswordStorage.createHash(newPassword + userID);

		}
		catch(Exception ex) {
	
			// delete the user from the database
			String message = "An error occurred while processing the modification of the new password. Please retry.";
			ErrorHandler.getInstance().push("changePassword", true, message, "");
			return false;

		}

		// update
		Document update = new Document("$set", new Document("password", passwordHash));
		users.updateOne(filter, update);
		return true;
	}

	/**
	 * Removes an user.
	 *
	 * @param      userID    The user id
	 * @param      password  The password
	 *
	 * @return     True if succeed, false otherwise.
	 */
	public static boolean removeUser(String userID, String password) {
		MongoCollection<Document> users = md.getCollection("users");
		// filter for query
		Bson filter = Filters.eq("_id", new ObjectId(userID));
		Document user = users.find(filter).first();

		if(user == null) {
			String messageGen = "This user does not exist";
			String messageCont = "The identifier is not valid";
			ErrorHandler.getInstance().push("userNotExist", true, messageGen, messageCont);
			return false;
		}

		// check if password is valid
		String correctHash = user.getString("password");
		Boolean isPasswordValid = null;

		try {
			
			isPasswordValid = PasswordStorage.verifyPassword(password + userID, correctHash);
		
		}
		catch(Exception ex) {
			
			// bad password
			String message = "Error while hashing the password";
			ErrorHandler.getInstance().push("changePassword", true, message, "");
			return false;

		}

		if (!isPasswordValid) {
			String messageGen = "The password is not correct";
			String messageCont = "The password is not correct";
			ErrorHandler.getInstance().push("changePassword", true, messageGen, messageCont);
			return false;
		}

		// old password is ok, then remove the user from the matrix
		users.deleteOne(filter);
		return true;
	}

	/**
	 * Gets all units.
	 *
	 * @return     All units.
	 */
	public static ArrayList<Unit> getAllUnits() {
		ArrayList<Unit> units = new ArrayList();

		FindIterable<Document> unitsMod = md.getCollection("units").find();
		unitsMod.forEach(new Block<Document>() {
			@Override
			public void apply(final Document doc) {
				ArrayList<Document> unitAltMod = (ArrayList<Document>)doc.get("alt");

				// create the alternate form unit
				ArrayList<UnitAlt> unitAlt = new ArrayList();
				for (Document alt : unitAltMod) {
					
					// construct the unit form & add to the list
					UnitAlt ua = new UnitAlt(
						alt.getString("name"),
						alt.getString("long"),
						alt.getDouble("factor")
					);
					unitAlt.add(ua);
				}

				// construct the unit & add to the list
				Unit unit = new Unit(
					doc.getObjectId("_id").toString(),
					doc.getString("unitname"),
					unitAlt
				);
				units.add(unit);

			}
		});

		return units;
	}

	/**
	 * Gets the item.
	 *
	 * @param      itemID    The item id
	 * @param      catModel  The cat model
	 *
	 * @return     The item.
	 */
	public static ItemFull getItem(String itemID, ArrayList<CategoryModel> catModel) {

		MongoCollection<Document> items = md.getCollection("items");
		BasicDBObject queryItem = new BasicDBObject();
		Document item = null;

		try {
			
			queryItem.put("_id", new ObjectId(itemID));
			item = items.find(queryItem).first();

		}
		catch (Exception ex) {
			String messageGen = "This item does not exist";
			String messageCont = "The identifier is not valid";
			ErrorHandler.getInstance().push("itemNotExist", true, messageGen, messageCont);
			return null;
		}

		String name = item.get("name").toString();
		String universeID = item.get("universe").toString();
		String description = item.get("description").toString();
		String image = item.get("image").toString();
		ArrayList<ItemCategoryFull> categories = new ArrayList();

		// categories & criterias model map
		HashMap<String, CategoryModel> categoriesModelMap = new HashMap();
		HashMap<String, CriteriaModel> criteriasModelMap = new HashMap();
		for (CategoryModel cm : catModel) {
			categoriesModelMap.put(cm.getID(), cm);
			for (CriteriaModel cmm : cm.getCriterias()) {
				criteriasModelMap.put(cmm.getID(), cmm);
			}
		}
		
		TypeFactory typeFactory = new TypeFactory();
		ArrayList<Document> catIt = (ArrayList<Document>)item.get("categories");
		
		// iterate over the categories
		for (Document cat : catIt) {
			
			// create criterias of the category
			ArrayList<ItemCriteriaFull> criterias = new ArrayList();
			ArrayList<Document> critIt = (ArrayList<Document>)cat.get("criteria");

			// iterate over the criterias
			for (Document cri : critIt) {

				String id = cri.get("criterionModel").toString();
				CriteriaModel critMod = criteriasModelMap.get(id);
				String nameCrit = critMod.getName();

				// creation criteria
				CItemType value = typeFactory.getItem(critMod.getItemType(),
							cri.get("value"), critMod.getUniverse());

				ItemCriteriaFull criteria = new ItemCriteriaFull(id, nameCrit, value);
				criterias.add(criteria);
			}

			String id = cat.get("categoryModel").toString();
			String nameCat = categoriesModelMap.get(id).getName();
			ItemCategoryFull category = new ItemCategoryFull(id, nameCat, criterias);
			categories.add(category);
		}

		return new ItemFull(itemID, name, universeID, description, image, categories);

	}

	/**
	 * Gets all items quick.
	 *
	 * @return     All items quick.
	 */
	public static ArrayList<ItemSoft> getAllItemsQuick() {

		ArrayList<ItemSoft> listItems = new ArrayList();
		
		FindIterable<Document> items = md.getCollection("items").find();
		
		MongoCollection<Document> universes = md.getCollection("universes");
		HashMap<ObjectId,String> universeMap = new HashMap();

		items.forEach(new Block<Document>() {
			@Override
			public void apply(final Document doc) {

				String id = doc.getObjectId("_id").toString();
				String name = doc.getString("name");
				String image = doc.getString("image");
				ObjectId universeID = doc.getObjectId("universe");

				// check if already retrieve the name, else get it
				String universeName = universeMap.get(universeID);
				if (universeName == null) {
					BasicDBObject queryUniverse = new BasicDBObject();
					queryUniverse.put("_id", universeID);
					universeName = universes.find(queryUniverse).first().getString("name");
				}

				listItems.add(new ItemSoft(id, name, image, universeName));

			}
		});
		return listItems;
	}

	/**
	 * Gets the items.
	 *
	 * @param      catModel  The categories model
	 *
	 * @return     The items.
	 */
	public static ArrayList<Item> getItems(ArrayList<CategoryModel> catModel) {
	
		ArrayList<Item> listItems = new ArrayList<Item>();
		
		FindIterable<Document> items = md.getCollection("items").find();
		
		// System.out.println("Retrieving the items");

		items.forEach(new Block<Document>() {
			@Override
			public void apply(final Document doc) {

				ArrayList<Document> catIt = (ArrayList<Document>)doc.get("categories");

				HashMap<String, Document> categoriesItemMap = new HashMap<String, Document>();
				for (Document d : catIt) {
					categoriesItemMap.put(d.getObjectId("categoryModel").toString(), d);
				}

				ArrayList<ItemCategory> categories = new ArrayList<ItemCategory>();

				// System.out.println("Retrieving the categories of " + doc.getString("name"));
				
				TypeFactory typeFactory = new TypeFactory();

				// iterator
				ListIterator<CategoryModel> itCatMod = catModel.listIterator();

				while(itCatMod.hasNext()) {

					CategoryModel currentCatMod = itCatMod.next();
					// not found yet
					Document currentCat = categoriesItemMap.get(currentCatMod.getID());

					String identifierCat = currentCat.getObjectId("categoryModel").toString();
					// System.out.println("Category " + identifierCat);

					ArrayList<ItemCriteria> criterias = new ArrayList<ItemCriteria>();
					ArrayList<Document> critIt = (ArrayList<Document>)currentCat.get("criteria");

					HashMap<String, Document> criteriasItemMap = new HashMap<String, Document>();
					for (Document d : critIt) {
						criteriasItemMap.put(d.getObjectId("criterionModel").toString(), d);
					}

					ArrayList<CriteriaModel> critModIt = currentCatMod.getCriterias();

					// iterator to iterate simultaneously
					ListIterator<CriteriaModel> itCriMod = critModIt.listIterator();

					while(itCriMod.hasNext()) {

						CriteriaModel currentCritMod = itCriMod.next();
						// not found yet
						Document currentCrit = criteriasItemMap.get(currentCritMod.getID());

						String identifierCrit = currentCrit.getObjectId("criterionModel").toString();
						// System.out.println("Criterion " + identifierCrit);

						// creation criteria
						CItemType value = typeFactory.getItem(currentCritMod.getItemType(),
							currentCrit.get("value"), currentCritMod.getUniverse());

						ItemCriteria criteria = new ItemCriteria(
							identifierCrit,
							value
						);

						criterias.add(criteria);

						// System.out.println(criteria);

					}

					ItemCategory category = new ItemCategory(
						identifierCat, criterias
					);

					categories.add(category);

				}

				String id = doc.getObjectId("_id").toString();
				Item item = new Item(id, categories);

				listItems.add(item);

			}
		});

		return listItems;

	}

	/**
	 * Gets the partial information of item.
	 *
	 * @param      itemID  The item id
	 *
	 * @return     The partial information of item, name, image (URL) and universe name.
	 */
	public static HashMap<String, Object> getPartialInfoOfItem(String itemID) {

		MongoCollection<Document> items = md.getCollection("items");
		MongoCollection<Document> universes = md.getCollection("universes");

		// @todo: check items & universes still exists
		
		BasicDBObject queryItem = new BasicDBObject();
		queryItem.put("_id", new ObjectId(itemID));
		Document item = items.find(queryItem).first();
		ObjectId universeID = (ObjectId)item.get("universe");
		BasicDBObject queryUniverse = new BasicDBObject();
		queryUniverse.put("_id", universeID);
		Document universe = universes.find(queryUniverse).first();

		HashMap<String, Object> partialInfo = new HashMap();
		// name & image
		partialInfo.put("itemName", item.get("name"));
		partialInfo.put("itemImageURL", item.get("image"));
		// universe
		partialInfo.put("universeName", universe.get("name"));

		return partialInfo;
	}

	/**
	 * Adds an item.
	 *
	 * @param      item  The item
	 *
	 * @return     True if added, false otherwise.
	 */
	public static boolean addItem(ItemFull item) {
		System.out.println("Adding the item");
		Document itemToAdd = DBH.getInstance().checkingParsingItem(item);

		// check error
		if(itemToAdd == null) {
			return false;
		}

		MongoCollection<Document> items = md.getCollection("items");
		items.insertOne(itemToAdd);
		System.out.println("Item added");
		return true;
	}

	/**
	 * Update values of an item.
	 *
	 * @param      item  The item
	 *
	 * @return     True if succeed, else otherwise.
	 */
	public static boolean updateItem(ItemFull item) {
		System.out.println("Updating the item");
		Document itemToUpdate = DBH.getInstance().checkingParsingItem(item);

		// check error
		if(itemToUpdate == null) {
			return false;
		}

		String itemID = item.getID();
		Bson filter = Filters.eq("_id", new ObjectId(itemID));
		Document update = new Document("$set", itemToUpdate);
		MongoCollection<Document> items = md.getCollection("items");
		items.updateOne(filter, update);
		System.out.println("Item updated");
		return true;
	}

	/**
	 * Checking and parsing item before add/update.
	 *
	 * @param      item  The item
	 *
	 * @return     The document parsed/checked
	 */
	private Document checkingParsingItem(ItemFull item) {

		String messageGen = "";
		String messageCont = "";
		boolean isError = false;
		
		// check universe exists
		String universeID = item.getUniverse();
		if(!DBH.getInstance().universeStillExists(universeID)) {
			messageGen = "The universe does not exist";
			messageCont = "The universe is not valid";
			isError = true;
		}

		// check name if not taken yet
		String id = item.getID();
		String name = item.getName();
		if (DBH.getInstance().itemNameAlreadyTaken(id, name)) {
			messageGen = "The name of the item is already taken";
			messageCont = "The name of the item is already taken";
			isError = true;
		}
		
		// check if error
		if (isError) {
			ErrorHandler.getInstance().push("checkingParsingItem", true, messageGen, messageCont);
			return null;
		}

		// create the item
		Document itemToAdd = new Document();
		itemToAdd.append("name", name);
		itemToAdd.append("universe", new ObjectId(universeID));
		itemToAdd.append("description", item.getDescription());
		itemToAdd.append("image", item.getImage());
		// the categories to add
		ArrayList<Document> categoriesToAdd = new ArrayList();

		ArrayList<CategoryModel> catMod = DBH.getInstance().getCategoriesModel();
		
		// map criterias for easy retrieve
		HashMap<String, CriteriaModel> criteriasModMap = new HashMap();
		for (CategoryModel cm : catMod) {
			for (CriteriaModel cmm : cm.getCriterias()) {
				criteriasModMap.put(cmm.getID(), cmm);
			}
		}

		TypeFactory typeFactory = new TypeFactory();

		for (ItemCategory category : item.getCategories()) {
			ItemCategoryFull icf = (ItemCategoryFull)category;

			// the criterias of the current category
			ArrayList<Document> criteriasToAdd = new ArrayList();

			for (Criteria criteria : icf.getCriterias()) {
				ItemCriteriaFull icrf = (ItemCriteriaFull)criteria;
				CriteriaModel cmCurr = criteriasModMap.get(icrf.getID());

				// get the value
				Object value = typeFactory.getItem(cmCurr.getItemType(), icrf.getValue(), cmCurr.getUniverse());
				
				Document criteriaToAdd = new Document();
				criteriaToAdd.append("criterionModel", new ObjectId(icrf.getID()));
				criteriaToAdd.append("value", value);
				criteriasToAdd.add(criteriaToAdd);

			}

			Document categoryToAdd = new Document();
			categoryToAdd.append("categoryModel", new ObjectId(icf.getID()));
			categoryToAdd.append("criteria", criteriasToAdd);
			categoriesToAdd.add(categoryToAdd);
		}

		itemToAdd.append("categories", categoriesToAdd);
		return itemToAdd;
	}

	/**
	 * Check if the name is already taken by another item.
	 *
	 * @param      id    The identifier
	 * @param      name  The name
	 *
	 * @return     True if name is taken, false otherwise.
	 */
	public static boolean itemNameAlreadyTaken(String id, String name) {
		MongoCollection<Document> items = md.getCollection("items");

		Bson filter = Filters.eq("name", name);
		
		// with id too
		if (id != "") {
			Bson filterID = Filters.ne("_id", new ObjectId(id));
			filter = Filters.and(filter, filterID);
		}

		Document itemAlreadyExists = items.find(filter).first();;
		return (itemAlreadyExists != null);
	}

	/**
	 * Removes an item.
	 *
	 * @param      itemID  The item id
	 *
	 * @return     True if succeed, false otherwise.
	 */
	public static boolean removeItem(String itemID) {
		System.out.println("Removing the item");
		MongoCollection<Document> items = md.getCollection("items");
		// filter for query
		Bson filter = Filters.eq("_id", new ObjectId(itemID));
		Document item = items.find(filter).first();

		if(item == null) {
			String messageGen = "This item does not exist";
			String messageCont = "The identifier is not valid";
			ErrorHandler.getInstance().push("itemNotExist", true, messageGen, messageCont);
			return false;
		}

		// delete related bookings
		DBH.getInstance().deleteBookingWith(itemID);

		// remove the item from the matrix
		items.deleteOne(filter);
		System.out.println("Item removed");
		return true;
	}

	/**
	 * Delete booking with the specified ID.
	 *
	 * @param      itemID  The item id
	 */
	private static void deleteBookingWith(String itemID) {

		MongoCollection<Document> users = md.getCollection("users");
		Bson filter = Filters.eq("bookings.itemID", new ObjectId(itemID));
		FindIterable<Document> user = users.find(filter);

		// for each user that have this item booked
		user.forEach(new Block<Document>() {
			@Override
			public void apply(final Document doc) {
				ArrayList<Document> bookings = (ArrayList<Document>)doc.get("bookings");
				ArrayList<Document> toDelete = new ArrayList();

				// delete them
				for (int i = 0; i < bookings.size(); i++) {
					Document booking = bookings.get(i);
					if(booking.getObjectId("itemID").toString().equals(itemID)) {
						bookings.remove(booking);
					}
				}

				// update it
				Bson filterUser = Filters.eq("_id", doc.getObjectId("_id"));
				Document update = new Document("$set", new Document("bookings", bookings));
				users.updateOne(filterUser, update);
			}

		});

	}

	/**
	 * Gets the universe.
	 *
	 * @param      universeID  The universe id
	 *
	 * @return     The universe.
	 */
	public static Universe getUniverse(String universeID) {
		MongoCollection<Document> universes = md.getCollection("universes");
		BasicDBObject queryUniverse = new BasicDBObject();
		
		try {
			
			queryUniverse.put("_id", new ObjectId(universeID));
			Document universe = universes.find(queryUniverse).first();
			return new Universe(
				universeID,
				universe.get("name").toString(),
				universe.get("description").toString(),
				universe.get("image").toString()
			);

		}
		catch (Exception ex) {
			String messageGen = "This universe does not exist";
			String messageCont = "The identifier is not valid";
			ErrorHandler.getInstance().push("universeNotExist", true, messageGen, messageCont);
			return null;
		}
	}

	/**
	 * Check if the universe still exists.
	 *
	 * @param      id    The identifier
	 *
	 * @return     True if exists, false otherwise.
	 */
	public static boolean universeStillExists(String id) {
		MongoCollection<Document> universes = md.getCollection("universes");
		BasicDBObject queryUniverse = new BasicDBObject();
		queryUniverse.put("_id", new ObjectId(id));
		Document universeExists = universes.find(queryUniverse).first();
		return (universeExists != null);
	}

	/**
	 * Gets all universes quick.
	 *
	 * @return     All universes quick.
	 */
	public static ArrayList<Universe> getAllUniversesQuick() {

		ArrayList<Universe> listUniverses = new ArrayList();
		
		FindIterable<Document> universes = md.getCollection("universes").find();
		
		universes.forEach(new Block<Document>() {
			@Override
			public void apply(final Document doc) {

				String id = doc.getObjectId("_id").toString();
				String name = doc.getString("name");
				String description = doc.getString("description");
				String image = doc.getString("image");

				listUniverses.add(new Universe(id, name, description, image));

			}
		});
		return listUniverses;
	}

	/**
	 * Adds an universe.
	 *
	 * @param      universe  The universe
	 *
	 * @return     True if added, false otherwise.
	 */
	public static boolean addUniverse(Universe universe) {
		System.out.println("Adding the universe");
		Document universeToAdd = DBH.getInstance().checkingParsingUniverse(universe);

		// check error
		if(universeToAdd == null) {
			return false;
		}

		MongoCollection<Document> universes = md.getCollection("universes");
		universes.insertOne(universeToAdd);
		System.out.println("Universe added");
		return true;
	}

	/**
	 * Update values of an universe.
	 *
	 * @param      universe  The universe
	 *
	 * @return     True if succeed, else otherwise.
	 */
	public static boolean updateUniverse(Universe universe) {
		System.out.println("Updating the universe");
		Document universeToUpdate = DBH.getInstance().checkingParsingUniverse(universe);

		// check error
		if(universeToUpdate == null) {
			return false;
		}

		String universeID = universe.getID();
		Bson filter = Filters.eq("_id", new ObjectId(universeID));
		Document update = new Document("$set", universeToUpdate);
		MongoCollection<Document> universes = md.getCollection("universes");
		universes.updateOne(filter, update);
		System.out.println("Universe updated");
		return true;
	}

	/**
	 * Checking and parsing universe before add/update.
	 *
	 * @param      universe  The universe
	 *
	 * @return     The document parsed/checked
	 */
	private Document checkingParsingUniverse(Universe universe) {

		String messageGen = "";
		String messageCont = "";
		boolean isError = false;

		// check name if not taken yet
		String id = universe.getID();
		String name = universe.getName();
		if (DBH.getInstance().universeNameAlreadyTaken(id, name)) {
			messageGen = "The name of the universe is already taken";
			messageCont = "The name of the universe is already taken";
			isError = true;
		}
		
		// check if error
		if (isError) {
			ErrorHandler.getInstance().push("checkingParsingUniverse", true, messageGen, messageCont);
			return null;
		}

		// create the item
		Document universeToAdd = new Document();
		universeToAdd.append("name", name);
		universeToAdd.append("description", universe.getDescription());
		universeToAdd.append("image", universe.getImage());
		
		return universeToAdd;
	}

	/**
	 * Check if the name is already taken by another universe.
	 *
	 * @param      id    The identifier
	 * @param      name  The name
	 *
	 * @return     True if name is taken, false otherwise.
	 */
	public static boolean universeNameAlreadyTaken(String id, String name) {
		MongoCollection<Document> universes = md.getCollection("universes");

		Bson filter = Filters.eq("name", name);
		
		// with id too
		if (id != "") {
			Bson filterID = Filters.ne("_id", new ObjectId(id));
			filter = Filters.and(filter, filterID);
		}

		Document universeAlreadyExists = universes.find(filter).first();;
		return (universeAlreadyExists != null);
	}

	/**
	 * Removes an universe.
	 *
	 * @param      universeID  The universe id
	 *
	 * @return     True if succeed, false otherwise.}
	 */
	public static boolean removeUniverse(String universeID) {
		System.out.println("Removing the universe");
		MongoCollection<Document> universes = md.getCollection("universes");
		// filter for query
		Bson filter = Filters.eq("_id", new ObjectId(universeID));
		Document universe = universes.find(filter).first();

		if(universe == null) {
			String messageGen = "This universe does not exist";
			String messageCont = "The identifier is not valid";
			ErrorHandler.getInstance().push("universeNotExist", true, messageGen, messageCont);
			return false;
		}

		// delete related items
		DBH.getInstance().deleteItemWith(universeID);

		// remove the universe from the matrix
		universes.deleteOne(filter);
		System.out.println("Universe removed");
		return true;
	}

	/**
	 * Delete item with the specified ID.
	 *
	 * @param      universeID  The item id
	 */
	private static void deleteItemWith(String universeID) {

		MongoCollection<Document> items = md.getCollection("items");
		Bson filter = Filters.eq("universe", new ObjectId(universeID));
		FindIterable<Document> item = items.find(filter);

		// for each item that belongs to the universe
		item.forEach(new Block<Document>() {
			@Override
			public void apply(final Document doc) {
				ObjectId idIdem = doc.getObjectId("_id");

				// delete booking of that item
				DBH.getInstance().deleteBookingWith(idIdem.toString());

				// delete the item
				Bson filterItem = Filters.eq("_id", idIdem);
				items.deleteOne(filterItem);
			}

		});

	}

	/**
	 * Gets all booking of the user.
	 *
	 * @param      userID  The user id
	 *
	 * @return     All booking.
	 */
	public static ArrayList<Booking> getAllBooking(String userID) {
		MongoCollection<Document> users = md.getCollection("users");

		BasicDBObject queryUser = new BasicDBObject();
		Document user = null;

		try {
			
			queryUser.put("_id", new ObjectId(userID));
			user = users.find(queryUser).first();

		}
		catch (Exception ex) {
			String messageGen = "This user does not exist";
			String messageCont = "The identifier is not valid";
			ErrorHandler.getInstance().push("userNotExist", true, messageGen, messageCont);
			return null;
		}

		ArrayList<Document> bookings = (ArrayList<Document>)user.get("bookings");
		ArrayList<Booking> bookingsFinal = new ArrayList<Booking>();

		for (Document booking : bookings) {
			
			String id = booking.getObjectId("_id").toString();
			String itemID = booking.getObjectId("itemID").toString();
			HashMap<String, Object> itemInfo = getPartialInfoOfItem(itemID);
			int qt = booking.getInteger("qt");
			long startDate = booking.getLong("startDate");
			long endDate = booking.getLong("endDate");

			bookingsFinal.add(new Booking(
				id,
				itemID,
				itemInfo.get("itemName").toString(), 
				itemInfo.get("itemImageURL").toString(),
				itemInfo.get("universeName").toString(),
				qt,
				startDate,
				endDate
				)
			);
		}

		return bookingsFinal;
	}

	/**
	 * Adds a booking.
	 *
	 * @param      userID     The user id
	 * @param      itemID     The item id
	 * @param      qt         The quantity
	 * @param      startDate  The start date
	 * @param      endDate    The end date
	 *
	 * @return     The identifier of the booking
	 */
	public static String addBooking(String userID, String itemID, int qt, long startDate, long endDate) {		
		String messageGen = "";
		String messageCont = "";
		boolean isError = false;
		// various check
		if (qt < 1) {
			isError = true;
			messageGen = "The quantity is not valid";
			messageCont = "The quantity is null or negative";
		}

		if ((startDate < 1) || (endDate < 1) || (endDate < startDate)) {
			isError = true;
			messageGen = "At least one date is not valid";
			messageCont = "At least one date is either negative or end is before start";
		}

		MongoCollection<Document> items = md.getCollection("items");
		BasicDBObject queryItem = new BasicDBObject();
		Document item = null;

		try {
			
			queryItem.put("_id", new ObjectId(itemID));
			item = items.find(queryItem).first();

		}
		catch (Exception ex) {
			isError = true;
			messageGen = "This item does not exist";
			messageCont = "The identifier is not valid";
		}
		
		if (isError) {
			ErrorHandler.getInstance().push("addBooking", true, messageGen, messageCont);
			return null;
		}

		MongoCollection<Document> users = md.getCollection("users");

		// filter for query
		Bson filter = Filters.eq("_id", new ObjectId(userID));
		Document user = users.find(filter).first();

		// alright
		ArrayList<Document> bookings = (ArrayList<Document>)user.get("bookings");
		if(bookings == null) {
			bookings = new ArrayList<Document>();
		}

		// new booking
		Document booking = new Document();
		booking.append("_id", new ObjectId());
		booking.append("itemID", new ObjectId(itemID));
		booking.append("qt", qt);
		booking.append("startDate", startDate);
		booking.append("endDate", endDate);
		
		// add to current list
		bookings.add(booking);

		// update data
		Document update = new Document("$set", new Document("bookings", bookings));
		users.updateOne(filter, update);
	
		return booking.getObjectId("_id").toString();
	}

	/**
	 * Removes a booking.
	 *
	 * @param      userID     The user id
	 * @param      bookingID  The booking id
	 *
	 * @return     True if remove succeed, false otherwise.
	 */
	public static boolean removeBooking(String userID, String bookingID) {
		MongoCollection<Document> users = md.getCollection("users");

		// filter for query
		Bson filter = Filters.eq("_id", new ObjectId(userID));
		Document user = users.find(filter).first();

		ArrayList<Document> bookings = (ArrayList<Document>)user.get("bookings");
		if(bookings == null) {
			return false;
		}
		
		// search
		int index = -1;
		for (index = 0; index < bookings.size(); index++) {
			Document current = bookings.get(index);
			if(current.getObjectId("_id").toString().equals(bookingID)) {
				break;
			}
		}

		// match, then remove
		if ((index != -1) && (index != bookings.size())) {
			bookings.remove(index);
		}
		else {
			return false;
		}

		// update
		Document update = new Document("$set", new Document("bookings", bookings));
		users.updateOne(filter, update);
		return true;
	}

	/**
	 * Authentificate an admin with his username and password.
	 *
	 * @param      username  The username
	 * @param      password  The password
	 *
	 * @return     The ObjectID of the user and his role
	 */
	public static Pair<String,AccreditationLevel> authAdmin(String username, String password) {
		Pair<String,AccreditationLevel> claims = DBH.getInstance().authUser(username, password);
		
		// check error
		if (claims == null) {
			return null;
		}

		// check rights
		AccreditationLevel level = claims.getRight();
		if (!AccreditationLevel.isAdmin(level)) {
			String messageGen = "You don't have the rights";
			String messageCont = "You don't have the rights";
			ErrorHandler.getInstance().push("authAdmin", true, messageGen, messageCont);
			return null;
		}

		return claims;
	}

	/**
	 * Check if the admin still exists and have the rights.
	 *
	 * @param      id    The identifier
	 *
	 * @return     True if ok, false otherwise.
	 */
	public static boolean adminStillExists(String id) {
		MongoCollection<Document> users = md.getCollection("users");
		BasicDBObject queryUser = new BasicDBObject();
		queryUser.put("_id", new ObjectId(id));
		Document userExists = users.find(queryUser).first();

		// check error
		if(userExists == null) {
			String messageGen = "The user does not exist";
			String messageCont = "The user does not exist";
			ErrorHandler.getInstance().push("authStillExists", true, messageGen, messageCont);
			return false;
		}

		AccreditationLevel level = AccreditationLevel.valueOf(userExists.getInteger("isAdmin").intValue());
		if (!AccreditationLevel.isAdmin(level)) {
			String messageGen = "You don't have the rights anymore";
			String messageCont = "You don't have the rights anymore";
			ErrorHandler.getInstance().push("authStillExists", true, messageGen, messageCont);
			return false;
		}

		return true;
	}

}