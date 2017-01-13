package locomotor.components.types;

import java.util.ArrayList;
import java.util.Set;

import locomotor.components.Compare;
import locomotor.components.Pair;

import org.jgrapht.alg.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

/**
 * Encapusulate an undirected graph and provide a Floyd-Warshall shortest paths algorithm on that graph.
 */
public class CSetGraph {
	
	/**
	 * The graph of relations.
	 */
	private SimpleGraph<Integer, DefaultEdge> _graph;

	/**
	 * Floyd-Warshall shortest paths.
	 */
	private FloydWarshallShortestPaths<Integer, DefaultEdge> _fwsp;

	/**
	 * Constructs the object.
	 *
	 * @param      vertices     The vertices
	 * @param      edges	  	The relations
	 */
	public CSetGraph(Set<Integer> vertices, ArrayList<Pair<Integer, Integer>> edges) {

		// create vertices
		_graph = new SimpleGraph(DefaultEdge.class);
		for(Integer key : vertices) {
			_graph.addVertex(key);
		}

		// create edges
		for(Pair<Integer, Integer> value : edges) {
			_graph.addEdge(value.getLeft(), value.getRight());
		}

		// shortest paths, no computations are performed yet
		_fwsp = new FloydWarshallShortestPaths(_graph);
	}

	/**
	 * Returns a string representation of the object.
	 *
	 * @return     String representation of the object.
	 */
	public String toString() {
		return "Relations:\n" + _graph.toString();
	}

	/**
	 * Gets the diameter.
	 *
	 * @return     The diameter.
	 */
	public double getDiameter() {
		return _fwsp.getDiameter();
	}

	/**
	 * Get the distance, the length of a shortest path.
	 *
	 * @param      start  The start
	 * @param      end    The end
	 *
	 * @return     the shortest distance between start and end
	 */
	public double distance(Integer start, Integer end) {
		return _fwsp.shortestDistance(start, end);
	}

	/**
	 * Compare the string list of the vehicle with the string list of the user
	 *
	 * @param      userKey  The user key set
	 * @param      itemKey  The item key set
	 *
	 * @return     1.0 (best match), tend toward 0.0 otherwise
	 */
	protected double compare(Set<Integer> userKey, Set<Integer> itemKey) {
		return Compare.graphValue(userKey, itemKey, this);
	}
	
}