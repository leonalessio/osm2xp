package com.osm2xp.translators;

import java.io.File;

import math.geom2d.Point2D;

public interface ITranslatorFactory {

	public ITranslator getTranslator(File currentFile,
			Point2D currentTile, String folderPath);
	
	public String getOutputMode();
	
}
