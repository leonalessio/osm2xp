package com.osm2xp.generation.collections;

import java.util.List;
import java.util.ListIterator;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.index.ArrayListVisitor;

/**
 * 
 * Implements the nearest neighbor search logic for a LonLatKdTree. This helper class
 * 
 * is used internally by {@link NearestKdTree}, and is not envisioned to be
 * 
 * generally useful to client code.
 * 
 * 
 * 
 * @author Bryce Nordgren
 * 
 * @since 1.12
 * 
 * @see NearestNotInSearch
 * 
 * @see NearestNonIdenticalSearch
 * 
 * @see NearestKdTree
 *
 * 
 * 
 */

public class NearestSearch extends Search {

	public NearestSearch(LonLatKdTree tree) {

		super(tree.getRoot());

	}

	/**
	 * 
	 * Returns the nearest neighbor as a coordinate only.
	 * 
	 * @param search the point to search for
	 * 
	 * @return point in the tree nearest to the search point.
	 * 
	 */

	public Coordinate nearest(Coordinate search) {
		return nearest(getRoot(),search).getCoordinate();
	}
	
	/**
	 * 
	 * Returns the nearest neighbor as a stored feature.
	 * 
	 * @param search the point to search for
	 * 
	 * @return {@link Object} representing featre of nearest stored node or <code>null</code>.
	 * 
	 */
	
	public Object nearestFeature (Coordinate search) {
		LonLatKdNode nearest = nearest(getRoot(),search);
		return nearest != null ? nearest.getData() : null;
	}
	
	

	/**
	 * 
	 * Implements the nearest neighbor search algorithm (recursive).
	 * 
	 * @param start    The node at which to begin the search
	 * 
	 * @param searchPt the point to search for.
	 * 
	 * @return search point, nearest neighbor, and separation distance
	 * 
	 */

	protected static LonLatKdNode nearest(LonLatKdNode start, Coordinate searchPt) {

		ArrayListVisitor v = new ArrayListVisitor();

		traverse(start, searchPt, v);

		List<?> path = v.getItems();

		ListIterator<?> unwind = path.listIterator(path.size());

		LonLatKdNode best = null;

		double min_dist = Double.NaN;

		double cur_dist;

		while (unwind.hasPrevious()) {

			LonLatKdNode current = ((LonLatKdNode) (unwind.previous()));

			// initialize the "best" pointer if necessary

			if (best == null) {

				best = current;

				min_dist = searchPt.distance(best.getCoordinate());

			} else {

				// check current point to see if its closer than best-so-far

				cur_dist = searchPt.distance(current.getCoordinate());

				if (min_dist > cur_dist) {

					best = current;

					min_dist = cur_dist;

				}

			}

			// check if it's possible that a closer point may be on

			// the other branch of the tree...

			Coordinate projection = current.projectPoint(searchPt);

			if ((searchPt.distance(projection) < min_dist) || Double.isNaN(min_dist)) {

				// determine which is the "other" branch

				LonLatKdNode child = otherChild(current, searchPt);

				if (child != null) {

					// search the "other" children of this node

					LonLatKdNode curNode = nearest(child, searchPt);

					if (curNode != null) {

						// see if the best match on that side is better.

						cur_dist = curNode.getCoordinate().distance(searchPt);

						if ((min_dist > cur_dist) || Double.isNaN(min_dist)) {

							best = curNode;

							min_dist = cur_dist;

						}
					}
				}
			}
		}
		return best;

	}

}
