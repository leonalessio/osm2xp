package com.osm2xp.translators;

import java.io.File;

import math.geom2d.Point2D;

public interface ITileTranslatorFactory extends ITranslatorFactory{

	public ITranslator getTranslator(File currentFile,
			Point2D currentTile, String folderPath);
	
}
