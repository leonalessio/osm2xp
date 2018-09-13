package com.osm2xp.dataProcessors.impl;

import java.util.HashMap;
import java.util.Map;

import com.carrotsearch.hppc.LongObjectHashMap;
import com.osm2xp.exceptions.DataSinkException;
import com.osm2xp.model.osm.Node;

/**
 * Memory data sink implementation
 * 
 * @author Benjamin Blanchet
 * 
 */
public class MemoryProcessorImpl extends AbstractDataProcessor {

	private Map<Long, double[]> nodeMap = new HashMap<Long, double[]>();
	private LongObjectHashMap<long[]> wayMap = new LongObjectHashMap<long[]>();

	@Override
	public void storeNode(final Node node) throws DataSinkException {
		double[] pt = new double[] { node.getLat(), node.getLon() };
		this.nodeMap.put(node.getId(), pt);
	}

	@Override
	public Node getNode(final Long id) throws DataSinkException {
		double[] loc = nodeMap.get(id);
		if (loc != null) {
			Node node = new Node();
			node.setId(id);
			node.setLat(loc[0]);
			node.setLon(loc[1]);
			return node;
		}
		return null;
	}

	@Override
	public void complete() {
		nodeMap = null;
	}

	@Override
	public Long getNodesNumber() {
		return (long) nodeMap.size();
	}

	@Override
	public void storeWayPoints(long wayId, long[] pointIds) {
		wayMap.put(wayId, pointIds);
	}

	@Override
	public long[] getWayPoints(long wayId) {
		return (long[]) wayMap.get(wayId);
	}

	@Override
	public void clearNodes() {
		nodeMap.clear();
	}

	@Override
	public void clearWays() {
		wayMap.clear();
	}

}
