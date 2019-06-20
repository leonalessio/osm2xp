package com.osm2xp.translators.xplane;

import com.osm2xp.generation.osm.OsmConstants;
import com.osm2xp.model.osm.polygon.OsmPolygon;
import com.osm2xp.utils.DsfObjectsProvider;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.writers.IWriter;

public class XPChimneyTranslator extends XPSpecObjectTranslator {
	
	public XPChimneyTranslator(IWriter writer, DsfObjectsProvider objectsProvider) {
		super(writer, objectsProvider);
	}

	@Override
	public void translationComplete() {
		// Do nothing
		
	}

	@Override
	protected boolean canProcess(OsmPolygon osmPolygon) {
		return XPlaneOptionsProvider.getOptions().isGenerateChimneys() &&
				"chimney".equalsIgnoreCase(osmPolygon.getTagValue(OsmConstants.MAN_MADE_TAG));
	}

	@Override
	protected int getObjectSize(OsmPolygon osmPolygon) {
		int height = osmPolygon.getHeight();
		if (height == 0) {
			return 50; //Default value
		}
		return height;
	}

	@Override
	protected String getObjectFilePreffix() {
		return "chimney";
	}
	
	@Override
	protected boolean generationEnabled() {
		return XPlaneOptionsProvider.getOptions().isGenerateChimneys();
	}
	
	protected String getComment(OsmPolygon osmPolygon) {
		StringBuilder commentBuilder = new StringBuilder("#Chimney");
		if (osmPolygon.getHeight() > 0) {
			commentBuilder.append(" ");
			commentBuilder.append(osmPolygon.getHeight());
			commentBuilder.append("m");
		}
		commentBuilder.append(System.getProperty("line.separator"));
		return commentBuilder.toString();
	}

	@Override
	public String getId() {
		return "chimney";
	}
	
}
