package com.osm2xp.parsers.impl;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.osm2xp.dataProcessors.IDataSink;
import com.osm2xp.exceptions.Osm2xpBusinessException;
import com.osm2xp.model.osm.OsmPolyline;
import com.osm2xp.model.osm.OsmPolylineFactory;
import com.osm2xp.model.osm.Tag;
import com.osm2xp.model.osm.Way;
import com.osm2xp.translators.ITranslator;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

public class GeneralTranslatingConverter extends AbstractTranslatingConverter {

	public GeneralTranslatingConverter(ITranslator translator, IDataSink processor,
			Map<Long, Color> roofsColorMap) {
		super(translator, processor, roofsColorMap);
	}

	@Override
	protected void translateWay(Way way, List<Long> ids) throws Osm2xpBusinessException {
		Geometry geometry = getGeometry(ids);
		if (geometry == null) {
			return;
		}
		List<Geometry> fixed = fix(Collections.singletonList(geometry));
		for (Geometry curGeo : fixed) {
			List<OsmPolyline> polylines = OsmPolylineFactory.createPolylinesFromJTSGeometry(way.getId(), way.getTags(), curGeo, false);
			for (OsmPolyline osmPolyline : polylines) {
				((ITranslator) translator).processPolyline(osmPolyline);
			}
		}
	}

	@Override
	protected void translatePolys(long id, List<Tag> tagsModel, List<Polygon> cleanedPolys) throws Osm2xpBusinessException {
		for (Geometry curGeo : cleanedPolys) {
			List<OsmPolyline> polylines = OsmPolylineFactory.createPolylinesFromJTSGeometry(id, tagsModel, curGeo, false);
			for (OsmPolyline osmPolyline : polylines) {
				((ITranslator) translator).processPolyline(osmPolyline);
			}
		}

	}

}
