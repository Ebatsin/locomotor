package locomotor.components.types;

import java.util.ArrayList;

import locomotor.components.Pair;

/**
 * @todo .
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
}