package com.osm2xp.dataProcessors.impl;

import com.carrotsearch.hppc.LongObjectHashMap;
import com.osm2xp.exceptions.DataSinkException;
import com.osm2xp.index.PointIndex;
import com.osm2xp.model.osm.Node;

/**
 * Memory data sink implementation
 * 
 * @author Benjamin Blanchet
 * 
 */
public class MemoryCriticalProcessorImpl extends AbstractDataProcessor {

	private LongObjectHashMap<Object> wayMap = new LongObjectHashMap<Object>();
	private long prevNode = -1;
	private PointIndex pointIndex = new PointIndex();
	private IDListFactory idListFactory = new IDListFactory();

	@Override
	public void storeNode(final Node node) throws DataSinkException {
		pointIndex.add(node.getId(), node.getLon(), node.getLat());
		if (prevNode > node.getId()) {
			System.out.println("MemoryProcessorImpl.storeNode() " + prevNode + "," + node.getId());
		}
		prevNode = node.getId();
	}

	@Override
	public Node getNode(final Long id) throws DataSinkException {
		double[] coords = pointIndex.getPoint(id);
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
		pointIndex = null;
	}

	@Override
	public Long getNodesNumber() {
		return (long) pointIndex.size();
	}

	@Override
	public void storeWayPoints(long wayId, long[] pointIds) {
		wayMap.put(wayId, idListFactory.getStore(pointIds));
	}

	@Override
	public long[] getWayPoints(long wayId) {
		return idListFactory.getIdsList(wayMap.get(wayId));
	}

	@Override
	public void clearNodes() {
//		latMap.clear();		
//		lonMap.clear();		
	}

	@Override
	public void clearWays() {
		wayMap = new LongObjectHashMap<>();		
	}

}
