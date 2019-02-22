package com.osm2xp.translators;

import java.io.File;

import com.osm2xp.translators.impl.FsxBgTranslatorImpl;
import com.osm2xp.writers.IWriter;
import com.osm2xp.writers.impl.BglWriterImpl;

import math.geom2d.Point2D;

public class FSXTranslatorFactory implements ITranslatorFactory {

	@Override
	public ITranslator getTranslator(File currentFile, Point2D currentTile, String folderPath) {
		IWriter writer = new BglWriterImpl(folderPath);
		return new FsxBgTranslatorImpl(writer, currentTile, folderPath);
	}

	@Override
	public String getOutputType() {
		return "FSX";
	}

}
