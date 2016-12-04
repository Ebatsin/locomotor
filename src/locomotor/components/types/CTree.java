package locomotor.components.types;

import java.util.List;
import java.util.ArrayList;

/**
 * @todo describe the class
 */
public class CTree implements CUniverseType, CVehicleType, CUserType {
	
    /**
     * The identifier
     */
    private Long _identifier;
	/**
	 * The string
	 */
    private String _data;
    /**
     * The list of children
     */
    private List<CTree> _children;

    /**
     * Constructs the CTree
     *
     * @param      data  The string
     */
    public CTree(Long id, String data) {
    	_children = new ArrayList<CTree>();
        _identifier = id;
        _data = data;
    }

    /**
     * Gets the ID.
     *
     * @return     The identifier.
     */
    public Long getID() {
        return _identifier;
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
    public List<CTree> getChildren() {
        return _children;
    }

    /**
     * Adds a child.
     *
     * @param      identifier  The identifier
     * @param      data        The data
     */
    public void addChild(Long identifier, String data) {
        CTree child = new CTree(identifier, data);
        _children.add(child);
    }

    /**
     * Adds a child.
     *
     * @param      child  The child
     */
    public void addChild(CTree child) {
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
    	for (CTree child : _children) {
    		if(child.isLeaf()) {
    			count++;
    		} else {
    			count += child.leafCount();
    		}
    	}
    	return count;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return     String representation of the object.
     */
    public String toString() {
        String s = "";
        s += "(" + _identifier + "," + _data;
        if(!isLeaf()) {
            s += "\n";
            for (CTree child : _children) {
                s += child.toString();
            }
            s += "\n";
        }
        s += ")";
        return s;
    }
}