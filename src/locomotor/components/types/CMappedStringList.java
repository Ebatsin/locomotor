package locomotor.components.types;

import java.util.TreeMap;

/**
 * Encapsulate a map between an Integer (32-bit) and the String associated.
 */
public class CMappedStringList extends CStringList {

	public CMappedStringList(TreeMap<Integer, String> values) {
		super(values);
	}
	
}