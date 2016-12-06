package locomotor.components.types;

import java.util.TreeMap;

/**
 * Encapsulate a map between an Integer (32-bit) and the String associated.
 * The Integer represents the weighted.
 */
public class CWeightedStringList extends CStringList {

	private Long _min;
	private Long _max;

	/**
	 * Builds a Weighted list.
	 * @param min     The lightest weight
	 * @param max     The biggest weight
	 * @param values  The map that contains the weights mapped to their string
	 */
	public CWeightedStringList(Long min, Long max, TreeMap<Integer, String> values) {
		super(values);
		_min = min;
		_max = max;
	}	
}