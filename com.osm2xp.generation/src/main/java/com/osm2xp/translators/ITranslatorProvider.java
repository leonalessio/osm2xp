package com.osm2xp.translators;

import java.util.Collection;

import math.geom2d.Point2D;

public interface ITranslatorProvider {
	
	public Collection<ISpecificTranslator> createAdditinalAdapters();
	
	public ITranslator getTranslator(Point2D currentTile);
	
}
