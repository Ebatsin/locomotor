package locomotor.components.types;

import java.util.ArrayList;
import java.util.TreeMap;
import org.bson.Document;

public class TypeFactory {

	public CUniverseType getUniverse(CEnumUniverseType type, Object o) {
		switch(type) {
			case BOOLEAN: {
					Boolean testvar = null;
					return CBoolean(testvar);
			} break;
			case INTEGER_INTERVAL: {
					Document universe = new Document("universe", o);
					return CIntervalInteger(universe.getLong("min"), universe.getLong("max"));
			} break;
			case FLOAT_INTERVAL: {
					Document universe = new Document("universe", o);
					return CIntervalFloat(universe.getDouble("min"), universe.getDouble("max"));
			} break;			
			case STRING_INTERVAL: {
				ArrayList<Document> universe = (ArrayList<Document>)new Document("universe", o);
				TreeMap<Long, String> map = new TreeMap<Long, String>();
				ArrayList<Document> universeString = (ArrayList<Document>)universe;
				for (Document crit : universe) {
					map.put(ucrit.getLong("id");, crit.getString("name"););
				}
				return CMappedStringList(map);
			} break;
			case WEIGHTED_STRING_LIST: {
				ArrayList<Document> universe = (ArrayList<Document>)new Document("universe", o);
				TreeMap<Long, String> map = new TreeMap<Long, String>();
				ArrayList<Document> universeString = (ArrayList<Document>)universe;
				for (Document crit : universe) {
					map.put(crit.getLong("value"), crit.getString("name"));
				}
				return CWeightedStringList(crit.getLong("min"), crit.getLong("max"), map);
			} break;
			case TREE: {
					// todo
					return CTree("test");
			} break;
			default: {
				// error
		        System.err.println("Error: Illegal universe type");
		        System.err.println(type);
		        System.err.println(o);
		   		System.exit(0);
			}
		}
	}
}