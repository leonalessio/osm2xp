package com.osm2xp.classification.parsing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.osm2xp.classification.model.WayEntity;
import com.osm2xp.core.model.osm.Nd;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Relation;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.core.model.osm.Way;

import math.geom2d.Box2D;

public class LearningWaysCollector extends LearningDataCollector {
	
	private Set<Long> necessaryWays;
	private List<WayEntity> collectedWayData = new ArrayList<WayEntity>();
	private Map<Long, List<Long>> wayNds = new HashMap<Long, List<Long>>();
	private boolean needsClosedWays;

	public LearningWaysCollector(Predicate<List<Tag>> samplePredicate, Set<Long> necessaryWays, boolean needsClosedWays) {
		super(samplePredicate);
		this.necessaryWays = necessaryWays;
		this.needsClosedWays = needsClosedWays;
	}

	@Override
	public void visit(Box2D box) {
		// Do nothing - we count for ways only here
	}

	@Override
	public void visit(Node node) {
		// Do nothing - we count for ways only here
	}

	@Override
	public void visit(Way way) {
		if (necessaryWays.contains(way.getId())) {
			wayNds.put(way.getId(), way.getNd().stream().map(nd -> nd.getRef()).collect(Collectors.toList()));
		} else if (isGoodSample(way.getTags())) {
			if (!isClosed(way) && needsClosedWays) {
				System.out.println("Invalid way " + way.getId() + ", not closed");
				return;
			}
			WayEntity data = new WayEntity(way.getId(), way.getTags(), way.getNd().stream().map(nd -> nd.getRef()).collect(Collectors.toList()));
			collectedWayData.add(data);
		}
	}

	private boolean isClosed(Way way) {
		List<Nd> nds = way.getNd();
		return nds.size() > 3 && nds.get(0).getRef() == nds.get(nds.size() - 1).getRef();
	}

	@Override
	public void visit(Relation relation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void complete() {
		// TODO Auto-generated method stub

	}

	public List<WayEntity> getCollectedWayData() {
		return collectedWayData;
	}

	public Map<Long, List<Long>> getWayNds() {
		return wayNds;
	}

}
