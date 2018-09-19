package com.osm2xp.parsers.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.osm2xp.dataProcessors.IDataSink;
import com.osm2xp.translators.TranslatorBuilder;
import com.osm2xp.utils.logging.Osm2xpLogger;

import math.geom2d.Point2D;

public class TranslationAdapterFactory {
	
	private Map<Point2D, TileTranslationAdapter> tileTranslationAdapters = new HashMap<Point2D, TileTranslationAdapter>();
	private IDataSink processor;
	private String folderPath;
	private File binaryFile;
	
	public TranslationAdapterFactory(File binaryFile, IDataSink processor, String folderPath) {
		super();
		this.binaryFile = binaryFile;
		this.processor = processor;
		this.folderPath = folderPath;
	}
	
	public void pointParsed(double lonf, double latf) {
		Point2D cleanedLoc = new Point2D((int) Math.floor(lonf), (int) Math.floor(latf));
		if (!tileTranslationAdapters.containsKey(cleanedLoc)) {
			Osm2xpLogger.info("Detected tile (" + cleanedLoc.x + ", " + cleanedLoc.y + ")");
			TileTranslationAdapter adapter = new TileTranslationAdapter(cleanedLoc, processor, TranslatorBuilder.getTranslator(binaryFile, cleanedLoc, folderPath));
			adapter.init();
			tileTranslationAdapters.put(cleanedLoc, adapter);
		}
	}
	
	public List<TileTranslationAdapter> getAvailableAdapters() {
		return new ArrayList<>(tileTranslationAdapters.values());
	}

}
