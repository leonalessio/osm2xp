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
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.parsers.IOSMDataVisitor;
import com.osm2xp.core.parsers.IParser;
import com.osm2xp.core.parsers.IVisitingParser;
import com.osm2xp.core.parsers.impl.SaxParserImpl;
import com.osm2xp.core.parsers.impl.ShapefileParserImpl;
import com.osm2xp.core.parsers.impl.TranslatingBinaryParser;
import com.osm2xp.datastore.DataSinkFactory;
import com.osm2xp.datastore.IDataSink;
import com.osm2xp.generation.options.GlobalOptionsProvider;
import com.osm2xp.generation.paths.PathsService;
import com.osm2xp.translators.ITranslator;
import com.osm2xp.translators.ITranslatorProvider;
import com.osm2xp.translators.airfield.XPAirfieldTranslationAdapter;
import com.osm2xp.utils.FilesUtils;

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
	 * @param outputFormat 
	 * @return
	 * @throws Osm2xpBusinessException
	 * @throws DataSinkException
	 * @throws NumberFormatException
	 * @throws Exception
	 */
//	public static IParser getParser(Point2D currentTile, File currentFile,
//			String folderPath, String outputFormat)
//			throws DataSinkException {
//		ITranslator translator = TranslatorBuilder.getTranslator(currentFile,
//				currentTile, folderPath, outputFormat);
//		// if a roof color file is available, load it into a map and give it to
//		// the parser
//		Map<Long, Color> roofsColorMap = null;
//		if (PathsService.getPathsProvider().getRoofColorFile() != null) {
//			roofsColorMap = FilesUtils.loadG2xplColorFile(PathsService.getPathsProvider()
//					.getRoofColorFile());
//		}
//		IDataSink dataSink = DataSinkFactory.getProcessor();
//		AbstractTranslatingConverter converter = new GeneralTranslatingConverter(translator, dataSink, roofsColorMap);
//		return getParser(currentFile, converter);
//	}
	public static IParser getParser(File currentFile,
			ITranslator translator, IDataSink dataSink)
					throws DataSinkException {
		// if a roof color file is available, load it into a map and give it to
		// the parser
		Map<Long, Color> roofsColorMap = null;
		if (PathsService.getPathsProvider().getRoofColorFile() != null) {
			roofsColorMap = FilesUtils.loadG2xplColorFile(PathsService.getPathsProvider()
					.getRoofColorFile());
		}
		AbstractTranslatingConverter converter = new GeneralTranslatingConverter(translator, dataSink, roofsColorMap);
		return getParser(currentFile, converter);
	}

	public static IParser getParser(File currentFile, IOSMDataVisitor converter) {
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
			return new ShapefileParserImpl(currentFile, converter, GlobalOptionsProvider.getShapefileTag());
		}
		return null;
	}
	
	public static IParser getXPAirfieldGeneratingParser(File currentFile,
			String folderPath) {
		try {
			return getParser(currentFile, new SpecificTranslatingConverter(new XPAirfieldTranslationAdapter(folderPath), DataSinkFactory.getProcessor(), null));
		} catch (DataSinkException e) {
			Osm2xpLogger.log(e);
		}
		return null;
	}
	
	
	/**
	 * Build the parser implementation for the type of file
	 * @param dataSink 
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
	public static IVisitingParser getMultiTileParser(File currentFile,ITranslatorProvider translatorProvider, IDataSink dataSink)
			throws DataSinkException {
		// if a roof color file is available, load it into a map and give it to
		// the parser
		Map<Long, Color> roofsColorMap = null;
		if (PathsService.getPathsProvider().getRoofColorFile() != null) {
			roofsColorMap = FilesUtils.loadG2xplColorFile(PathsService.getPathsProvider()
					.getRoofColorFile());
		}
		MultiTileDataConverter converter = new MultiTileDataConverter(dataSink, translatorProvider, roofsColorMap);		
		return (IVisitingParser) getParser(currentFile, converter);
	}

	public static IVisitingParser getPreprocessParser(File currentFile, ITranslatorProvider translatorProvider, IDataSink dataSink) {
		return null;
	}

}
