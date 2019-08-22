package com.osm2xp.generation.collections;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;

import com.osm2xp.utils.geometry.LatLonCoordinate;

/**
 * 
 * An implementation of a 2-D KD-Tree. KD-trees provide fast range searching on
 * point data.
 * <p>
 * This implementation supports detecting and snapping points which are closer
 * than a given tolerance value. If the same point (up to tolerance) is inserted
 * more than once a new node is not created but the count of the existing node
 * is incremented.
 * 
 * 
 * @author David Skea
 * @author Martin Davis
 * 
 *         Adapted by 32kda to use with x as lontitude, y as latitude
 */
public class LonLatKdTree {
	// typedef
	protected static final Coordinate[] coordType = new Coordinate[0];
	private LonLatKdNode root = null;
	private LonLatKdNode last = null;
	protected long numberOfNodes;
	private double tolerance;

	/**
	 * 
	 * Creates a new instance of a LonLatKdTree
	 * 
	 * with a snapping tolerance of 0.0.
	 * 
	 * (I.e. distinct points will <i>not</i> be snapped)
	 * 
	 */
	public LonLatKdTree() {
		this(0.0);
	}

	/**
	 * 
	 * Creates a new instance of a LonLatKdTree, specifying a snapping distance
	 * tolerance.
	 * 
	 * Points which lie closer than the tolerance to a point already
	 * 
	 * in the tree will be treated as identical to the existing point.
	 * 
	 * 
	 * 
	 * @param tolerance
	 * 
	 *                  the tolerance distance for considering two points equal
	 * 
	 */
	public LonLatKdTree(double tolerance) {
		this.tolerance = tolerance;
	}

	protected LonLatKdTree(LonLatKdNode _root, double tolerance) {
		this.tolerance = tolerance;
		this.root = _root;
	}

	/**
	 * 
	 * Inserts a new point in the kd-tree, with no data.
	 * 
	 * 
	 * 
	 * @param p
	 * 
	 *          the point to insert
	 * 
	 */
	public LonLatKdNode insert(Coordinate p) {
		return insert(new LatLonCoordinate(p), null);
	}

	/**
	 * 
	 * Inserts a new point into the kd-tree.
	 * 
	 * 
	 * 
	 * @param p
	 * 
	 *             the point to insert
	 * 
	 * @param data
	 * 
	 *             a data item for the point
	 * 
	 * @return returns a new LonLatKdNode if a new point is inserted, else an
	 *         existing
	 * 
	 *         node is returned with its counter incremented. This can be checked
	 * 
	 *         by testing returnedNode.getCount() > 1.
	 * 
	 */
	public LonLatKdNode insert(Coordinate p, Object data) {
		p = new LatLonCoordinate(p);
		if (root == null) {
			root = new LonLatKdNode(p, data, 0);
		}
		LonLatKdNode currentNode = root;
		LonLatKdNode leafNode = root;
		int splitOrdinate = 1;
		boolean isOddLevel = true;
		boolean isLessThan = true;
		// traverse the tree first cutting the plane left-right the top-bottom
		while (currentNode != last) {
			if (isOddLevel) {
				isLessThan = p.x < currentNode.getX();
			} else {
				isLessThan = p.y < currentNode.getY();
			}
			leafNode = currentNode;
			if (isLessThan) {
				currentNode = currentNode.getLeft();
			} else {
				currentNode = currentNode.getRight();
			}
			// test if point is already a node
			if (currentNode != null) {
				boolean isInTolerance = p.distance(currentNode.getCoordinate()) < tolerance;
				// if (isInTolerance && ! p.equals2D(currentNode.getCoordinate())) {
				// System.out.println("KDTree: Snapped!");
				// System.out.println(WKTWriter.toPoint(p));
				// }
				// check if point is already in tree (up to tolerance) and if so simply
				// return
				// existing node
				if (isInTolerance) {
					currentNode.increment();
					return currentNode;
				}
			}
			splitOrdinate = (splitOrdinate + 1) % 2;
			isOddLevel = !isOddLevel;
		}
		// no node found, add new leaf node to tree
		numberOfNodes = numberOfNodes + 1;
		LonLatKdNode node = new LonLatKdNode(p, data, splitOrdinate);
		node.setLeft(last);
		node.setRight(last);
		if (isLessThan) {
			leafNode.setLeft(node);
		} else {
			leafNode.setRight(node);
		}
		return node;
	}

	private void queryNode(LonLatKdNode currentNode, LonLatKdNode bottomNode, Envelope queryEnv, boolean odd,
			List<LonLatKdNode> result) {
		if (currentNode == bottomNode)
			return;
		double min;
		double max;
		double discriminant;
		if (odd) {
			min = queryEnv.getMinX();
			max = queryEnv.getMaxX();
			discriminant = currentNode.getX();
		} else {
			min = queryEnv.getMinY();
			max = queryEnv.getMaxY();
			discriminant = currentNode.getY();
		}
		boolean searchLeft = min < discriminant;
		boolean searchRight = discriminant <= max;
		if (searchLeft) {
			queryNode(currentNode.getLeft(), bottomNode, queryEnv, !odd, result);
		}
		if (queryEnv.contains(currentNode.getCoordinate())) {
			result.add(currentNode);
		}
		if (searchRight) {
			queryNode(currentNode.getRight(), bottomNode, queryEnv, !odd, result);
		}
	}

	/**
	 * 
	 * Performs a range search of the points in the index.
	 * 
	 * 
	 * 
	 * @param queryEnv
	 * 
	 *                 the range rectangle to query
	 * 
	 * @return a list of the KdNodes found
	 * 
	 */
	public List<LonLatKdNode> query(Envelope queryEnv) {
		List<LonLatKdNode> result = new ArrayList<>();
		queryNode(root, last, queryEnv, true, result);
		return result;
	}

	/**
	 * 
	 * Performs a range search of the points in the index.
	 * 
	 * 
	 * 
	 * @param queryEnv
	 * 
	 *                 the range rectangle to query
	 * 
	 * @param result
	 * 
	 *                 a list to accumulate the result nodes into
	 * 
	 */
	public void query(Envelope queryEnv, List<LonLatKdNode> result) {
		queryNode(root, last, queryEnv, true, result);
	}

	protected LonLatKdNode getRoot() {
		return root;
	}

	protected LonLatKdNode getLast() {
		return last;
	}

	/**
	 * Returns the number of points stored in the tree.
	 * 
	 * @return size of the tree.
	 * @since 1.12
	 */
	public long size() {
		return numberOfNodes;
	}
}