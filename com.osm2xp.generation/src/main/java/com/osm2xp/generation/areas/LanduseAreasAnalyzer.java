package com.osm2xp.generation.areas;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.locationtech.jts.geom.Polygon;

import com.onpositive.classification.core.util.TagUtil;
import com.osm2xp.converters.impl.AbstractOSMDataConverter;
import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.core.model.osm.Way;
import com.osm2xp.datastore.IDataSink;
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
		return TagUtil.getValue("landuse", tagsModel) != null;
	}

	@Override
	protected void translateWay(Way way, List<Long> ids) throws Osm2xpBusinessException {
		List<Tag> tags = way.getTags();
		String landuse = OsmUtils.getTagValue("landuse", tags);
		if (!StringUtils.stripToEmpty(landuse).trim().isEmpty()) {
			Polygon poly = getPolygon(ids);
			if (poly != null) {
				MapArea area = new MapArea(landuse, poly);
				AreaProvider.getInstance().addArea(area);
			}
		}
	}

	@Override
	protected void translatePolys(long id, List<Tag> tagsModel, List<Polygon> cleanedPolys)
			throws Osm2xpBusinessException {
		String landuse = OsmUtils.getTagValue("landuse", tagsModel);
		if (!StringUtils.stripToEmpty(landuse).trim().isEmpty()) {
			for (Polygon polygon : cleanedPolys) {
				MapArea area = new MapArea(landuse, polygon);
				AreaProvider.getInstance().addArea(area);
			}
		}
	}
	
	@Override
	public void complete() {
		dataSink.setReadOnly(true);
	}

}
