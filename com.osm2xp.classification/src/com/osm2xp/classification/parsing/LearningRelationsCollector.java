package com.osm2xp.classification.parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.osm2xp.classification.RelationBuildingData;
import com.osm2xp.core.model.osm.Member;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Relation;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.core.model.osm.Way;

import math.geom2d.Box2D;

public class LearningRelationsCollector extends LearningDataCollector {
	
	List<RelationBuildingData> collectedRelationData = new ArrayList<RelationBuildingData>();

	public LearningRelationsCollector(Predicate<List<Tag>> samplePredicate) {
		super(samplePredicate);
		// Do nothing
	}

	@Override
	public void visit(Box2D box) {
		// Do nothing
	}

	@Override
	public void visit(Node node) {
		// Do nothing
	}

	@Override
	public void visit(Way way) {
		// Do nothing
	}

	@Override
	public void visit(Relation relation) {
		List<Tag> tags = relation.getTags();
		if (isGoodSample(tags)) {
			RelationBuildingData buildingData = new RelationBuildingData(relation.getMember().stream().filter(member -> isOuter(member)).map(member -> member.getId()).collect(Collectors.toList()),
																		relation.getMember().stream().filter(member -> isInner(member)).map(member -> member.getId()).collect(Collectors.toList()));
			initDataFromTags(buildingData, tags);
			collectedRelationData.add(buildingData);
		}
	}

	protected boolean isOuter(Member member) {
		return "outer".equals(member.getRole());
	}
	
	protected boolean isInner(Member member) {
		return "inner".equals(member.getRole());
	}

	@Override
	public void complete() {
		// Do nothing
	}

	public List<RelationBuildingData> getCollectedRelationData() {
		return collectedRelationData;
	}

}
