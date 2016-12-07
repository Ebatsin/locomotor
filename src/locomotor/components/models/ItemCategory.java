package locomotor.components.models;

import java.util.ArrayList;

public class ItemCategory extends Category {

	public ItemCategory(String id, ArrayList<ItemCriteria> criterias) {
		super(id, criterias);
	}
	
	public String toString() {
		return "Category item: " + super.toString();
	}
}