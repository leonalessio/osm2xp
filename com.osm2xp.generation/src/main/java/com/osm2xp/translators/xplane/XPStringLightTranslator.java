package com.osm2xp.translators.xplane;

import java.util.ArrayList;
import java.util.List;

import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.generation.options.XplaneOptions;
import com.osm2xp.generation.xplane.resources.DsfObjectsProvider;
import com.osm2xp.generation.xplane.resources.XPOutputFormat;
import com.osm2xp.utils.geometry.GeomUtils;
import com.osm2xp.writers.IWriter;

import math.geom2d.polygon.LinearCurve2D;

public class XPStringLightTranslator implements IXPLightTranslator{
	
	private DsfObjectsProvider objectsProvider;
	private XPOutputFormat outputFormat;
	private IWriter writer;

	public XPStringLightTranslator(IWriter writer, DsfObjectsProvider objectsProvider, XPOutputFormat outputFormat) {
		super();
		this.writer = writer;
		this.objectsProvider = objectsProvider;
		this.outputFormat = outputFormat;
	}

	@Override
	public void writeLightStrings(LinearCurve2D baseLine, double distance, boolean doubleSided) {
		XplaneOptions options = XPlaneOptionsProvider.getOptions();
		double length = GeomUtils.computeEdgesLength(baseLine);
		int interval = options.getStreetLightsInterval();
		if (length > interval) { //Ignore length, which is smaller than lights interval
			List<LinearCurve2D> lightStrings = getLightStrings(baseLine, distance, doubleSided);
			for (LinearCurve2D lightStr : lightStrings) {
				writer.write(outputFormat.getPolygonString(lightStr, objectsProvider.getStringIndex(options.getLightObjectString()) + "", interval + ""));
			}
		}
	}
	
	@SuppressWarnings("restriction")
	protected List<LinearCurve2D> getLightStrings(LinearCurve2D baseLine, double distance, boolean doubleSided) {
		//TODO cut ends here to avoid lights on crossings
		List<LinearCurve2D> resList = new ArrayList<>();
		LinearCurve2D localCurve = GeomUtils.linearCurve2DToLocal(baseLine, baseLine.vertex(0));
		double coordsDist = distance / GeomUtils.LATITUDE_TO_M;
		resList.add(GeomUtils.localToLinearCurve2D(localCurve.parallel(-coordsDist), baseLine.vertex(0)));
		if (doubleSided) {
			LinearCurve2D curve = GeomUtils.localToLinearCurve2D(localCurve.parallel(coordsDist), baseLine.vertex(0));
			curve = (LinearCurve2D) curve.reverse(); //We need this to make the lampposts directed towards the road center
			resList.add(curve);
		}
		return resList;
	}
	

}
