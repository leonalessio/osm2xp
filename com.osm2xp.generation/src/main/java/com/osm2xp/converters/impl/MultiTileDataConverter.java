package com.osm2xp.converters.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Polygon;

import com.osm2xp.core.exceptions.DataSinkException;
import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.datastore.IDataSink;
import com.osm2xp.generation.collections.PointSet;
import com.osm2xp.translators.ISpecificTranslator;
import com.osm2xp.translators.ITranslatorProvider;
import com.osm2xp.translators.impl.TileTranslationAdapter;
import com.osm2xp.utils.osm.OsmUtils;

import math.geom2d.Box2D;
import math.geom2d.Point2D;

/**
 * OSM dta converter with ability to automatically detect tiles and split parsed data around several polygons 
 * using JTS library to clip, cut, simplify and fix OSM polygons 
 * 
 * @author Dmitry Karpenko, OnPositive
 * 
 */
public class MultiTileDataConverter extends AbstractOSMDataConverter {
	
	private List<ISpecificTranslator> translationAdapters = new ArrayList<>();
	private Set<Point2D> tiles = new PointSet();
	private Box2D boundingBox;
	private ITranslatorProvider translatorProvider;

	public MultiTileDataConverter(IDataSink processor, ITranslatorProvider translatorProvider) {
		super(processor);
		this.translatorProvider = translatorProvider;
		translationAdapters.addAll(translatorProvider.createAdditinalAdapters());
	}
	
	public void complete() {
		for (ISpecificTranslator tileTranslationAdapter : translationAdapters) {
			tileTranslationAdapter.complete();
		}
		try {
			dataSink.complete();
		} catch (DataSinkException e) {
			Osm2xpLogger.error(e);
		}
	}
	
	public void pointParsed(double lonf, double latf) {
		Point2D cleanedLoc = new Point2D((int) Math.floor(lonf), (int) Math.floor(latf));
		if (!tiles.contains(cleanedLoc)) {
			Osm2xpLogger.info("Detected tile (" + cleanedLoc.x() + ", " + cleanedLoc.y() + ")");
			addTranslationAdapter(cleanedLoc);
		}
	}

	protected void addTranslationAdapter(Point2D point) {
		TileTranslationAdapter adapter = new TileTranslationAdapter(point, dataSink, translatorProvider.getTranslator(point));
		adapter.init();
		translationAdapters.add(adapter);
		tiles.add(point);
		if (boundingBox != null) {
			adapter.processBoundingBox(boundingBox);
		}
	}
	
	@Override
	public void visit(Node node) {
		pointParsed(node.getLon(), node.getLat());
		// give the node to the translator for processing 
		for (ISpecificTranslator adapter : translationAdapters) {
			try {
				adapter.processNode(node);
			} catch (Exception e) {
				Osm2xpLogger.log(e);
			}
		}
		super.visit(node);
	}

	@Override
	protected boolean mustStoreNode(Node node) {
		if (dataSink.isCompleted()) {
			return false;
		}
		for (ISpecificTranslator tileTranslationAdapter : translationAdapters) {
			if (tileTranslationAdapter.mustStoreNode(node)) {
				return true;		
			}
		}
		return false;
	}

	@Override
	protected boolean mustProcessPolyline(List<Tag> tagsModel) {
		for (ISpecificTranslator adapter : translationAdapters) {
			if (adapter.mustProcessPolyline(tagsModel)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected void translatePolys(long id, List<Tag> tagsModel, List<Polygon> cleanedPolys) {
		if (cleanedPolys.isEmpty()) {
			String type = OsmUtils.getReadableType(tagsModel);
			Osm2xpLogger.warning( "Way/Relation of type " + type + " with id " + id + " is invalid, unable to fix it automatically. Possible reasons - self-intersection or partial node information.");
		}
		for (ISpecificTranslator adapter : translationAdapters) {
			adapter.processWays(id, tagsModel, null, cleanedPolys);
		}
	}

	@Override
	protected void translateWay(com.osm2xp.core.model.osm.Way way, List<Long> ids) throws Osm2xpBusinessException {
		Geometry geometry = getGeometry(ids);
		if (geometry == null) {
			return;
		}
		List<Geometry> fixed = fix(Collections.singletonList(geometry));
		for (ISpecificTranslator adapter : translationAdapters) {
			adapter.processWays(way.getId(), way.getTags(), geometry, fixed);
		}
		if (fixed.isEmpty()) {
			return;
		} 
	}

	@Override
	public void visit(Box2D boundingBox) {
		this.boundingBox = boundingBox;
		for (ISpecificTranslator tileTranslationAdapter : translationAdapters) {
			tileTranslationAdapter.processBoundingBox(boundingBox);
		}
		
	}

	public int getTilesCount() {
		return tiles.size();
	}

}
