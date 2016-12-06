package locomotor.components.models;

import java.util.ArrayList;

public class UserCategory extends Category {

	public UserCategory(String id, ArrayList<UserCriteria> criterias) {
		super(id, criterias);
	}
	
	public String toString() {
		return "Category user: " + super.toString();
	}
}