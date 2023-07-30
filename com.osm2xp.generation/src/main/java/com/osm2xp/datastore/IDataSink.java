package com.osm2xp.datastore;

import java.util.List;

import com.osm2xp.core.exceptions.DataSinkException;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Way;

import math.geom2d.Box2D;

/**
 * Data sink interface. Store open street map nodes.
 * 
 * @author Benjamin Blanchet
 * 
 */
public interface IDataSink {
	
	/**
	 * Process input bounding box
	 * @param boundingBox Input bounding box
	 */
	void processBoundingBox(Box2D boundingBox);

	/**
	 * Store a node in the storage implementation.
	 * 
	 * @param node
	 * @throws DataSinkException
	 */
	void storeNode(Node node) throws DataSinkException;

	/**
	 * find a node in the storage implementation.
	 * 
	 * @param nodeId
	 * @return a osm node
	 * @throws DataSinkException
	 */
	Node getNode(Long id) throws DataSinkException;

	/**
	 * find a list of nodes in the storage implementation.
	 * 
	 * @param nodeIds
	 * @return a list of osm nodes
	 */
	List<Node> getNodes(List<Long> nodeIds) throws DataSinkException;

	/**
	 * called on completion of generation job.
	 */
	public void complete() throws DataSinkException;
	
	/**
	 * @return Whether this data sink is "readonly" and nothing more should be appended to it. Can be used e.g. to avoid double element addition if we have more than one pass
	 */
	public boolean isReadOnly();
	
	/**
	 * @param readOnly <code>true</code> if this store does not support elemnet addition anymore (e.g. after first input file pass was finished), <code>false</code> otherwise
	 */
	public void setReadOnly(boolean readOnly); 

	/**
	 * return the number of stored nodes
	 * 
	 * @return
	 */
	public long getNodesNumber();


	/**
	 * Store a way in the storage implementation.
	 * @param wayId TODO
	 * @param pointIds way to store
	 */
	void storeWayPoints(long wayId, long[] pointIds);
	
	/**
	 * Find a way in the storage implementation.
	 * 
	 * @param wayId
	 * @return corresponding {@link Way} or <code>null</code>
	 */
	long[] getWayPoints(long wayId);
	
}
