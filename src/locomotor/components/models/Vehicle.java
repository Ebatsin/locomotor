package locomotor.components.models;

import java.util.ArrayList;

public class Vehicle {

	private String _identifier;

	private ArrayList<Category> _categories;

	public Vehicle(String id, ArrayList<Category> categories) {
		_identifier = id;
		_categories = categories;
	}

}