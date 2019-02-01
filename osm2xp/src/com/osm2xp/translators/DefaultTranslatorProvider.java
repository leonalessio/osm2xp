package com.osm2xp.translators;

import java.io.File;

import math.geom2d.Point2D;

public class DefaultTranslatorProvider extends AbstractTranslatorProvider{

	public DefaultTranslatorProvider(File binaryFile, String folderPath) {
		super(binaryFile, folderPath);
	}

	@Override
	public ITranslator getTranslator(Point2D currentTile) {
		return TranslatorBuilder.getTranslator(binaryFile, currentTile, folderPath);
	}

}
