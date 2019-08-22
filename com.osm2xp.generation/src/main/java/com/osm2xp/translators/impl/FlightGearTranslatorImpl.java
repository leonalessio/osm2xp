package com.osm2xp.translators.impl;

import java.io.File;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.model.osm.polygon.OsmPolyline;
import com.osm2xp.translators.ITranslator;
import com.osm2xp.utils.FilesUtils;
import com.osm2xp.utils.geometry.GeomUtils;
import com.osm2xp.generation.options.FlightGearOptionsProvider;
import com.osm2xp.generation.options.GlobalOptionsProvider;
import com.osm2xp.generation.options.ObjectFile;
import com.osm2xp.generation.options.rules.TagsRule;
import com.osm2xp.utils.osm.OsmUtils;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.polygon.LinearRing2D;

/**
 * FlightGear Translator implementation.
 * 
 * @author Benjamin Blanchet.
 * 
 */
public class FlightGearTranslatorImpl implements ITranslator {
	/**
	 * current lat/long tile.
	 */
	private Point2D currentTile;
	/**
	 * generated file folder path.
	 */
	private String folderPath;
	/**
	 * generated xml file.
	 */
	private File xmlFile;

	private static final String FLIGHT_GEAR_OBJECT_DECLARATION = "OBJECT_SHARED_AGL {0} {1} {2} {3} {4} {5} {6}\n";

	/**
	 * Constuctor.
	 * 
	 * @param currentTile
	 *            current lat/long tile.
	 * @param folderPath
	 *            folder path.
	 */
	public FlightGearTranslatorImpl(Point2D currentTile, String folderPath) {
		super();
		this.currentTile = currentTile;
		this.folderPath = folderPath;
		File file = new File(GlobalOptionsProvider.getOptions().getCurrentFilePath());
		String fileName = file.getName().substring(0,
				file.getName().indexOf("."));
		this.xmlFile = new File(this.folderPath + File.separator + fileName
				+ "_" + currentTile.y() + "_" + currentTile.x() + ".stg");

	}

	@Override
	public void processNode(Node node) throws Osm2xpBusinessException {
	}

	@Override
	public void processPolyline(OsmPolyline osmPolygon)
			throws Osm2xpBusinessException {
		if (osmPolygon != null && osmPolygon.getNodes() != null) {
			// check if the current polygon has some tags this translator wants
			// to use
			List<TagsRule> matchingTags = OsmUtils.getMatchingRules(
					FlightGearOptionsProvider.getOptions().getObjectsRules()
							.getRules(), osmPolygon);
			if (matchingTags != null && !matchingTags.isEmpty()) {

				LinearRing2D polygon = new LinearRing2D();
				// if the dataSink sent back a complete list of nodes
				// construct a polygon from those nodes
				polygon = GeomUtils.getPolygonFromOsmNodes(osmPolygon
						.getNodes());
				// inject it into the scenery file.
				injectPolygonIntoScenery(polygon, matchingTags);
			}
		}
	}

	private void injectPolygonIntoScenery(LinearRing2D polygon,
			List<TagsRule> matchingTagsRules) {

		// simplify shape until we have a simple rectangle

		LinearRing2D simplifiedPolygon = GeomUtils.simplifyPolygon(polygon);

		// shuffle matching tags rules
		Collections.shuffle(matchingTagsRules);
		TagsRule logicRule = matchingTagsRules.get(0);
		// shuffle objects
		Collections.shuffle(logicRule.getObjectsFiles());
		// select object that will be injected.
		ObjectFile object = logicRule.getObjectsFiles().get(0);

		if (object != null && StringUtils.isNotBlank(object.getPath())) {

			// compute center point of the polygon.
			Point2D centerPoint = GeomUtils.getPolylineCenter(simplifiedPolygon);
			// params : <object-path> <longitude> <latitude>
			// <elevation-offset-m> <heading-deg> <pitch-deg> <roll-deg>
			String objectDeclaration = MessageFormat.format(
					FLIGHT_GEAR_OBJECT_DECLARATION,
					new Object[] { object.getPath(), centerPoint.y(),
							centerPoint.x(), 0, 1, 0, 0 });
			objectDeclaration = objectDeclaration.replaceAll(",", ".");
			FilesUtils.writeTextToFile(this.xmlFile, objectDeclaration, true);

		}
	}

	@Override
	public void complete() {
		Osm2xpLogger.info("FlightGear file finished.");
	}

	@Override
	public void init() {
		Osm2xpLogger.info("Starting FlightGear file for tile "
				+ this.currentTile.y() + "/" + this.currentTile.x() + ".");
	}

	@Override
	public boolean mustStoreNode(Node node) {
		return GeomUtils.compareCoordinates(currentTile, node);
	}

	@Override
	public boolean mustProcessPolyline(List<Tag> tags) {
		return false;
	}

	
	@Override
	public void processBoundingBox(Box2D bbox) {
		// Do nothing
	}
	
	@Override
	public int getMaxHoleCount(List<Tag> tags) {
		return Integer.MAX_VALUE; //TODO is this supported for FlightGear ?
	}

}
