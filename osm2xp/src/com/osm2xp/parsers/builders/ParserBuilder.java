package com.osm2xp.parsers.builders;

import java.awt.Color;
import java.io.File;
import java.util.Map;

import com.osm2xp.converters.impl.AbstractTranslatingConverter;
import com.osm2xp.converters.impl.GeneralTranslatingConverter;
import com.osm2xp.converters.impl.MultiTileDataConverter;
import com.osm2xp.converters.impl.SpecificTranslatingConverter;
import com.osm2xp.core.exceptions.DataSinkException;
import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.parsers.IOSMDataVisitor;
import com.osm2xp.core.parsers.IParser;
import com.osm2xp.core.parsers.IVisitingParser;
import com.osm2xp.core.parsers.impl.SaxParserImpl;
import com.osm2xp.core.parsers.impl.ShapefileParserImpl;
import com.osm2xp.core.parsers.impl.TranslatingBinaryParser;
import com.osm2xp.dataProcessors.DataSinkFactory;
import com.osm2xp.dataProcessors.IDataSink;
import com.osm2xp.gui.Activator;
import com.osm2xp.translators.ITranslator;
import com.osm2xp.translators.TranslatorBuilder;
import com.osm2xp.translators.airfield.XPAirfieldTranslationAdapter;
import com.osm2xp.utils.FilesUtils;
import com.osm2xp.utils.helpers.GuiOptionsHelper;

import math.geom2d.Point2D;

/**
 * ParserBuilder.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class ParserBuilder {

	/**
	 * Build the parser implementation for the type of file
	 * 
	 * @param currentTile tile to create parser for
	 * @param folderPath
	 * @return
	 * @throws Osm2xpBusinessException
	 * @throws DataSinkException
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public static IParser getParser(Point2D currentTile, File currentFile,
			String folderPath)
			throws DataSinkException {
		ITranslator translator = TranslatorBuilder.getTranslator(currentFile,
				currentTile, folderPath);
		// if a roof color file is available, load it into a map and give it to
		// the parser
		Map<Long, Color> roofsColorMap = null;
		if (GuiOptionsHelper.getRoofColorFile() != null) {
			roofsColorMap = FilesUtils.loadG2xplColorFile(GuiOptionsHelper
					.getRoofColorFile());
		}
		IDataSink processor = DataSinkFactory.getProcessor();
		AbstractTranslatingConverter converter = new GeneralTranslatingConverter(translator, processor, roofsColorMap);
		return getParser(currentFile, converter);
	}

	private static IParser getParser(File currentFile, IOSMDataVisitor converter) {
		// PBF FILE
		if (currentFile.getName().toLowerCase().endsWith(".pbf")) {
			return new TranslatingBinaryParser(currentFile, converter);
		}
		// OSM FILE
		else if (currentFile.getName().toLowerCase().endsWith(".osm")) {
			return new SaxParserImpl(currentFile, converter);
		}
		// SHP FILE
		else if (currentFile.getName().toLowerCase().endsWith(".shp")) {
			return new ShapefileParserImpl(currentFile, converter, GuiOptionsHelper.getShapefileTag());
		}
		return null;
	}
	
	public static IParser getXPAirfieldGeneratingParser(File currentFile,
			String folderPath) {
		try {
			return getParser(currentFile, new SpecificTranslatingConverter(new XPAirfieldTranslationAdapter(folderPath), DataSinkFactory.getProcessor(), null));
		} catch (DataSinkException e) {
			Activator.log(e);
		}
		return null;
	}
	
	
	/**
	 * Build the parser implementation for the type of file
	 * 
	 * @param folderPath
	 * @param relationsList
	 * 
	 * @param tiles Tiles list
	 * @return
	 * @throws Osm2xpBusinessException
	 * @throws DataSinkException
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public static IVisitingParser getMultiTileParser(File currentFile,
			String folderPath)
			throws DataSinkException {
		IDataSink processor = DataSinkFactory.getProcessor();
		// if a roof color file is available, load it into a map and give it to
		// the parser
		Map<Long, Color> roofsColorMap = null;
		if (GuiOptionsHelper.getRoofColorFile() != null) {
			roofsColorMap = FilesUtils.loadG2xplColorFile(GuiOptionsHelper
					.getRoofColorFile());
		}
		MultiTileDataConverter converter = new MultiTileDataConverter(processor, currentFile, folderPath, roofsColorMap);		
		return (IVisitingParser) getParser(currentFile, converter);
	}

}
