package locomotor.components.models;

import java.util.ArrayList;

public class Item {

	private String _identifier;

	private ArrayList<ItemCategory> _categories;

	public Item(String id, ArrayList<ItemCategory> categories) {
		_identifier = id;
		_categories = categories;
	}

}