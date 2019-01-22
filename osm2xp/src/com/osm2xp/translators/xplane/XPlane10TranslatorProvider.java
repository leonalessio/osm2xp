package com.osm2xp.translators.xplane;

import java.io.File;

import com.osm2xp.translators.impl.XPlaneTranslatorImpl;
import com.osm2xp.translators.impl.Xplane10TranslatorImpl;
import com.osm2xp.writers.IHeaderedWriter;

import math.geom2d.Point2D;

public class XPlane10TranslatorProvider extends XPlaneTranslatorProvider {

	public XPlane10TranslatorProvider(File binaryFile, String folderPath) {
		super(binaryFile, folderPath);
	}

	@Override
	protected XPlaneTranslatorImpl createTranslator(Point2D currentTile, IHeaderedWriter writer) {
		return new Xplane10TranslatorImpl(writer, currentTile, folderPath, dsfObjectsProvider);
	}


}
