package com.osm2xp.translators.impl;

import java.io.File;
import java.util.List;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.model.osm.polygon.OsmPolygon;
import com.osm2xp.model.osm.polygon.OsmPolyline;
import com.osm2xp.translators.ITranslator;
import com.osm2xp.utils.FilesUtils;
import com.osm2xp.utils.OsmUtils;
import com.osm2xp.utils.geometry.GeomUtils;
import com.osm2xp.utils.helpers.FlyLegacyOptionsHelper;
import com.osm2xp.utils.helpers.GuiOptionsHelper;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.polygon.LinearRing2D;

/**
 * Fly Legacy Translator implementation. Generates a .ofe file, that is used by
 * Fly Legacy simulator to build 3D objects.
 * 
 * @author Benjamin Blanchet, following Jean Sabatier specifications.
 * 
 */
public class FlyLegacyTranslatorImpl implements ITranslator {
	/**
	 * current lat/long tile.
	 */
	private Point2D currentTile;
	/**
	 * generated file folder path.
	 */
	private String folderPath;
	/**
	 * generated ofe file.
	 */
	private File ofeFile;
	/**
	 * number of generated objects.
	 */
	private int cptObjects;

	/**
	 * Constuctor.
	 * 
	 * @param currentTile
	 *            current lat/long tile.
	 * @param folderPath
	 *            folder path.
	 */
	public FlyLegacyTranslatorImpl(Point2D currentTile, String folderPath) {
		super();
		this.currentTile = currentTile;
		this.folderPath = folderPath;
		File file = new File(GuiOptionsHelper.getOptions().getCurrentFilePath());
		String fileName = file.getName().substring(0,
				file.getName().indexOf("."));
		this.ofeFile = new File(this.folderPath + File.separator + fileName
				+ "_" + currentTile.y + "_" + currentTile.x + ".ofe");
		writeAreaHeader(currentTile);

	}

	/**
	 * write Area sequence to fly file.
	 * 
	 * @param coordinates
	 *            Point2D lat/long.
	 */
	private void writeAreaHeader(Point2D coordinates) {
		int latitude = (int) coordinates.y;
		int longitude = (int) coordinates.x;
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Area SW[" + latitude + "," + longitude + "]");
		stringBuilder.append(" NE[" + (latitude + 1) + "," + (longitude + 1)
				+ "]\n");
		// add area sequence
		FilesUtils
				.writeTextToFile(this.ofeFile, stringBuilder.toString(), true);

	}

	@Override
	public void processNode(Node node) throws Osm2xpBusinessException {
		LinearRing2D polygon = new LinearRing2D();
		//dummy polygon as tags placeholder
		OsmPolyline osmPolygon =new OsmPolygon(node.getId(), node.getTags(), null, false);
		// get list of watched tags that are also in the osm polygon
		List<Tag> matchingTags = OsmUtils.getMatchingTags(
				FlyLegacyOptionsHelper.getOptions().getWatchedTagsList()
						.getTags(), osmPolygon);
		if (matchingTags != null) {
			StringBuilder stringBuilder = new StringBuilder();
			// remove last node for fly specification
			polygon.removePoint(polygon.getLastPoint());
			stringBuilder.append("Start " + ++cptObjects + " id="
					+ osmPolygon.getId() + "\n");
			// write all the tags for this polygon
			for (Tag matchingTag : matchingTags) {
				stringBuilder.append("tag(" + matchingTag.getKey() + "="
						+ matchingTag.getValue() + ")\n");
			}
				stringBuilder
						.append("P(" + node.getLat() + "," + node.getLon() + ")\n");
			FilesUtils.writeTextToFile(this.ofeFile, stringBuilder.toString(),
					true);
		}
	}

	@Override
	public void processPolyline(OsmPolyline osmPolyline)
			throws Osm2xpBusinessException {
		LinearRing2D polygon = new LinearRing2D();

		// if the processor sent back a complete list of nodes
		// construct a polygon from those nodes

		polygon = GeomUtils.getPolygonFromOsmNodes(osmPolyline.getNodes());
		// simplify shape if checked and if necessary
		if (GuiOptionsHelper.getOptions().isSimplifyShapes() && osmPolyline instanceof OsmPolygon
				&& !((OsmPolygon) osmPolyline).isSimplePolygon()) {
			polygon = GeomUtils.simplifyPolygon(polygon);
		}

		// get list of watched tags that are also in the osm polygon
		List<Tag> matchingTags = OsmUtils.getMatchingTags(
				FlyLegacyOptionsHelper.getOptions().getWatchedTagsList()
						.getTags(), osmPolyline);
		if (matchingTags != null) {
			StringBuilder stringBuilder = new StringBuilder();
			// remove last node for fly specification
			polygon.removePoint(polygon.getLastPoint());
			stringBuilder.append("Start " + ++cptObjects + " id="
					+ osmPolyline.getId() + "\n");
			// write all the tags for this polygon
			for (Tag matchingTag : matchingTags) {
				stringBuilder.append("tag(" + matchingTag.getKey() + "="
						+ matchingTag.getValue() + ")\n");
			}
			for (Point2D point2d : polygon.getVertices()) {
				stringBuilder
						.append("V(" + point2d.y + "," + point2d.x + ")\n");
			}
			FilesUtils.writeTextToFile(this.ofeFile, stringBuilder.toString(),
					true);
		}

	}

	@Override
	public void complete() {
		FilesUtils.writeTextToFile(this.ofeFile, "END", true);
		Osm2xpLogger.info("Fly! Legacy buildings file finished.");
	}

	@Override
	public void init() {
		Osm2xpLogger.info("Starting Fly! Legacy file for tile "
				+ this.currentTile.y + "/" + this.currentTile.x + ".");
	}

	@Override
	public boolean mustStoreNode(Node node) {
		Boolean result = true;
		if (!GuiOptionsHelper.getOptions().isSinglePass()) {
			result = GeomUtils.compareCoordinates(currentTile, node);
		}
		return result;
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
		return Integer.MAX_VALUE; //TODO is this supported?
	}

}
