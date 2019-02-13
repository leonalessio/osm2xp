package com.osm2xp.datastore.impl;

import com.osm2xp.core.exceptions.DataSinkException;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.index.IIdIndex;
import com.osm2xp.index.IdToObjectIndex;
import com.osm2xp.index.IntIndexStorage;
import com.osm2xp.index.PointCoordsIndex;

/**
 * Memory-critical data sink implementation for processing large files. 
 * Using sorted arrays and binary search methods instead of Hashmaps for storing nodes and way point ids
 * 
 * @author 32kda
 * 
 */
public class MemoryCriticalProcessorImpl extends AbstractDataProcessor {

	private IntIndexStorage<double[]> pointStorage = new IntIndexStorage<double[]>() {

		@Override
		protected IIdIndex<double[]> createIndex() {
			return new PointCoordsIndex();
		}
	};
	private IntIndexStorage<Object> wayStorage = new IntIndexStorage<Object>() {

		@Override
		protected IIdIndex<Object> createIndex() {
			return new IdToObjectIndex();
		}
	};
	private IDListFactory idListFactory = new IDListFactory();

	@Override
	public void storeNode(final Node node) throws DataSinkException {
		long id = node.getId();
		pointStorage.add(id, new double[] {node.getLon(), node.getLat()});
	}

	@Override
	public Node getNode(final Long id) throws DataSinkException {
		double[] coords = pointStorage.get(id);
		if (coords != null) {
			Node node = new Node();
			node.setId(id);
			node.setLat(coords[1]);
			node.setLon(coords[0]);
			return node;
		}
		return null;
	}

	@Override
	public void complete() {
		pointStorage = null;
		wayStorage = null;
	}

	@Override
	public Long getNodesNumber() {
		return (long) pointStorage.size();
	}

	@Override
	public void storeWayPoints(long wayId, long[] pointIds) {
		wayStorage.add(wayId, idListFactory.getStore(pointIds));
	}

	@Override
	public long[] getWayPoints(long wayId) {
		return idListFactory.getIdsList(wayStorage.get(wayId));
	}

}
