package com.osm2xp.translators;

import java.io.File;

import com.osm2xp.translators.impl.G2xplTranslatorImpl;

import math.geom2d.Point2D;

public class G2XPLTranslatorFactory implements ITileTranslatorFactory {

	@Override
	public ITranslator getTranslator(File currentFile, Point2D currentTile, String folderPath) {
		return new G2xplTranslatorImpl(currentTile, folderPath);
	}

	@Override
	public String getOutputMode() {
		return "G2XPL";
	}

	@Override
	public boolean isFileWriting() {
		return true;
	}

}
