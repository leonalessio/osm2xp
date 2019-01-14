package com.osm2xp.classification.parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.osm2xp.classification.WayBuildingData;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Relation;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.core.model.osm.Way;

import math.geom2d.Box2D;

public class LearningWaysCollector extends LearningDataCollector {
	
	private Set<Long> necessaryWays;
	private List<WayBuildingData> collectedWayData = new ArrayList<WayBuildingData>();
	private Map<Long, List<Long>> wayNds = new HashMap<Long, List<Long>>();

	public LearningWaysCollector(Predicate<List<Tag>> samplePredicate, Set<Long> necessaryWays) {
		super(samplePredicate);
		this.necessaryWays = necessaryWays;
	}

	@Override
	public void visit(Box2D box) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Node node) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Way way) {
		if (necessaryWays.contains(way.getId())) {
			wayNds.put(way.getId(), way.getNd().stream().map(nd -> nd.getRef()).collect(Collectors.toList()));
		} else if (isGoodSample(way.getTags())) {
			WayBuildingData data = new WayBuildingData(way.getNd().stream().map(nd -> nd.getRef()).collect(Collectors.toList()));
			initDataFromTags(data, way.getTags());
			collectedWayData.add(data);
		}
	}

	@Override
	public void visit(Relation relation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void complete() {
		// TODO Auto-generated method stub

	}

	public List<WayBuildingData> getCollectedWayData() {
		return collectedWayData;
	}

	public Map<Long, List<Long>> getWayNds() {
		return wayNds;
	}

}
