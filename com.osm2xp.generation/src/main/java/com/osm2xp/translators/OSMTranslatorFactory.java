package com.osm2xp.translators;

import java.io.File;

import com.osm2xp.translators.impl.OsmTranslatorImpl;
import com.osm2xp.writers.IWriter;
import com.osm2xp.writers.impl.OsmWriterImpl;

import math.geom2d.Point2D;

public class OSMTranslatorFactory implements ITileTranslatorFactory {

	@Override
	public ITranslator getTranslator(File currentFile, Point2D currentTile, String folderPath) {
		IWriter writer = new OsmWriterImpl(folderPath);
		return new OsmTranslatorImpl(writer, currentTile);
	}

	@Override
	public String getOutputMode() {
		return "OSM";
	}
	
	@Override
	public boolean isFileWriting() {
		return true;
	}

}
