package com.osm2xp.classification;

import java.util.List;

import com.osm2xp.classification.annotations.Ignore;

public class WayBuildingData extends BuildingData {

	@Ignore
	private List<Long> nodes;
	
	private boolean hasHoles;

	public WayBuildingData(List<Long> nodes) {
		this.nodes = nodes;
	}

	public List<Long> getNodes() {
		return nodes;
	}

	public boolean isHasHoles() {
		return hasHoles;
	}

	public void setHasHoles(boolean hasHoles) {
		this.hasHoles = hasHoles;
	}

}
