package com.osm2xp.translators.impl;

import java.util.Random;

import com.osm2xp.model.osm.polygon.OsmPolygon;
import com.osm2xp.model.osm.polygon.OsmPolyline;
import com.osm2xp.utils.DsfObjectsProvider;
import com.osm2xp.utils.geometry.GeomUtils;
import com.osm2xp.generation.options.GlobalOptionsProvider;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.utils.osm.OsmUtils;
import com.osm2xp.utils.xplane.XplaneExclusionsHelper;
import com.osm2xp.writers.IHeaderedWriter;

import math.geom2d.Point2D;
import math.geom2d.polygon.LinearRing2D;

/**
 * Xplane 10 translator implementation. Generates XPlane scenery from osm data.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class Xplane10TranslatorImpl extends XPlaneTranslatorImpl {

	/**
	 * Constructor.
	 * 
	 * @param stats
	 *            stats object.
	 * @param writer
	 *            file writer.
	 * @param currentTile
	 *            current lat/lon tile.
	 * @param folderPath
	 *            generated scenery folder path.
	 * @param dsfObjectsProvider
	 *            dsf object provider.
	 */
	public Xplane10TranslatorImpl(IHeaderedWriter writer,
			Point2D currentTile, String folderPath,
			DsfObjectsProvider dsfObjectsProvider) {
		super(writer, currentTile, folderPath, dsfObjectsProvider);
	}

	/**
	 * Write streetlight objects in dsf file.
	 * 
	 * @param osmPolygon
	 *            osm road polygon
	 */
	public void writeStreetLightToDsf(OsmPolygon osmPolygon) {
		// init d'un entier pour modulo densit� street lights
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
		LinearRing2D polygon = osmPolygon.getPolygon();
		for (int i = 0; i < polygon.vertices().size(); i++) {
			if ((i % densityIndex) == 0) {
				Point2D lightLoc = polygon.vertex(i);
				polygon.setVertex(i, lightLoc.translate(0.0001, 0.0001));
				if (GeomUtils.compareCoordinates(lightLoc, currentTile)) {
					Random randomGenerator = new Random();
					int orientation = randomGenerator.nextInt(360);
					sb.append("OBJECT "
							+ dsfObjectsProvider.getRandomStreetLightObject()
							+ " " + (lightLoc.y()) + " " + (lightLoc.x()) + " "
							+ orientation);
					sb.append(LINE_SEP);
				}
			}
		}

		writer.write(sb.toString());
	}
	
}
