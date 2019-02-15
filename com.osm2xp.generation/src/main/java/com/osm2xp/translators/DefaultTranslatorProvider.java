package com.osm2xp.translators;

import java.io.File;

import math.geom2d.Point2D;

public class DefaultTranslatorProvider extends AbstractTranslatorProvider{

	private String outputFomat;

	public DefaultTranslatorProvider(File binaryFile, String folderPath, String outputFomat) {
		super(binaryFile, folderPath);
		this.outputFomat = outputFomat;
	}

	@Override
	public ITranslator getTranslator(Point2D currentTile) {
		return TranslatorBuilder.getTranslator(binaryFile, currentTile, folderPath, outputFomat);
	}

}
