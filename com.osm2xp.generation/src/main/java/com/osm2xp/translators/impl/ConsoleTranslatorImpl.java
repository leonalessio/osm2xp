package com.osm2xp.translators.impl;

import java.util.List;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.generation.options.GlobalOptionsProvider;
import com.osm2xp.model.osm.polygon.OsmPolyline;
import com.osm2xp.translators.ITranslator;
import com.osm2xp.utils.geometry.GeomUtils;
import com.osm2xp.utils.osm.OsmUtils;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.polygon.LinearCurve2D;
import math.geom2d.polygon.LinearRing2D;

/**
 * Console translator implementation. A basic translator for xplane debuging
 * purpose. Output text information about osm data being parser.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class ConsoleTranslatorImpl implements ITranslator {
	/**
	 * Minimum vectors for a building.
	 */
	private final static int BUILDING_MIN_VECTORS = 3;
	/**
	 * Maximum vectors for a building.
	 */
	private final static int BUILDING_MAX_VECTORS = 512;
	/**
	 * current lat/long tile.
	 */
	private Point2D currentTile;

	/**
	 * Constructor.
	 * 
	 * @param currentTile
	 *            current lat/long tile.
	 * @param dsfObjectsProvider
	 */
	public ConsoleTranslatorImpl(Point2D currentTile) {
		this.currentTile = currentTile;

	}

	@Override
	public void processNode(Node node) throws Osm2xpBusinessException {
		if (GeomUtils.compareCoordinates(currentTile, node)) {

			List<Tag> tagsOfInterest = OsmUtils.removeCommonTags(node.getTags());

			if (tagsOfInterest != null && !tagsOfInterest.isEmpty()) {
				StringBuilder nodeDetail = new StringBuilder();
				nodeDetail.append("Complex node at " + node.getLat() + "/"
						+ node.getLon());
				for (Tag tag : tagsOfInterest) {
					nodeDetail.append("\n" + tag.toString());
				}
				nodeDetail.append("\n");
				Osm2xpLogger.info(nodeDetail.toString());
			}
		}

	}

	@Override
	public void processPolyline(OsmPolyline polyline)
			throws Osm2xpBusinessException {
		LinearCurve2D poly = new LinearRing2D();
		poly = GeomUtils.getPolylineFromOsmNodes(polyline.getNodes());
		if (!processBuilding(polyline, (LinearRing2D) poly)) {
			processForest(polyline,  (LinearRing2D) poly);
		}
	}

	@Override
	public void complete() {
	}

	@Override
	public void init() {
		Osm2xpLogger.info("Starting console debug output for tile lat "
				+ currentTile.y() + " long " + currentTile.x());
	}

	private boolean processBuilding(OsmPolyline polygon, LinearRing2D poly) {
		Boolean result = false;
		if (OsmUtils.isBuilding(polygon.getTags())
				&& !OsmUtils.isExcluded(polygon.getTags(), polygon.getId())
				&& poly.vertexNumber() > BUILDING_MIN_VECTORS
				&& poly.vertexNumber() < BUILDING_MAX_VECTORS) {
			Double[] extremesVectors = GeomUtils.computeExtremeVectors(poly);
			Double minVector = extremesVectors[0];
			Double maxVector = extremesVectors[1];
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("building id " + polygon.getId() + "\n");
			stringBuilder.append("smallest vector " + minVector
					+ ", largest vector " + maxVector + "\n");
			stringBuilder.append("area " + (poly.area() * 100000 * 100000)
					+ "\n");

			stringBuilder.append("polygon is made of  "
					+ (poly.vertexNumber() - 1) + " points\n");

			List<Tag> tagsOfInterest = OsmUtils.removeCommonTags(polygon
					.getTags());

			if (tagsOfInterest != null && !tagsOfInterest.isEmpty()) {
				stringBuilder.append("Building tags:\n  ");
				for (Tag tag : tagsOfInterest) {
					stringBuilder.append(tag.toString() + "\n");
				}
			}

			Osm2xpLogger.info(stringBuilder.toString());
			result = true;
		}
		return result;
	}

	/**
	 * @param way
	 * @param poly
	 * @return
	 */
	private boolean processForest(OsmPolyline osmPolygon, LinearRing2D poly) {

		if (OsmUtils.isOsmForest(osmPolygon.getTags())) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("forest id " + osmPolygon.getId() + "\n");
			stringBuilder.append("area "
					+ ((poly.area() * 100000) * 100000) + "\n");
			stringBuilder.append("polygon is made of  "
					+ (poly.vertexNumber() - 1) + " points\n");
			Osm2xpLogger.info(stringBuilder.toString());
		}

		return false;
	}

	@Override
	public boolean mustStoreNode(Node node) {
		Boolean result = true;
		if (!GlobalOptionsProvider.getOptions().isSinglePass()) {
			result = GeomUtils.compareCoordinates(currentTile, node);
		}
		return result;
	}

	@Override
	public void processBoundingBox(Box2D bbox) {
		if (bbox != null) {
			Osm2xpLogger.info("Bounding box: " + bbox.getMinX() + "," + bbox.getMinY() + "," + bbox.getMaxX() + "," + bbox.getMaxY());
		}
	}

	@Override
	public boolean mustProcessPolyline(List<Tag> tags) {
		return false;
	}

	@Override
	public int getMaxHoleCount(List<Tag> tags) {
		return Integer.MAX_VALUE;
	}
}
