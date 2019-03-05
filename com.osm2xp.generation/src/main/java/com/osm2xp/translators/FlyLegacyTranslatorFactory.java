package com.osm2xp.translators;

import java.io.File;

import com.osm2xp.translators.impl.FlyLegacyTranslatorImpl;

import math.geom2d.Point2D;

public class FlyLegacyTranslatorFactory implements ITileTranslatorFactory {

	@Override
	public ITranslator getTranslator(File currentFile, Point2D currentTile, String folderPath) {
		return new FlyLegacyTranslatorImpl(currentTile, folderPath);
	}

	@Override
	public String getOutputMode() {
		return "FLY_LEGACY";
	}
	
	@Override
	public boolean isFileWriting() {
		return true;
	}

}
