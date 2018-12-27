package com.osm2xp.translators;

import java.util.List;

import com.osm2xp.model.osm.Tag;
import com.vividsolutions.jts.geom.Geometry;

public interface ISpecificTranslator extends IBasicTranslator {	

	public void processWays(long wayId, List<Tag> tags, Geometry originalGeometry, List<? extends Geometry> fixedGeometries);

}
