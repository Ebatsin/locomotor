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
	 * Compare the double interval value of the vehicle with the double interval value of the user
	 *
	 * @param      user      The user
	 * @param      universe  The universe
	 *
	 * @return     1.0 (best match), tend toward 0.0 otherwise
	 */
	public double compare(CIntervalDouble user, CIntervalDouble universe) {
		return Compare.intervalValue(user.min().doubleValue(), user.max().doubleValue(), min().doubleValue(), max().doubleValue());
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
