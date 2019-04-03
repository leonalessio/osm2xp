package com.osm2xp.translators.xplane;

import com.osm2xp.core.model.osm.IHasTags;
import com.osm2xp.model.osm.polygon.OsmPolyline;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.writers.IWriter;

public class XPPowerlineTranslator extends XPPathTranslator {

	public XPPowerlineTranslator(IWriter writer, IDRenumbererService idProvider, XPOutputFormat outputFormat) {
		super(writer, outputFormat, idProvider);
	}

	@Override
	public boolean handlePoly(OsmPolyline osmPolyline) {
		if (!XPlaneOptionsProvider.getOptions().isGeneratePowerlines()) {
			return false;
		}
		if ("line".equals(osmPolyline.getTagValue("power"))) {
			addSegmentsFrom(osmPolyline);
			return true; 
		}
		return false;
	}

	@Override
	protected int getPathType(IHasTags polygon) {
		return 220; //TODO using only one type for now
	}
	
	@Override
	protected boolean isBridge(IHasTags poly) {
		return false; //Not supported for power lines
	}
	
	@Override
	protected int getBridgeRampLength() {
		return 0;  //Not supported for power lines
	}
	
	@Override
	public String getId() {
		return "powerline";
	}
}
