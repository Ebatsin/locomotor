package locomotor.components.types;

import java.util.List;
import java.util.ArrayList;

/**
 * @todo describe the class
 */
public class CTree<Long,String> implements CUniverseType, CVehicleType, CUserType {
	
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
    private List<CTree<Long,String>> _children;

    /**
     * Constructs the CTree
     *
     * @param      data  The string
     */
    public CTree(Long id, String data) {
    	_children = new ArrayList<CTree<Long,String>>();
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
    public List<CTree<Long,String>> getChildren() {
        return _children;
    }

    /**
     * Adds a child.
     *
     * @param      identifier  The identifier
     * @param      data        The data
     */
    public void addChild(Long identifier, String data) {
        CTree<Long,String> child = new CTree<Long,String>(identifier, data);
        _children.add(child);
    }

    /**
     * Adds a child.
     *
     * @param      child  The child
     */
    public void addChild(CTree<Long,String> child) {
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
    	for (CTree<Long,String> child : _children) {
    		if(child.isLeaf()) {
    			count++;
    		} else {
    			count += child.leafCount();
    		}
    	}
    	return count;
    }
}