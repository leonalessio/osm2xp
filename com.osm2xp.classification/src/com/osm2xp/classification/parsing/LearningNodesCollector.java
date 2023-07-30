package com.osm2xp.classification.parsing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Relation;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.core.model.osm.Way;

import math.geom2d.Box2D;

public class LearningNodesCollector extends LearningDataCollector {
	
	private Set<Long> necessaryNodes;
	private Map<Long, Node> collectedNodes = new HashMap<>();

	public LearningNodesCollector(Predicate<List<Tag>> samplePredicate, Set<Long> necessaryNodes) {
		super(samplePredicate);
		this.necessaryNodes = necessaryNodes;
	}

	@Override
	public void visit(Box2D box) {
		// Do nothing
	}

	@Override
	public void visit(Node node) {
		if (necessaryNodes.contains(node.getId())) {
			collectedNodes.put(node.getId(), node);
		}
	}

	@Override
	public void visit(Way way) {
		// Do nothing
	}

	@Override
	public void visit(Relation relation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void complete() {
		// TODO Auto-generated method stub

	}

	public Map<Long, Node> getCollectedNodes() {
		return collectedNodes;
	}

}
