package com.osm2xp.parsers;

import org.openstreetmap.osmosis.osmbinary.Osmformat.HeaderBBox;

import com.osm2xp.model.osm.Node;
import com.osm2xp.model.osm.Relation;
import com.osm2xp.model.osm.Way;

public interface IOSMDataVisitor {
	public void visit(HeaderBBox box);
	public void visit(Node node);
	public void visit(Way way);
	public void visit(Relation relation);
	public void complete();
}
