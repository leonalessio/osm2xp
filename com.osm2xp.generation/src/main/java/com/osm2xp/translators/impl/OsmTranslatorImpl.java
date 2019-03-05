package com.osm2xp.translators.impl;

import java.util.ArrayList;
import java.util.List;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.generation.options.GlobalOptionsProvider;
import com.osm2xp.model.osm.polygon.OsmPolygon;
import com.osm2xp.model.osm.polygon.OsmPolyline;
import com.osm2xp.translators.ITranslator;
import com.osm2xp.utils.geometry.GeomUtils;
import com.osm2xp.utils.osm.OsmUtils;
import com.osm2xp.writers.IWriter;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.polygon.LinearCurve2D;
import math.geom2d.polygon.LinearRing2D;

/**
 * OSM Translator implementation. Generates .osm files. Usefull to split a .pbf
 * file into multiples .osm files, it will also generate small files as only
 * building/forests/objects will be exported .
 * 
 * @author Benjamin Blanchet
 * 
 */
public class OsmTranslatorImpl implements ITranslator {
	/**
	 * file writer.
	 */
	private IWriter writer;
	/**
	 * current lat/long tile.
	 */
	private Point2D currentTile;
	/**
	 * osm node index.
	 */
	private int nodeIndex = 1;

	/**
	 * Constructor.
	 * 
	 * @param writer
	 *            file writer.
	 * @param currentTile
	 *            current lat/long tile.
	 */
	public OsmTranslatorImpl(IWriter writer, Point2D currentTile) {
		this.writer = writer;
		this.currentTile = currentTile;

	}

	/**
	 * write a node in the osm file.
	 * 
	 * @param node
	 *            osm node.
	 */
	private void writeNode(Node node) {

		if (node.getTags().isEmpty()) {
			writer.write("<node id=\"" + node.getId() + "\" lat=\""
					+ node.getLat() + "\" lon=\"" + node.getLon()
					+ "\" version=\"1\" />\n");
		} else {
			writer.write("<node id=\"" + node.getId() + "\" lat=\""
					+ node.getLat() + "\" lon=\"" + node.getLon()
					+ "\" version=\"1\" >\n");
			for (Tag tag : node.getTags()) {
				String normalizedTag = OsmUtils.getNormalizedTagText(tag);
				if (normalizedTag != null) {
					writer.write(normalizedTag);
				}
			}
			writer.write("</node>\n");
		}
	}

	@Override
	public void init() {
		Osm2xpLogger.info("Starting OpenStreetMap xml generation of tile "
				+ (int) currentTile.y() + "/" + (int) currentTile.x());
		writer.init(currentTile);
	}

	/**
	 * write a way (polygon) in the osm file.
	 * 
	 * @param osmPolyline
	 *            osm polygon.
	 */
	private void writeWay(OsmPolyline osmPolyline) {
		// we create a polygon from the way nodes
		LinearCurve2D poly = GeomUtils.getPolylineFromOsmNodes(osmPolyline
				.getNodes());

		// simplify shape if checked and if necessary
		if (GlobalOptionsProvider.getOptions().isSimplifyShapes() && osmPolyline instanceof OsmPolygon
				&& !((OsmPolygon) osmPolyline).isSimplePolygon()) {
			poly = GeomUtils.simplifyPolygon((LinearRing2D) poly);
		}
		List<Node> nodeList = new ArrayList<Node>();
		for (Point2D point : poly.vertices()) {
			Node node = new Node(null, point.y(), point.x(), nodeIndex);
			writeNode(node);
			nodeList.add(node);
			nodeIndex++;
		}
		writer.write("<way id=\"" + osmPolyline.getId()
				+ "\" visible=\"true\" version=\"2\" >\n");

		for (Node node : nodeList) {
			writer.write("<nd ref=\"" + node.getId() + "\"/>\n");
		}

		for (Tag tag : osmPolyline.getTags()) {
			String normalizedTag = OsmUtils.getNormalizedTagText(tag);
			if (normalizedTag != null) {
				writer.write(normalizedTag);
			}

		}
		writer.write("</way>\n");

	}

	@Override
	public void complete() {
		writer.complete();
		Osm2xpLogger.info("Osm file complete");

	}

	@Override
	public void processNode(Node node) throws Osm2xpBusinessException {
	}

	@Override
	public void processPolyline(OsmPolyline osmPolygon)
			throws Osm2xpBusinessException {

		if (OsmUtils.isBuilding(osmPolygon.getTags())
				&& !OsmUtils.isExcluded(osmPolygon.getTags(),
						osmPolygon.getId())
				|| OsmUtils.isForest(osmPolygon.getTags())
				|| OsmUtils.isObject(osmPolygon.getTags())) {
			writeWay(osmPolygon);

		}

	}

	@Override
	public boolean mustStoreNode(Node node) {
		return GeomUtils.compareCoordinates(currentTile, node);
	}

	@Override
	public void processBoundingBox(Box2D bbox) {
		// Do nothing
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
