package locomotor.components.models;

import java.util.ArrayList;

public class VehicleCategory extends Category {

	public VehicleCategory(String id, ArrayList<VehicleCriteria> criterias) {
		super(id, criterias);
	}
	
	public String toString() {
		return "Category vehicle: " + super.toString();
	}
}