package com.osm2xp.datastore.impl;

import java.util.HashMap;
import java.util.Map;

import com.osm2xp.core.exceptions.DataSinkException;
import com.osm2xp.core.model.osm.Node;

/**
 * Memory data sink implementation <br>
 * 
 * @author Benjamin Blanchet <br>
 * 
 * <b>Deprecated</b> This Memory Processor stucks in case of processing large PBF files (~500 mb), e.g. describing one European country
 * It was replaced with linear array indexes implemented in {@link MemoryCriticalProcessorImpl}, please use it instead.
 * Even on small files {@link MemoryCriticalProcessorImpl} is not slower, or only a bit slower, but these 1-2 secs usually doesn't matter.
 */
@Deprecated
public class MemoryProcessorImpl extends AbstractDataProcessor {

	private Map<Long, double[]> nodeMap = new HashMap<>();
	private Map<Long, long[]> wayMap = new HashMap<>();

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
		wayMap = null;
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

}
