package locomotor.components.models;

import java.util.ArrayList;

public abstract class Category {
	
	private String _idCatModel;
	private ArrayList<? extends Criteria> _criterias;

	public Category(String id, ArrayList<? extends Criteria> criterias) {
		_idCatModel = id;
		_criterias = criterias;
	}
	
	public String toString() {
		return _idCatModel;
	}
}