package com.osm2xp.classification.model;

import java.util.List;

import com.osm2xp.core.model.osm.Tag;

public class RelationEntity extends OsmEntity {
	
	protected final List<Long> outerWayIds;
	protected final List<Long> innerWayIds;

	public RelationEntity(long id, List<Tag> tags, List<Long> outerWayIds, List<Long> innerWayIds) {
		super(id, tags);
		this.outerWayIds = outerWayIds;
		this.innerWayIds = innerWayIds;
	}

	public List<Long> getOuterWayIds() {
		return outerWayIds;
	}

	public List<Long> getInnerWayIds() {
		return innerWayIds;
	}
	

}
