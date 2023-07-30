package com.osm2xp.translators.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.datastore.IDataSink;
import com.osm2xp.translators.ISpecificTranslator;
import com.osm2xp.translators.TranslatorBuilder;

import math.geom2d.Point2D;

public class TranslationAdapterFactory {
	
	private Map<Point2D, ISpecificTranslator> tileTranslationAdapters = new HashMap<Point2D, ISpecificTranslator>();
	private IDataSink processor;
	private String folderPath;
	private File binaryFile;
	private String outputFormat;
	
	public TranslationAdapterFactory(File binaryFile, IDataSink processor, String folderPath, String outputFormat) {
		super();
		this.binaryFile = binaryFile;
		this.processor = processor;
		this.folderPath = folderPath;
		this.outputFormat = outputFormat;
	}
	
	public void pointParsed(double lonf, double latf) {
		Point2D cleanedLoc = new Point2D((int) Math.floor(lonf), (int) Math.floor(latf));
		if (!tileTranslationAdapters.containsKey(cleanedLoc)) {
			Osm2xpLogger.info("Detected tile (" + cleanedLoc.x() + ", " + cleanedLoc.y() + ")");
			TileTranslationAdapter adapter = new TileTranslationAdapter(cleanedLoc, processor, TranslatorBuilder.getTranslator(binaryFile, cleanedLoc, folderPath, outputFormat));
			adapter.init();
			tileTranslationAdapters.put(cleanedLoc, adapter);
		}
	}
	
	public List<ISpecificTranslator> getAvailableAdapters() {
		return new ArrayList<>(tileTranslationAdapters.values());
	}

}
