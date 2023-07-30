package com.osm2xp.converters.impl;

import java.util.Collections;
import java.util.List;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.core.model.osm.Way;
import com.osm2xp.datastore.IDataSink;
import com.osm2xp.model.osm.polygon.OsmPolyline;
import com.osm2xp.model.osm.polygon.OsmPolylineFactory;
import com.osm2xp.translators.ITranslator;

public class GeneralTranslatingConverter extends AbstractTranslatingConverter {

	public GeneralTranslatingConverter(ITranslator translator, IDataSink processor) {
		super(translator, processor);
	}

	@Override
	public void visit(Node node) {
		super.visit(node);
		try {
			translator.processNode(node);
		} catch (Osm2xpBusinessException e) {
			Osm2xpLogger.error(e);
		}
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
