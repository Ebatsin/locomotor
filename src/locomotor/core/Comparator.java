package locomotor.core;

import locomotor.components.models.CategoryModel;
import locomotor.components.models.Criteria;
import locomotor.components.models.CriteriaModel;
import locomotor.components.models.Item;
import locomotor.components.models.ItemCategory;
import locomotor.components.models.ItemCriteria;
import locomotor.components.models.UserCategory;
import locomotor.components.models.UserCriteria;
import locomotor.components.models.UserItem;

import locomotor.components.types.CComparable;
import locomotor.components.types.CItemType;
import locomotor.components.types.CUniverseType;
import locomotor.components.types.CUserType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @todo.
 */
public class Comparator {

	/**
	 * The model.
	 */
	private ArrayList<CategoryModel> _model;

	/**
	 * The user perfect item.
	 */
	private UserItem _userPerfectItem;

	/**
	 * The criterias mapped, easy retrieving.
	 */
	private HashMap<String, CriteriaModel> _modelCrits;

	/**
	 * Constructs the comparator.
	 *
	 * @param      model     The model
	 * @param      userItem  The user item
	 */
	public Comparator(ArrayList<CategoryModel> model, UserItem userItem) {
		_model = model;
		_userPerfectItem = userItem;

		// map to retrieve easier (perf)
		_modelCrits = new HashMap<String, CriteriaModel>();
		for (CategoryModel cam : model) {
			for (CriteriaModel crm : cam.getCriterias()) {
				_modelCrits.put(crm.getID(), crm);
			}
		}
	}

	// @todo.
	public void computeGradeOfItems(ArrayList<Item> items) {
		TreeMap<String, Double> gradesList = new TreeMap<String, Double>();
		
		TreeMap<String, TreeMap<String, Double>> criteriasGradeItem = new TreeMap<String, TreeMap<String, Double>>();
		
		for (Item item : items) {
			// useful to display best criterias
			TreeMap<String, Double> criteriasGrade = new TreeMap<String, Double>();

			double grade = computeGradeOfItem(item, criteriasGrade);
			
			if (grade != -1.0) {

				gradesList.put(item.getID(), grade);
				criteriasGradeItem.put(item.getID(), criteriasGrade);
			}
		}

		// @todo just display, need to order and filter
		for(Map.Entry<String, Double> item : gradesList.entrySet()) {
			
			String key = item.getKey();
			Double val = item.getValue();

			System.out.println(key + " => " + val);

			TreeMap<String, Double> gradeCrits = criteriasGradeItem.get(key);
			for(Map.Entry<String, Double> crit : gradeCrits.entrySet()) {

				String keyCrits = crit.getKey();
				String name = _modelCrits.get(keyCrits).getName();
				Double valCrits = crit.getValue();

				System.out.println("\t" + name + " => " + valCrits);

			}
		}


	}

	// @todo.
	private double computeGradeOfItem(Item item, TreeMap<String, Double> criteriasGrade) {
		double grade = 0.0;
		int numberOfCategories = 0;

		// map to retrieve easier (perf)
		HashMap<String, ItemCategory> itemCats = new HashMap<String, ItemCategory>();
		for (ItemCategory ic : item.getCategories()) {
			itemCats.put(ic.getID(), ic);
		}

		// calculate the grade for each categories of the user perfect item
		for (UserCategory uc : _userPerfectItem.getCategories()) {
			
			double currentGrade = computeGradeOfCategory(uc, itemCats.get(uc.getID()), criteriasGrade);
			// flexibilty did not match for the criteria
			if (currentGrade == -1.0) {
				return -1.0;
			}

			grade += currentGrade;
			numberOfCategories++;
		}
		return (numberOfCategories == 0) ? grade : grade/numberOfCategories;
	}

	// @todo.
	private double computeGradeOfCategory(UserCategory userCategory, ItemCategory itemCategory, TreeMap<String, Double> criteriasGrade) {
		double grade = 0.0;
		int numberOfCriterias = 0;

		// map to retrieve easier (perf)
		HashMap<String, ItemCriteria> itemCrits = new HashMap<String, ItemCriteria>();
		for (Criteria ic : itemCategory.getCriterias()) {
			itemCrits.put(((ItemCriteria)ic).getID(), (ItemCriteria)ic);
		}

		// calculate the grade for each criterias of the user perfect item's category
		for (Criteria uc : userCategory.getCriterias()) {
			double currentGrade = computeGradeOfCriteria(((UserCriteria)uc), itemCrits.get(uc.getID()));
			
			// flexibilty did not match for the criteria
			if (currentGrade == -1.0) {
				return -1.0;
			}

			criteriasGrade.put(uc.getID(), currentGrade);
			grade += currentGrade;
			numberOfCriterias++;
		}
		return (numberOfCriterias == 0) ? grade : grade/numberOfCriterias;
	}

	/**
	 * Calculates the grade of criteria.
	 *
	 * @param      userCriteria  The user criteria
	 * @param      itemCriteria  The item criteria
	 *
	 * @return     The grade of criteria, between -1 (flexibily disable) else between 0 and 1 (best).
	 */
	private double computeGradeOfCriteria(UserCriteria userCriteria, ItemCriteria itemCriteria) {
		CUniverseType universe = _modelCrits.get(userCriteria.getID()).getUniverse();
		CComparable item = (CComparable)itemCriteria.getValue();
		CUserType user = userCriteria.getValue();
		boolean disableFlexibility = userCriteria.getDisableFlexibility();

		return item.compare(user, universe, disableFlexibility);
	}

}