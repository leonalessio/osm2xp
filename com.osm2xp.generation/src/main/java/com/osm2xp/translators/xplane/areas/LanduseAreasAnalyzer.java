package com.osm2xp.translators.xplane.areas;

import java.util.List;

import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

import com.onpositive.classification.core.util.TagUtil;
import com.osm2xp.converters.impl.AbstractOSMDataConverter;
import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.core.model.osm.Way;
import com.osm2xp.datastore.IDataSink;

import math.geom2d.Box2D;

public class LanduseAreasAnalyzer extends AbstractOSMDataConverter {
	
	public LanduseAreasAnalyzer(IDataSink dataSink) {
		super(dataSink);
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
		Geometry geometry = getGeometry(ids);
		Envelope envelope = geometry.getEnvelopeInternal();
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void translatePolys(long id, List<Tag> tagsModel, List<Polygon> cleanedPolys)
			throws Osm2xpBusinessException {
		// TODO Auto-generated method stub
		
	}

}
