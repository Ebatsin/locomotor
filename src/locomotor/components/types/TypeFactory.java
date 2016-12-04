package locomotor.components.types;

import java.util.ArrayList;
import java.util.TreeMap;
import org.bson.Document;

public class TypeFactory {

	public CUniverseType getUniverse(CEnumUniverseType type, Object o) {
		System.out.println("Type = " + type);
		switch(type) {
			case BOOLEAN: {
					Boolean testvar = null;
					return new CBoolean(testvar);
				}
			case INTEGER_INTERVAL: {
					Document universe = new Document("universe", o);
					return new CIntervalInteger(universe.getLong("min"), universe.getLong("max"));
			}
			case FLOAT_INTERVAL: {
					Document universe = new Document("universe", o);
					return new CIntervalDouble(universe.getDouble("min"), universe.getDouble("max"));
			}			
			case STRING_INTERVAL: {
				ArrayList<Document> values = (ArrayList<Document>)o;
				TreeMap<Long, String> map = new TreeMap<Long, String>();
				for (Document value : values) {
					map.put(new Long(value.getInteger("id")), value.getString("name"));
				}
				return new CMappedStringList(map);
			}
			case WEIGHTED_STRING_LIST: {
				Document universe = new Document("universe", o);
				ArrayList<Document> values = (ArrayList<Document>)universe.get("values");
				TreeMap<Long, String> map = new TreeMap<Long, String>();
				for (Document value : values) {
					map.put(new Long(value.getInteger("value")), value.getString("name"));
				}
				return new CWeightedStringList(universe.getLong("min"), universe.getLong("max"), map);
			}
			case TREE: {
					// todo
					return new CTree("test");
			}
			default: {
				// error
		        System.err.println("Error: Illegal universe type");
		        System.err.println(type);
		        System.err.println(o);
		   		System.exit(0);
			}
		}
		return null;
	}
}