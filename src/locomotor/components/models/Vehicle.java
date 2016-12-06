package locomotor.components.models;

import java.util.ArrayList;

public class Vehicle {

	private String _identifier;

	private ArrayList<VehicleCategory> _categories;

	public Vehicle(String id, ArrayList<VehicleCategory> categories) {
		_identifier = id;
		_categories = categories;
	}

}