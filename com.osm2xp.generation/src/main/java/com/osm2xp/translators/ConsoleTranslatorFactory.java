package com.osm2xp.translators;

import java.io.File;

import com.osm2xp.translators.impl.ConsoleTranslatorImpl;

import math.geom2d.Point2D;

public class ConsoleTranslatorFactory implements ITranslatorFactory {

	@Override
	public ITranslator getTranslator(File currentFile, Point2D currentTile, String folderPath) {
		return new ConsoleTranslatorImpl(currentTile);
	}

	@Override
	public String getOutputMode() {
		return "CONSOLE";
	}

}
