package locomotor.core;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;
import java.util.ListIterator;

import locomotor.components.models.CategoryModel;
import locomotor.components.models.CriteriaModel;
import locomotor.components.models.Item;
import locomotor.components.models.ItemCategory;
import locomotor.components.models.ItemCriteria;

import locomotor.components.types.CEnumItemType;
import locomotor.components.types.CEnumUniverseType;
import locomotor.components.types.CEnumUserType;
import locomotor.components.types.CItemType;
import locomotor.components.types.CUniverseType;
import locomotor.components.types.TypeFactory;

import org.bson.Document;
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

				System.out.println("Retrieving the criterias of " + name);
				
				TypeFactory typeFactory = new TypeFactory();

				for(Document crit : critIt) {

					System.out.println("Retrieving the criteria " + crit.getString("name"));

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
						universe
					);

					criteriasMod.add(criteria);

					System.out.print(criteria);

				}

				CategoryModel catModel = new CategoryModel(id, name, criteriasMod);

				listCatMod.add(catModel);

			}
		});

		return listCatMod;
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
		
		System.out.println("Retrieving the items");

		items.forEach(new Block<Document>() {
			@Override
			public void apply(final Document doc) {

				String id = doc.getObjectId("_id").toString();
				ArrayList<Document> catIt = (ArrayList<Document>)doc.get("categories");

				ArrayList<ItemCategory> categories = new ArrayList<ItemCategory>();

				System.out.println("Retrieving the categories of " + doc.getString("name"));
				
				TypeFactory typeFactory = new TypeFactory();

				// iterator to iterate simultaneously
				ListIterator<CategoryModel> itCatMod = catModel.listIterator();
				ListIterator<Document> itCat = catIt.listIterator();

				while(itCatMod.hasNext() && itCat.hasNext()) {

					CategoryModel currentCatMod = itCatMod.next();
					Document currentCat = itCat.next();

					String identifierCat = currentCat.getObjectId("categoryModel").toString();
					System.out.println("Category " + identifierCat);

					// check category model
					if(!currentCatMod.getID().equals(identifierCat)) {
						// error
						System.err.println("Error: Category model does not match current category");
						System.err.println(currentCat);
						System.err.println(currentCatMod);
						System.exit(0);
					}

					ArrayList<ItemCriteria> criterias = new ArrayList<ItemCriteria>();
					ArrayList<Document> critIt = (ArrayList<Document>)currentCat.get("criteria");
					ArrayList<CriteriaModel> critModIt = currentCatMod.getCriterias();

					// iterator to iterate simultaneously
					ListIterator<CriteriaModel> itCriMod = critModIt.listIterator();
					ListIterator<Document> itCrit = critIt.listIterator();

					while(itCriMod.hasNext() && itCrit.hasNext()) {

						CriteriaModel currentCritMod = itCriMod.next();
						Document currentCrit = itCrit.next();

						String identifierCrit = currentCrit.getObjectId("criterionModel").toString();
						System.out.println("Criterion " + identifierCrit);

						// check criteria model
						if(!currentCritMod.getID().equals(identifierCrit)) {
							// error
							System.err.println("Error: Criteria model does not match current criteria");
							System.err.println(currentCrit);
							System.err.println(currentCritMod);
							System.exit(0);
						}

						// creation criteria
						CItemType value = typeFactory.getItem(currentCritMod.getItemType(),
							currentCrit.get("value"), currentCritMod.getUniverse());

						ItemCriteria criteria = new ItemCriteria(
							identifierCrit,
							value
						);

						criterias.add(criteria);

						System.out.println(criteria);

					}

					ItemCategory category = new ItemCategory(
						identifierCat, criterias
					);

					categories.add(category);

				}

				Item item = new Item(id, categories);

				listItems.add(item);

			}
		});

		return listItems;

	}

	/**
	 * Creates an user.
	 *
	 * @param      username  The username
	 * @param      password  The password
	 */
	public static void createUser(String username, String password) {
		MongoCollection<Document> users = md.getCollection("users");
		Document userAlreadyExists = users.find(eq("username", username)).first();
		
		if (userAlreadyExists != null) {
			// @todo: handle username already exists
			System.out.println(userAlreadyExists.toJson());
			return;
		}
			
		Document user = new Document();
		user.append("username", username.trim());
		user.append("password", "");
		user.append("isAdmin", false);
		user.append("notifications", new ArrayList<Document>());
		users.insertOne(user);
			
		ObjectId id = (ObjectId)user.get("_id");
		String passwordHash = "";
			
		try {

			passwordHash = PasswordStorage.createHash(password + id.toString());

		}
		catch(Exception ex) {
	
			// delete the user from the database
			users.deleteOne(eq("_id", id));
			// @todo: handle exception
			System.out.println("ERROR: " + ex);
			System.exit(1);

		}
		
		// add hashed password
		users.updateOne(eq("_id", id), set("password", passwordHash));

		// @todo: return OK
	}

	/**
	 * Authentificate an user with his username and password.
	 *
	 * @param      username  The username
	 * @param      password  The password
	 */
	public static void authUser(String username, String password) {

		MongoCollection<Document> users = md.getCollection("users");
		Document user = users.find(eq("username", username)).first();
		
		if (user == null) {
			// @todo: handle user with that username does not exist
			return;
		}
		
		ObjectId id = (ObjectId)user.get("_id");
		String correctHash = user.getString("password");
		Boolean isPasswordValid = null;

		try {
			
			isPasswordValid = PasswordStorage.verifyPassword(password + id.toString(), correctHash);
		
		}
		catch(Exception ex) {
			
			// @todo: handle exception
			System.out.println("ERROR: " + ex);
			System.exit(1);

		}

		if (!isPasswordValid) {
			// @todo: handle password is not valid
			return;
		}

		// @todo: check pending notifications
		// @todo: return OK

	}


}