package com.osm2xp.datastore.impl;

import java.util.ArrayList;
import java.util.List;

import com.osm2xp.core.exceptions.DataSinkException;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.datastore.IDataSink;

public abstract class AbstractDataProcessor implements IDataSink {

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
	
}
