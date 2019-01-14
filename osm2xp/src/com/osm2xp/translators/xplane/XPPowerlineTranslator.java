package com.osm2xp.translators.xplane;

import com.osm2xp.core.model.osm.IHasTags;
import com.osm2xp.model.osm.polygon.OsmPolyline;
import com.osm2xp.translators.impl.XPOutputFormat;
import com.osm2xp.utils.helpers.XplaneOptionsHelper;
import com.osm2xp.writers.IWriter;

public class XPPowerlineTranslator extends XPPathTranslator {

	public XPPowerlineTranslator(IWriter writer, XPOutputFormat outputFormat, IDRenumbererService idProvider) {
		super(writer, outputFormat, idProvider);
	}

	@Override
	public boolean handlePoly(OsmPolyline osmPolyline) {
		if (!XplaneOptionsHelper.getOptions().isGeneratePowerlines()) {
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
	protected String getComment(IHasTags poly) {
		return "power line";
	}

	@Override
	protected boolean isBridge(IHasTags poly) {
		return false; //Not supported for power lines
	}
	
	@Override
	protected int getBridgeRampLength() {
		return 0;  //Not supported for power lines
	}
}
