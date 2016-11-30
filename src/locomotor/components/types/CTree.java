package locomotor.components.types;

import java.util.List;
import java.util.ArrayList;

/**
 * @todo describe the class
 */
public class CTree<String> implements CUniverseType, CVehicleType, CUserType {
	
	/**
	 * The string
	 */
    private String _data;
    /**
     * The list of children
     */
    private List<CTree<String>> _children;

    /**
     * Constructs the CTree
     *
     * @param      data  The string
     */
    public CTree(String data) {
    	_children = new ArrayList<CTree<String>>();
        _data = data;
    }

    /**
     * Gets the data.
     *
     * @return     The data.
     */
    public String getData() {
        return _data;
    }

    /**
     * Sets the data.
     *
     * @param      data  The data
     */
    public void setData(String data) {
        _data = data;
    }

    /**
     * Get the children.
     * 
     * @return     The children.
     */
    public List<CTree<String>> getChildren() {
        return _children;
    }

    /**
     * Adds a child.
     *
     * @param      data  The string hold by the child
     */
    public void addChild(String data) {
        CTree<String> child = new CTree<String>(data);
        _children.add(child);
    }

    /**
     * Adds a child.
     *
     * @param      child  The child
     */
    public void addChild(CTree<String> child) {
        _children.add(child);
    }

    /**
     * Determines if leaf.
     *
     * @return     True if leaf, False otherwise.
     */
    public boolean isLeaf() {
        return (_children.size() == 0);
    }

    /**
     * Count the number of leaf
     *
     * @return     The number of leaf in the CTree
     */
    public int leafCount() {
    	int count = 0;
    	for (CTree<String> child : _children) {
    		if(child.isLeaf()) {
    			count++;
    		} else {
    			count += child.leafCount();
    		}
    	}
    	return count;
    }
}