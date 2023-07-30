package com.osm2xp.classification;

import java.util.List;

public class RelationBuildingData extends BuildingData {

	private List<Long> outerWayIds;
	private List<Long> innerWayIds;

	public RelationBuildingData(List<Long> outerWayIds, List<Long> innerWayIds) {
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
