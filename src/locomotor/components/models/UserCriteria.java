package locomotor.components.models;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import locomotor.components.types.CUserType;
import locomotor.components.types.TypeFactory;

/**
 * An user criteria, identified by his criteria model and containing a value.
 * It can be compare with another criteria based on the same model criteria.
 */
public class UserCriteria extends Criteria {

	/**
	 * Disable flexibility for comparison.
	 */
	private boolean _disableFlex;

	/**
	 * Constructs the object.
	 *
	 * @param      id           The user criteria identifier
	 * @param      value        The value
	 * @param      disableFlex  The disable flex
	 */
	public UserCriteria(String id, CUserType value, boolean disableFlex) {
		super(id, value);
		_disableFlex = disableFlex;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return     String representation of the object.
	 */
	public String toString() {
		return "Criteria user:\n" + "Flexibility: " + (!_disableFlex) + "\n" + super.toString();
	}

	/**
	 * Gets the disable flexibility.
	 *
	 * @return     The disable flexibility.
	 */
	public boolean getDisableFlexibility() {
		return _disableFlex;
	}

	/**
	 * Gets the value.
	 *
	 * @return     The value.
	 */
	public CUserType getValue() {
		return (CUserType)super.getValue();
	}

	/**
	 * Create the perfect item's criteria (user perspective).
	 *
	 * @param      json       The json
	 * @param      critModel  The criteria model
	 *
	 * @return     The perfect item's criteria.
	 */
	public static UserCriteria fromJSON(JsonValue json, CriteriaModel critModel) {
		JsonObject criteria = json.asObject();
		String identifier = criteria.get("criterionId").asString();
		boolean disableFlex = criteria.get("disableFlex").asBoolean();
		JsonObject value = criteria.get("value").asObject();

		// delegate instanciation to the factory
		TypeFactory typeFactory = new TypeFactory();
		CUserType userValue = typeFactory.getUser(critModel.getUserType(), value);

		return new UserCriteria(identifier, userValue, disableFlex);
	}

}