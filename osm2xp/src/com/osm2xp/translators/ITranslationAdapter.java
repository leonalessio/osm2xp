package com.osm2xp.translators;

import java.util.List;

import org.openstreetmap.osmosis.osmbinary.Osmformat.HeaderBBox;

import com.osm2xp.exceptions.Osm2xpBusinessException;
import com.osm2xp.model.osm.Node;
import com.osm2xp.model.osm.Tag;
import com.vividsolutions.jts.geom.Geometry;

public interface ITranslationAdapter {

	void processWays(long wayId, List<Tag> tags, Geometry originalGeometry, List<? extends Geometry> fixedGeometries);

	void init();

	void complete();

	void processNode(Node node) throws Osm2xpBusinessException;

	void processBoundingBox(HeaderBBox bbox);

	Boolean mustProcessPolyline(List<Tag> tags);

	Boolean mustStoreNode(Node node);

}
