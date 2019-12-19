package com.osm2xp.translators.xplane;

import com.osm2xp.generation.osm.OsmConstants;
import com.osm2xp.generation.xplane.resources.DsfObjectsProvider;
import com.osm2xp.model.osm.polygon.OsmPolygon;
import com.osm2xp.utils.geometry.GeomUtils;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.writers.IWriter;

public class XPCoolingTowerTranslator extends XPSpecObjectTranslator {
	
	private static final int MIN_TOWER_DIAMETER = 30;

	public XPCoolingTowerTranslator(IWriter writer, DsfObjectsProvider objectsProvider) {
		super(writer, objectsProvider);
	}

	@Override
	public void translationComplete() {
		// Do nothing
		
	}

	@Override
	protected boolean canProcess(OsmPolygon osmPolygon) {
		return XPlaneOptionsProvider.getOptions().isGenerateCoolingTowers() &&
				("cooling_tower".equalsIgnoreCase(osmPolygon.getTagValue(OsmConstants.MAN_MADE_TAG))
				|| "cooling".equalsIgnoreCase(osmPolygon.getTagValue("tower:type")));
	}

	@Override
	protected int getObjectSize(OsmPolygon osmPolygon) {
		double length = GeomUtils.computeEdgesLength(osmPolygon.getPolygon());
		int diameter = (int) Math.round(length / Math.PI);
		if (diameter < MIN_TOWER_DIAMETER) {
			return -1;
		}
		return diameter;
	}

	@Override
	protected String getObjectFilePreffix() {
		return "cooling_tower";
	}

	@Override
	protected boolean generationEnabled() {
		return XPlaneOptionsProvider.getOptions().isGenerateCoolingTowers();
	}
	
	protected String getComment(OsmPolygon osmPolygon) {
		StringBuilder commentBuilder = new StringBuilder("#Cooling tower");	
		commentBuilder.append(", way " + osmPolygon.getId());
		commentBuilder.append(System.getProperty("line.separator"));
		return commentBuilder.toString();
	}

	@Override
	public String getId() {
		return "cooling_tower";
	}
}
