package locomotor.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import locomotor.components.Pair;

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
	 * The minimum grade of the item to be valuable.
	 */
	private final double _minimumGrade = 0.10;
	
	/**
	 * The maximum number of items wanted.
	 */
	private final int maxItemsWished = 10;

	/**
	 * The maximum number of criterias wanted.
	 */
	private final int maxCriteriasWished = 5;

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

		// grade -> item map
		SortedMap<Double, ArrayList<Pair<String, Double>>> gradesList = new TreeMap<Double, ArrayList<Pair<String, Double>>>(Collections.reverseOrder());
		
		// grade -> criteria map of an item, so for each item
		TreeMap<String, SortedMap<Double, ArrayList<Pair<String, Double>>>> criteriasGradeItem = new TreeMap<String, SortedMap<Double, ArrayList<Pair<String, Double>>>>();
		
		for (Item item : items) {
			
			// store the grade of criterias to sort it easier later
			SortedMap<Double, ArrayList<Pair<String, Double>>> criteriasGrade = new TreeMap<Double, ArrayList<Pair<String, Double>>>(Collections.reverseOrder());

			// compute, get the grade of the item
			double grade = computeGradeOfItem(item, criteriasGrade);
			
			// "error", flexibility is disable for a criteria
			// and this criteria does not match perfectly
			// so item is excluded
			// here keep going the process, cause no "error"
			if (grade != -1.0) {

				// if no item with that grade, then create the list
				ArrayList<Pair<String, Double>> itemList = gradesList.get(grade);
				if (itemList == null) {
					itemList = new ArrayList<Pair<String, Double>>();
					gradesList.put(grade, itemList);
				}
				// add the grade to the arraylist				
				itemList.add(new Pair(item.getID(), grade));

				// add the item's criteria grade list to the map
				criteriasGradeItem.put(item.getID(), criteriasGrade);
			}
		}

		// filter part
		filterResults(gradesList, criteriasGradeItem);

	}

	public void filterResults(SortedMap<Double, ArrayList<Pair<String, Double>>> gradesList, TreeMap<String, SortedMap<Double, ArrayList<Pair<String, Double>>>> criteriasGradeItem) {
		
		// keep the best items
		gradesList = gradesList.headMap(_minimumGrade);
		
		List<Pair<String, Double>> bestItems = new ArrayList<Pair<String, Double>>();
		for(Map.Entry<Double, ArrayList<Pair<String, Double>>> listItemPerGrade : gradesList.entrySet()) {
			bestItems.addAll(listItemPerGrade.getValue());
		}

		// keep the X first best
		if (bestItems.size() > maxItemsWished) {
			bestItems = bestItems.subList(0, maxItemsWished);
		}

		// retrieve the best criterias of each item
		for (Pair<String, Double> item : bestItems) {

			// the list of best criterias
			List<Pair<String, Double>> bestCriterias = new ArrayList<Pair<String, Double>>();
			for(Map.Entry<Double, ArrayList<Pair<String, Double>>> listCriteriaPerGrade : criteriasGradeItem.get(item.getLeft()).entrySet()) {
				bestCriterias.addAll(listCriteriaPerGrade.getValue());
			}

			// keep the X first best criterias
			if (bestCriterias.size() > maxCriteriasWished) {
				bestCriterias = bestCriterias.subList(0, maxCriteriasWished);
			}

			System.out.println(item.getLeft() + " => " + item.getRight());

			for (Pair<String, Double> crit : bestCriterias) {
				String name = _modelCrits.get(crit.getLeft()).getName();
				System.out.println("\t" + name + " => " + crit.getRight());
			}

		}
	}

	/**
	 * Calculates the grade of the item.
	 *
	 * @param      item			  	The item
	 * @param      criteriasGrade  	The map to store the grade computed
	 *
	 * @return     The grade of the item, between -1 (flexibily disable) else between 0 and 1 (best).
	 */
	private double computeGradeOfItem(Item item, SortedMap<Double, ArrayList<Pair<String, Double>>> criteriasGrade) {
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
		return (numberOfCategories == 0) ? grade : (grade / numberOfCategories);
	}

	/**
	 * Calculates the grade of the category.
	 *
	 * @param      userCategory  	The user category
	 * @param      itemCategory  	The item category
	 * @param      criteriasGrade  	The map to store the grade computed
	 *
	 * @return     The grade of the category, between -1 (flexibily disable) else between 0 and 1 (best).
	 */
	private double computeGradeOfCategory(UserCategory userCategory, ItemCategory itemCategory, SortedMap<Double, ArrayList<Pair<String, Double>>> criteriasGrade) {
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

			ArrayList<Pair<String, Double>> critList = criteriasGrade.get(currentGrade);
				
			if (critList == null) {
				critList = new ArrayList<Pair<String, Double>>();
				criteriasGrade.put(currentGrade, critList);
			}
			critList.add(new Pair(uc.getID(), currentGrade));

			grade += currentGrade;
			numberOfCriterias++;
		}
		return (numberOfCriterias == 0) ? grade : (grade / numberOfCriterias);
	}

	/**
	 * Calculates the grade of criteria.
	 *
	 * @param      userCriteria  The user criteria
	 * @param      itemCriteria  The item criteria
	 *
	 * @return     The grade of the criteria, between -1 (flexibily disable) else between 0 and 1 (best).
	 */
	private double computeGradeOfCriteria(UserCriteria userCriteria, ItemCriteria itemCriteria) {
		CUniverseType universe = _modelCrits.get(userCriteria.getID()).getUniverse();
		CComparable item = (CComparable)itemCriteria.getValue();
		CUserType user = userCriteria.getValue();
		boolean disableFlexibility = userCriteria.getDisableFlexibility();

		return item.compare(user, universe, disableFlexibility);
	}

}