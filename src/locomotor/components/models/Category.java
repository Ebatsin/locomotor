package locomotor.components.models;

import java.util.ArrayList;

public class Category {
	private String _idCatModel;
	private ArrayList<Criteria> _criterias;

	public Category(String id, ArrayList<Criteria> criterias) {
		_idCatModel = id;
		_criterias = criterias;
	}
}