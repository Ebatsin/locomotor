package locomotor.components.models;

import locomotor.components.types.CComparableType;

public abstract class Criteria {

	private String _idCritModel;

	private CComparableType _value;

	public Criteria(String id, CComparableType value) {
		_idCritModel = id;
		_value = value;
	}

	public String toString() {
		return _value.toString() + "\n";
	}

}