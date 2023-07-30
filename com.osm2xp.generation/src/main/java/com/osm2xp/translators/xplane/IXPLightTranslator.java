package com.osm2xp.translators.xplane;

import math.geom2d.polygon.LinearCurve2D;

public interface IXPLightTranslator {
	
	void writeLightStrings(LinearCurve2D baseLine, double distance, boolean doubleSided);

}
