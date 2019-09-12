package com.osm2xp.generation.areas;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

import com.onpositive.classification.core.util.TagUtil;
import com.osm2xp.converters.impl.AbstractOSMDataConverter;
import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.core.model.osm.Way;
import com.osm2xp.datastore.IDataSink;
import com.osm2xp.generation.osm.OsmConstants;
import com.osm2xp.utils.osm.OsmUtils;

import math.geom2d.Box2D;

public class LanduseAreasAnalyzer extends AbstractOSMDataConverter {
	
	public LanduseAreasAnalyzer(IDataSink dataSink) {
		super(dataSink);
		AreaProvider.getInstance().clear();
	}

	@Override
	public void visit(Box2D box) {
		// Do nothing
		
	}

	@Override
	protected boolean mustStoreNode(Node node) {
		return true;
	}

	@Override
	protected boolean mustProcessPolyline(List<Tag> tagsModel) {
		return TagUtil.getValue(OsmConstants.LANDUSE_TAG, tagsModel) != null;
	}

	@Override
	protected void translateWay(Way way, List<Long> ids) throws Osm2xpBusinessException {
		List<Tag> tags = way.getTags();
		String landuse = OsmUtils.getTagValue(OsmConstants.LANDUSE_TAG, tags);
		if (!StringUtils.stripToEmpty(landuse).trim().isEmpty()) {
			Polygon poly = getPolygon(ids);
			if (poly != null) {
				List<Geometry> fixed = fix(Collections.singletonList(poly));
				for (Geometry geometry : fixed) {
					if (geometry instanceof Polygon) {
						MapArea area = new MapArea(landuse, (Polygon) geometry);
						AreaProvider.getInstance().addArea(area);
					}
				}
			}
		}
	}

	@Override
	protected void translatePolys(long id, List<Tag> tagsModel, List<Polygon> cleanedPolys)
			throws Osm2xpBusinessException {
		String landuse = OsmUtils.getTagValue(OsmConstants.LANDUSE_TAG, tagsModel);
		if (!StringUtils.stripToEmpty(landuse).trim().isEmpty()) {
			for (Polygon polygon : cleanedPolys) {
				List<Geometry> fixed = fix(Collections.singletonList(polygon));
				for (Geometry geometry : fixed) {
					if (geometry instanceof Polygon) {
						MapArea area = new MapArea(landuse, (Polygon) geometry);
						AreaProvider.getInstance().addArea(area);
					}
				}
			}
		}
	}
	
	@Override
	public void complete() {
		dataSink.setReadOnly(true);
	}

}
