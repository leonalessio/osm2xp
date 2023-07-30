package com.osm2xp.translators.xplane;

import java.util.List;

import com.osm2xp.model.osm.polygon.OsmPolygon;
import com.osm2xp.model.osm.polygon.OsmPolyline;
import com.osm2xp.utils.geometry.GeomUtils;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.generation.xplane.resources.DsfObjectsProvider;
import com.osm2xp.generation.xplane.resources.XPOutputFormat;
import com.osm2xp.writers.IWriter;

import math.geom2d.polygon.LinearRing2D;

public class XPForestTranslator extends XPWritingTranslator {

	private DsfObjectsProvider dsfObjectsProvider;
	private XPOutputFormat outputFormat;

	public XPForestTranslator(IWriter writer, DsfObjectsProvider dsfObjectsProvider, XPOutputFormat outputFormat) {
		super(writer);
		this.dsfObjectsProvider = dsfObjectsProvider;
		this.outputFormat = outputFormat;
	}

	@Override
	public boolean handlePoly(OsmPolyline osmPolyline) {
		if (osmPolyline instanceof OsmPolygon && XPlaneOptionsProvider.getOptions().isGenerateFor()) {
			Integer[] forestIndexAndDensity = dsfObjectsProvider
					.getRandomForestIndexAndDensity(osmPolyline.getTags());
			if (forestIndexAndDensity != null) {
				if (!osmPolyline.isValid()) {
					List<LinearRing2D> fixed = GeomUtils.fix((LinearRing2D)osmPolyline.getPolyline());
					for (LinearRing2D linearRing2D : fixed) {
						writer.write(outputFormat.getPolygonString(linearRing2D, forestIndexAndDensity[0] + "", forestIndexAndDensity[1] + ""));
					}
				} else {
					writeForestToDsf((OsmPolygon) osmPolyline, forestIndexAndDensity);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void translationComplete() {
		// Do nothing
	}
	
	/**
	 * @param polygon
	 *            the forest polygon
	 * @param forestIndexAndDensity
	 *            index and density of the forest rule
	 */
	private void writeForestToDsf(OsmPolygon osmPolygon, Integer[] forestIndexAndDensity) {
	
		writer.write(outputFormat.getPolygonString(osmPolygon, forestIndexAndDensity[0] + "", forestIndexAndDensity[1] + ""));
			
	}
	
	@Override
	public String getId() {
		return "forest";
	}

	@Override
	public boolean isTerminating() {
		return false;
	}
}
