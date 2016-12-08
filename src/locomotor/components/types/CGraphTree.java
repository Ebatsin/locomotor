package locomotor.components.types;

import java.util.ArrayList;

import locomotor.components.Pair;

/**
 * @todo
 */
public class CGraphTree implements CUniverseType {

	/**
	 * The tree representing the universe (all possible data)
	 */
	private CTree _universeTree;

	/**
	 * The list of relations between two leave.
	 */
	private ArrayList<Pair<Integer, Integer>> _relations;

	/**
	 * Constructs the object.
	 *
	 * @param      universeTree  The universe tree
	 * @param      relations     The relations
	 */
	public CGraphTree(CTree universeTree, ArrayList<Pair<Integer, Integer>> relations) {
		_universeTree = universeTree;
		_relations = relations;
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
		str += "Relations:\n";
		for(Pair<Integer, Integer> value : _relations) {
		
			str += value.toString() + "\n";
		}
		return str;
	}
}