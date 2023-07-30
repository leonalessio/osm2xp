package com.osm2xp.classification.model;

import java.util.List;

import com.osm2xp.core.model.osm.Tag;

public class WayEntity extends OsmEntity {
	
	protected final List<Long> nodes;
	protected boolean hasHoles = false;

	public WayEntity(long id, List<Tag> tags, List<Long> nodes) {
		super(id, tags);
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
