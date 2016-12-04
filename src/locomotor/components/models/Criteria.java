package locomotor.components.models;

import locomotor.components.types.CVehicleType;

public class Criteria {

	private String _idCritModel;

	private CVehicleType _value;

	public Criteria(String id, CVehicleType value) {
		_idCritModel = id;
		_value = value;
	}

	public String toString() {
		return _value.toString() + "\n";
	}

}