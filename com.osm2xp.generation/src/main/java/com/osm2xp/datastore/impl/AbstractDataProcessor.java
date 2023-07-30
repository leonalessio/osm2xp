package com.osm2xp.datastore.impl;

import java.util.ArrayList;
import java.util.List;

import com.osm2xp.core.exceptions.DataSinkException;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.datastore.IDataSink;

import math.geom2d.Box2D;

public abstract class AbstractDataProcessor implements IDataSink {

	private boolean readOnly = false;

	@Override
	public List<Node> getNodes(final List<Long> ids) throws DataSinkException {
		final List<Node> nodes = new ArrayList<Node>();
		for (Long nd : ids) {
			final Node node = getNode(nd);
			if (node != null) {
				nodes.add(node);
			} 
		}
		return nodes.size() > 0 ? nodes : null;
	}
	
	@Override
	public void processBoundingBox(Box2D boundingBox) {
		// Data Sink doesn't care by default; Override if necessary		
	}
	
	@Override
	public boolean isReadOnly() {
		return readOnly;
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
}
