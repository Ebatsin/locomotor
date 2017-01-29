package locomotor.components.types;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import java.lang.Math;

import locomotor.components.Compare;

/**
 * A interval between two Double.
 * @see CEnumUniverseType.
 * @see CEnumItemType.
 * @see CEnumUserType.
 */
public class CIntervalDouble extends CInterval implements CComparable<CIntervalDouble, CIntervalDouble> {

	/**
	 * Initializes a closed interval [min, max].
	 *
	 * @param  min the smaller endpoint (64-bit)
	 * @param  max the larger endpoint (64-bit)
	 */
	public CIntervalDouble(Double min, Double max) {
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
	public double compare(CIntervalDouble user, CIntervalDouble universe, boolean disableFlexibility) {
		return Compare.intervalValue(
			user.min().doubleValue(), 
			user.max().doubleValue(), 
			min().doubleValue(), 
			max().doubleValue(), 
			disableFlexibility
		);
	}

	/**
	 * Factory from representation JSON.
	 *
	 * @param      json  The json
	 *
	 * @return     A new CIntervalDouble object.
	 */
	public static CIntervalDouble fromJSON(JsonValue json) {
		JsonObject interval = json.asObject();
		double min = interval.get("min").asDouble();
		double max = interval.get("max").asDouble();
		return new CIntervalDouble(min, max);
	}
}
