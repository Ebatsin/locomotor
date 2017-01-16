package locomotor.components.types;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import java.util.ArrayList;

import locomotor.components.Pair;

/**
 * Represents a tree (@see CTree) with a graph of relations between the leaves of that tree.
 */
public class CGraphTree extends CSetGraph implements CUniverseType {

	/**
	 * The tree representing the universe (all possible data).
	 */
	private CTree _universeTree;

	/**
	 * Constructs the object.
	 *
	 * @param      universeTree  The universe tree.
	 * @param      relations     The relations.
	 */
	public CGraphTree(CTree universeTree, ArrayList<Pair<Integer, Integer>> relations) {
		super(universeTree.toSet(), relations);
		_universeTree = universeTree;
	}

	/**
	 * Gets the tree.
	 *
	 * @return     The tree.
	 */
	public CTree getTree() {
		return _universeTree;
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return     String representation of the object.
	 */
	public String toString() {
		String str = "Tree: " + _universeTree.toString() + "\n";
		str += super.toString();
		return str;
	}

	public JsonObject toJSON() {
		// @todo
		return Json.object();
	}
}