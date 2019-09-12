package com.osm2xp.translators.xplane;

import com.osm2xp.generation.options.XPlaneOptionsProvider;

public class XP10OutputFormat extends XPOutputFormat {
	
	public XP10OutputFormat(int objectRenderLevel, int facadeRenderLevel) {
		super(objectRenderLevel, facadeRenderLevel);
	}

	@Override
	protected String getDsfExclusions(String tileCoordinate) {
		StringBuilder builder = new StringBuilder(super.getDsfExclusions(tileCoordinate));
		if (XPlaneOptionsProvider.getOptions().isExcludePol()) {
			builder.append("PROPERTY sim/exclude_pol " + tileCoordinate);
		}

		if (XPlaneOptionsProvider.getOptions().isExcludeBch()) {
			builder.append("PROPERTY sim/exclude_bch " + tileCoordinate);
		}
		return builder.toString();
	}

}
