package com.osm2xp.translators.xplane;

import java.util.List;
import java.util.Random;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.model.osm.polygon.OsmPolyline;
import com.osm2xp.model.stats.GenerationStats;
import com.osm2xp.utils.DsfObjectsProvider;
import com.osm2xp.utils.geometry.GeomUtils;
import com.osm2xp.utils.osm.OsmUtils;
import com.osm2xp.writers.IHeaderedWriter;
import com.osm2xp.writers.IWriter;

import math.geom2d.Point2D;
import math.geom2d.polygon.LinearCurve2D;

/**
 * Xplane 9 translator implementation. Generates Xplane scenery from osm data.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class Xplane9TranslatorImpl extends XPlaneTranslatorImpl {

	/**
	 * current lat/long tile.
	 */
	private Point2D currentTile;
	/**
	 * stats object.
	 */
	private GenerationStats stats;
	/**
	 * file writer.
	 */
	private IWriter writer;
	/**
	 * dsf object provider.
	 */
	private DsfObjectsProvider dsfObjectsProvider;

	static int merde;

	/**
	 * Constructor.
	 * 
	 * @param stats
	 *            stats object.
	 * @param writer
	 *            file writer.
	 * @param currentTile
	 *            current lat/long tile.
	 * @param folderPath
	 *            generated scenery folder path.
	 * @param dsfObjectsProvider
	 *            dsf object provider.
	 */
	public Xplane9TranslatorImpl(IHeaderedWriter writer,
			Point2D currentTile, String folderPath,
			DsfObjectsProvider dsfObjectsProvider) {
		super(writer, currentTile, folderPath, dsfObjectsProvider);
	}

	

	/**
	 * Write streetlight objects in dsf file.
	 * 
	 * @param osmPolyline
	 *            osm road polygon
	 */
	public void writeStreetLightToDsf(OsmPolyline osmPolyline) {
		// init d'un entier pour modulo densit
		Integer densityIndex = 0;
		if (XPlaneOptionsProvider.getOptions().getLightsDensity() == 0) {
			densityIndex = 10;
		} else {
			if (XPlaneOptionsProvider.getOptions().getLightsDensity() == 1) {
				densityIndex = 5;
			} else {
				if (XPlaneOptionsProvider.getOptions().getLightsDensity() == 2)
					densityIndex = 3;
			}
		}
		StringBuffer sb = new StringBuffer();
		LinearCurve2D polygon = osmPolyline.getPolyline();
		for (int i = 0; i < polygon.vertices().size(); i++) {
			if ((i % densityIndex) == 0) {
				Point2D lightLoc = polygon.vertex(i);
				polygon.setVertex(i, lightLoc.translate(0.0001, 0.0001));
				if (GeomUtils.compareCoordinates(lightLoc, currentTile)) {
					Random randomGenerator = new Random();
					int orientation = randomGenerator.nextInt(360);
					sb.append("OBJECT "
							+ dsfObjectsProvider.getRandomStreetLightObject()
							+ " " + (lightLoc.x()) + " " + (lightLoc.y()) + " "
							+ orientation);
					sb.append(System.getProperty("line.separator"));
				}
			}
		}

		writer.write(sb.toString());
	}

	@Override
	public void processPolyline(OsmPolyline osmPolygon)
			throws Osm2xpBusinessException {

		// polygon is null or empty don't process it
		if (osmPolygon.getNodes() != null && !osmPolygon.getNodes().isEmpty()) {
			List<OsmPolyline> polylines = preprocess(osmPolygon);
			// try to transform those polygons into dsf objects.
			for (OsmPolyline poly : polylines) {
				// try to generate a 3D object
				if (!process3dObject(poly)) {
					// nothing generated? try to generate a facade building.
					if (!processBuilding(poly)) {
						// nothing generated? try to generate a forest.
						if (!forestTranslator.handlePoly(poly)) {
							// still nothing? try to generate a streetlight.
							if (!processStreetLights(poly)) {
								processByHandlers(poly);
							}
						}
					}
				}
			}
		}
	}

	
	/**
	 * send a streetLight in the dsf file.
	 * 
	 * @param poly
	 *            osm polygon
	 * @return true if a streetlight has been written in the dsf file.
	 */
	private boolean processStreetLights(OsmPolyline poly) {
		Boolean result = false;
		if (XPlaneOptionsProvider.getOptions().isGenerateStreetLights()
				&& OsmUtils.isTagInTagsList("highway", "residential",
						poly.getTags())) {
			writeStreetLightToDsf(poly);
			result = true;
		}
		return result;
	}

}
