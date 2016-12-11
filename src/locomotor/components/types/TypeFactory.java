package locomotor.components.types;

import java.util.ArrayList;
import java.util.TreeMap;

import locomotor.components.Pair;

import org.bson.Document;

/**
 * Front-end to create type objects.
 */
public class TypeFactory {
	
	/**
	 * Gets the universe.
	 *
	 * @param      type    The type of universe to create
	 * @param      object  The object containing the data of the universe to create
	 *
	 * @return     The universe.
	 */
	public CUniverseType getUniverse(CEnumUniverseType type, Object object) {
		switch(type) {
			case BOOLEAN: {
				Boolean testvar = null;
				return new CBoolean(testvar);
			}
			case INTEGER_INTERVAL: {
				Document universe = (Document)(new Document("universe", object)).get("universe");
				return new CIntervalInteger(universe.getLong("min"), universe.getLong("max"));
			}

			case FLOAT_INTERVAL: {
				Document universe = (Document)(new Document("universe", object)).get("universe");
				return new CIntervalDouble(universe.getDouble("min"), universe.getDouble("max"));
			}		
			case STRING_INTERVAL: {
				Document universe = (Document)(new Document("universe", object)).get("universe");
				ArrayList<Document> nodes = (ArrayList<Document>)universe.get("nodes");
				TreeMap<Integer, String> map = new TreeMap<Integer, String>();
				ArrayList<Document> relations = (ArrayList<Document>)universe.get("relations");
				ArrayList<Pair<Integer, Integer>> pair = new ArrayList<Pair<Integer, Integer>>();
				for(Document value : nodes) {
					map.put(value.getInteger("id"), value.getString("name"));
				}
				for(Document value : relations) {
					pair.add(new Pair(value.getInteger("start"), value.getInteger("end")));
				}
				return new CGraphStringList(map, pair);
			}
			case WEIGHTED_STRING_LIST: {
				Document universe = (Document)(new Document("universe", object)).get("universe");
				ArrayList<Document> values = (ArrayList<Document>)universe.get("values");
				TreeMap<Integer, String> map = new TreeMap<Integer, String>();
				for(Document value : values) {
					map.put(value.getInteger("value"), value.getString("name"));
				}
				return new CWeightedStringList(universe.getLong("min"), universe.getLong("max"), map);
			}
			case TREE: {
				Document universe = (Document)(new Document("universe", object)).get("universe");
				CTree tree = getTree(universe.get("tree"));
				
				ArrayList<Document> relations = (ArrayList<Document>)universe.get("relations");
				ArrayList<Pair<Integer, Integer>> pair = new ArrayList<Pair<Integer, Integer>>();
				for(Document value : relations) {
					pair.add(new Pair(value.getInteger("start"), value.getInteger("end")));
				}
				return new CGraphTree(tree, pair);
			}
			default: { // error
				System.err.println("Error: Illegal universe type");
				System.err.println(type);
				System.err.println(object);
				System.exit(0);
			}
		}
		return null;
	}

	/**
	 * Gets the item.
	 *
	 * @param      type      The type of item to create
	 * @param      object    The object containing the data of the item to create
	 * @param      universe  The universe containing the data of the universe
	 *
	 * @return     The item.
	 */
	public CItemType getItem(CEnumItemType type, Object object, CUniverseType universe) {
		switch(type) {
			case INTEGER: {
				Document value = (Document)(new Document("value", object));
				if (universe.getClass() == CIntervalInteger.class) {
					return new CInteger(value.getLong("value"));
				}
				else {
					return new CWeightedInteger(value.getLong("value"));
				}
			}
			case FLOAT: {
				Document value = (Document)(new Document("value", object));
				return new CFloat(value.getDouble("value"));
			}
			case BOOLEAN: {
				Document value = (Document)(new Document("value", object));
				return new CBoolean(value.getBoolean("value"));
			}
			case INTEGER_INTERVAL: {
				Document value = (Document)(new Document("value", object)).get("value");
				return new CIntervalInteger(value.getLong("min"), value.getLong("max"));
			}
			case FLOAT_INTERVAL: {
				Document value = (Document)(new Document("value", object)).get("value");
				return new CIntervalDouble(value.getDouble("min"), value.getDouble("max"));
			}		
			case INTEGER_LIST: {
				ArrayList<Integer> values = (ArrayList<Integer>)object;
				TreeMap<Integer, String> map = new TreeMap<Integer, String>();
				
				CGraphStringList listMod = (CGraphStringList)universe;
				TreeMap<Integer, String> critMap = listMod.getMap();
				
				for(Integer value : values) {
					map.put(value, critMap.get(value));
				}
				return new CMappedStringList(map);
			}
			case INTEGER_TREE: {
				CGraphTree treeMod = (CGraphTree)universe;
				CTree tree = getTree(object, treeMod.getTree());
				return tree;
			}	
			default: { 
				// error
				System.err.println("Error: Illegal item type");
				System.err.println(type);
				System.err.println(object);
				System.exit(0);
			}
		}
		return null;
	}

	/**
	 * Gets the tree.
	 *
	 * @param      o     The object containg the raw data
	 *
	 * @return     The tree.
	 */
	private CTree getTree(Object o) {
		Document universe = (Document)(new Document("universe", o)).get("universe");
		CTree root = new CTree(universe.getInteger("id"), universe.getString("value"));
		ArrayList<Object> children = (ArrayList<Object>)universe.get("children");
		if (children != null) {
			for (Object child : children) {
				root.addChild(getTree(child));
			}
		}
		return root;
	}

	/**
	 * Gets the subtree.
	 *
	 * @param      o     The object containg the raw data of the subtree
	 * @param      tree  The root tree containg the data of the tree
	 *
	 * @return     The subtree.
	 */
	private CTree getTree(Object o, CTree tree) {
		Document value = (Document)(new Document("value", o)).get("value");
		
		Integer id = value.getInteger("value");
		String name = tree.getData();	
		
		CTree root = new CTree(id, name);
		ArrayList<Object> children = (ArrayList<Object>)value.get("children");
		if (children != null) {
			ArrayList<CTree> childrenMod = tree.getChildren();
			for (Object child : children) {
				
				// find which childMod match the current child
				Integer idToFind = ((Document)(new Document("value", child)).get("value")).getInteger("value");
				
				for (CTree c : childrenMod) {

					if (c.getID().equals(idToFind)) {
						root.addChild(getTree(child, c));
						break;
					}
				}
				
			}
		}
		return root;
	}
}