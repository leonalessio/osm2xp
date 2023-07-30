package com.osm2xp.translators;

import java.util.List;

import com.osm2xp.core.model.osm.Tag;
import org.locationtech.jts.geom.Geometry;

public interface ISpecificTranslator extends IBasicTranslator {	

	public void processWays(long wayId, List<Tag> tags, Geometry originalGeometry, List<? extends Geometry> fixedGeometries);

}
