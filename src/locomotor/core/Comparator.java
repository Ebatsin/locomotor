package locomotor.core;

import locomotor.components.models.CategoryModel;
import locomotor.components.models.Item;
import locomotor.components.models.ItemCategory;
import locomotor.components.models.ItemCriteria;
import locomotor.components.models.UserCategory;
import locomotor.components.models.UserCriteria;
import locomotor.components.models.UserItem;

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

	private HashMap<String, CriteriaModel> modelCrits;

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
		HashMap<String, CriteriaModel> modelCrits = new HashMap<String, CriteriaModel>();
		for (CategoryModel cam : model) {
			for (CriteriaModel crm : cam.getCriterias()) {
				modelCrits.put(crm.getID(), crm);
			}
		}

	}

	// @todo.
	public void computeGradeOfItems(ArrayList<Item> items) {
		TreeMap<String, Double> gradesList = new TreeMap<String, Double>();
		for (Item item : items) {
			double grade = computeGradeOfItem(item);
			gradesList.put(item.getID(), grade);
		}
	}

	// @todo.
	private double computeGradeOfItem(Item item) {
		double grade = 0.0;
		int numberOfCategories = 0;

		// map to retrieve easier (perf)
		HashMap<String, ItemCategory> itemCats = new HashMap<String, ItemCategory>();
		for (ItemCategory ic : item.getCategories()) {
			itemCats.put(ic.getID(), ic);
		}

		// calculate the grade for each categories of the user perfect item
		for (UserCategory uc : _userPerfectItem.getCategories()) {
			grade += computeGradeOfCategory(uc, itemCats.get(uc.getID()));
			numberOfCategories++;
		}
		return (numberOfCategories == 0) ? grade : grade/numberOfCategories;
	}

	// @todo.
	private double computeGradeOfCategory(UserCategory userCategory, ItemCategory itemCategory) {
		double grade = 0.0;
		int numberOfCriterias = 0;

		// map to retrieve easier (perf)
		HashMap<String, ItemCriteria> itemCrits = new HashMap<String, ItemCriteria>();
		for (ItemCriteria ic : itemCategory.getCriterias()) {
			itemCrits.put(ic.getID(), ic);
		}

		// calculate the grade for each criterias of the user perfect item's category
		for (UserCriteria uc : userCategory.getCriterias()) {
			grade += computeGradeOfCriteria(uc, itemCrits.get(uc.getID()));
			numberOfCriterias++;
		}
		return (numberOfCriterias == 0) ? grade : grade/numberOfCriterias;
	}

	// @todo.
	private double computeGradeOfCriteria(UserCriteria userCriteria, ItemCriteria itemCriteria) {
		CriteriaModel universeCriteria = modelCrits.get(userCriteria.getID());
		// @todo: use flexibility
		double grade = itemCriteria.compare(userCriteria, universeCriteria);
		return grade;
	}