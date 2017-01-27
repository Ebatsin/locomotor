package locomotor.components.types;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import locomotor.components.Compare;

/**
 * A interval between two Long.
 * @see CEnumUniverseType.
 * @see CEnumItemType.
 * @see CEnumUserType.
 */
public class CIntervalInteger extends CInterval implements CComparable<CIntervalInteger, CIntervalInteger> {

	/**
	 * Initializes a closed interval [min, max].
	 *
	 * @param  min the smaller endpoint (64-bit)
	 * @param  max the larger endpoint (64-bit)
	 */
	public CIntervalInteger(Long min, Long max) {
		super(min, max);
	}

	/**
	 * Compare boolean value
	 *
	 * @param      user                The user criteria
	 * @param      universe            The universe
	 * @param      disableFlexibility  Disable the flexibility
	 *
	 * @return     1.0 (match), 0.0 otherwise or -1.0 if does not match perfectly (flexibility disable)
	 */
	public double compare(CIntervalInteger user, CIntervalInteger universe, boolean disableFlexibility) {
		return Compare.intervalValue(user.min().doubleValue(), user.max().doubleValue(), min().doubleValue(), max().doubleValue(), disableFlexibility);
	}

	/**
	 * Factory from representation JSON.
	 *
	 * @param      json  The json
	 *
	 * @return     A new CIntervalInteger object.
	 */
	public static CIntervalInteger fromJSON(JsonValue json) {
		JsonObject interval = json.asObject();
		long min = interval.get("min").asLong();
		long max = interval.get("max").asLong();
		return new CIntervalInteger(min, max);
	}

}
