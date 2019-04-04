package com.osm2xp.translators.xplane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.osm2xp.model.osm.polygon.OsmPolygon;
import com.osm2xp.model.osm.polygon.OsmPolyline;
import com.osm2xp.utils.DsfObjectsProvider;
import com.osm2xp.utils.geometry.GeomUtils;
import com.osm2xp.generation.options.Polygon;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.generation.options.XplaneOptions;
import com.osm2xp.generation.options.rules.PolygonRulesList;
import com.osm2xp.generation.options.rules.PolygonTagsRule;
import com.osm2xp.writers.IWriter;

import math.geom2d.polygon.LinearRing2D;

public class XPDrapedPolyTranslator extends XPWritingTranslator {

	private DsfObjectsProvider dsfObjectsProvider;
	private XPOutputFormat outputFormat;

	public XPDrapedPolyTranslator(IWriter writer, DsfObjectsProvider dsfObjectsProvider, XPOutputFormat outputFormat) {
		super(writer);
		this.dsfObjectsProvider = dsfObjectsProvider;
		this.outputFormat = outputFormat;
	}

	@Override
	public boolean handlePoly(OsmPolyline osmPolyline) {
		XplaneOptions options = XPlaneOptionsProvider.getOptions();
		if (osmPolyline instanceof OsmPolygon && options.isGeneratePolys()) {
			PolygonRulesList polygonRules = options.getPolygonRules();
			if (GeomUtils.computeEdgesLength(osmPolyline.getPolyline()) > options.getPolygonRules().getMinPerimeter()) {
				List<PolygonTagsRule> matchingRules = getMatchingRules(osmPolyline, polygonRules);
				for (PolygonTagsRule matchingRule : matchingRules) {
					List<Polygon> polygons = matchingRule.getPolygons();
					Random rnd = new Random();
					int i = rnd.nextInt(polygons.size());
					Polygon polygon = polygons.get(i);				 
					int idx = dsfObjectsProvider.getStringIndex(polygon.getPath());
					if (!osmPolyline.isValid()) {
						List<LinearRing2D> fixed = GeomUtils.fix((LinearRing2D)osmPolyline.getPolyline());
						for (LinearRing2D linearRing2D : fixed) {
							writer.write(outputFormat.getPolygonString(linearRing2D, idx + "", "0"));
						}
					} else {
						writer.write(outputFormat.getPolygonString(osmPolyline.getPolyline(), idx + "", "0"));
					}
				}
				return matchingRules.size() > 0;
			}			
		}
		return false;
	}

	private List<PolygonTagsRule> getMatchingRules(OsmPolyline osmPolyline, PolygonRulesList polygonRules) {
		List<PolygonTagsRule> resList = new ArrayList<PolygonTagsRule>();
		List<PolygonTagsRule> rules = polygonRules.getRules();
		for (PolygonTagsRule polygonTagsRule : rules) {
			if (osmPolyline.hasTag(polygonTagsRule.getTag())) {
				resList.add(polygonTagsRule);
			}
		}
		return resList;
	}

	@Override
	public void translationComplete() {
		// Do nothing
	}
	
	@Override
	public String getId() {
		return "draped-polygon";
	}
	
	@Override
	public boolean isTerminating() {
		return false;
	}

}
