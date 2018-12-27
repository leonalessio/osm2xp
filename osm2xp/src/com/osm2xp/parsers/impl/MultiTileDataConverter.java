package com.osm2xp.parsers.impl;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.openstreetmap.osmosis.osmbinary.Osmformat.HeaderBBox;

import com.osm2xp.dataProcessors.IDataSink;
import com.osm2xp.exceptions.Osm2xpBusinessException;
import com.osm2xp.gui.Activator;
import com.osm2xp.model.osm.Node;
import com.osm2xp.model.osm.Tag;
import com.osm2xp.translators.ISpecificTranslator;
import com.osm2xp.translators.TranslatorBuilder;
import com.osm2xp.translators.impl.TileTranslationAdapter;
import com.osm2xp.utils.OsmUtils;
import com.osm2xp.utils.logging.Osm2xpLogger;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

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
	private Set<Point2D> tiles = new HashSet<Point2D>();
	private String folderPath;
	private File binaryFile;

	public MultiTileDataConverter(IDataSink processor, File binaryFile, String folderPath, Map<Long, Color> roofsColorMap) {
		super(processor, roofsColorMap);
		this.binaryFile = binaryFile;
		this.folderPath = folderPath;
		translationAdapters.addAll(TranslatorBuilder.createAdditinalAdapters(folderPath));
	}
	
	public void complete() {
		for (ISpecificTranslator tileTranslationAdapter : translationAdapters) {
			tileTranslationAdapter.complete();
		}
	}
	
	public void pointParsed(double lonf, double latf) {
		Point2D cleanedLoc = new Point2D((int) Math.floor(lonf), (int) Math.floor(latf));
		if (!tiles.contains(cleanedLoc)) {
			Osm2xpLogger.info("Detected tile (" + cleanedLoc.x + ", " + cleanedLoc.y + ")");
			TileTranslationAdapter adapter = new TileTranslationAdapter(cleanedLoc, processor, TranslatorBuilder.getTranslator(binaryFile, cleanedLoc, folderPath));
			adapter.init();
			translationAdapters.add(adapter);
			tiles.add(cleanedLoc);
		}
	}
	
	@Override
	public void visit(Node node) {
		// give the node to the translator for processing 
		for (ISpecificTranslator adapter : translationAdapters) {
			try {
				adapter.processNode(node);
			} catch (Osm2xpBusinessException e) {
				Activator.log(e);
			}
		}
		super.visit(node);
	}

	@Override
	protected boolean mustStoreNode(Node node) {
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
			Activator.log(IStatus.WARNING, "Way/Relation of type " + type + " with id " + id + " is invalid, unable to fix it automatically. Possible reasons - self-intersection or partial node information.");
		}
		for (ISpecificTranslator adapter : translationAdapters) {
			adapter.processWays(id, tagsModel, null, cleanedPolys);
		}
	}

	@Override
	protected void translateWay(com.osm2xp.model.osm.Way way, List<Long> ids) throws Osm2xpBusinessException {
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
	public void visit(HeaderBBox box) {
		for (ISpecificTranslator tileTranslationAdapter : translationAdapters) {
			tileTranslationAdapter.processBoundingBox(box);
		}
		
	}

	public int getTilesCount() {
		return translationAdapters.size();
	}

}
