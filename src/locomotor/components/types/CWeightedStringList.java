package locomotor.components.types;

import java.util.TreeMap;

/**
 * @todo
 */
public class CWeightedStringList extends CStringList {

	private Long _min;
	private Long _max;

	public CWeightedStringList(Long min, Long max, TreeMap<Integer, String> values) {
		super(values);
		_min = min;
		_max = max;
	}
	
}