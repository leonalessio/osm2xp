package com.osm2xp.parsers.impl;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.osm2xp.dataProcessors.IDataSink;
import com.osm2xp.exceptions.Osm2xpBusinessException;
import com.osm2xp.model.osm.Tag;
import com.osm2xp.model.osm.Way;
import com.osm2xp.translators.ISpecificTranslator;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

public class SpecificTranslatingConverter extends AbstractTranslatingConverter {

	public SpecificTranslatingConverter(ISpecificTranslator translator, IDataSink processor,
			Map<Long, Color> roofsColorMap) {
		super(translator, processor, roofsColorMap);
	}

	@Override
	protected void translateWay(Way way, List<Long> ids) throws Osm2xpBusinessException {
		Geometry geometry = getGeometry(ids);
		if (geometry != null) {
			List<Geometry> geoms = fix(Collections.singletonList(geometry));
			((ISpecificTranslator) translator).processWays(way.getId(), way.getTags(), geometry, geoms);
		}
	}

	@Override
	protected void translatePolys(long id, List<Tag> tagsModel, List<Polygon> cleanedPolys)
			throws Osm2xpBusinessException {
		((ISpecificTranslator) translator).processWays(id, tagsModel, null, cleanedPolys);
	}

}
