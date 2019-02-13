package com.osm2xp.translators.xplane;

import com.osm2xp.core.model.osm.IHasTags;
import com.osm2xp.model.osm.polygon.OsmPolyline;
import com.osm2xp.translators.impl.XPOutputFormat;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.writers.IWriter;

public class XPRailTranslator extends XPPathTranslator {

	public XPRailTranslator(IWriter writer, XPOutputFormat outputFormat, IDRenumbererService idProvider) {
		super(writer, outputFormat, idProvider);
	}

	@Override
	public boolean handlePoly(OsmPolyline osmPolyline) {
		if (!XPlaneOptionsProvider.getOptions().isGenerateRailways()) {
			return false;
		}
		if ("rail".equals(osmPolyline.getTagValue("railway"))) {
			addSegmentsFrom(osmPolyline);
			return true; 
		}
		return false;
	}

	@Override
	protected int getPathType(IHasTags polygon) {
		return 151; //TODO using only one type for now
	}
	
	@Override
	protected int getBridgeRampLength() {
		return XPlaneOptionsProvider.getOptions().getRailBridgeRampLen();
	}
	
	@Override
	public String getId() {
		return "railway";
	}
}
