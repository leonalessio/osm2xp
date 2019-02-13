package com.osm2xp.core.parsers;

import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Relation;
import com.osm2xp.core.model.osm.Way;

import math.geom2d.Box2D;

public interface IOSMDataVisitor {
	public void visit(Box2D box);
	public void visit(Node node);
	public void visit(Way way);
	public void visit(Relation relation);
	public void complete();
}
