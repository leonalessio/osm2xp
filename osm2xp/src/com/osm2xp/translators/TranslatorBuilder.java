package com.osm2xp.translators;

import java.io.File;
import java.util.Collection;
import java.util.Collections;

import com.osm2xp.constants.Perspectives;
import com.osm2xp.model.stats.GenerationStats;
import com.osm2xp.translators.airfield.XPAirfieldTranslationAdapter;
import com.osm2xp.translators.impl.ConsoleTranslatorImpl;
import com.osm2xp.translators.impl.FlightGearTranslatorImpl;
import com.osm2xp.translators.impl.FlyLegacyTranslatorImpl;
import com.osm2xp.translators.impl.FsxBgTranslatorImpl;
import com.osm2xp.translators.impl.G2xplTranslatorImpl;
import com.osm2xp.translators.impl.OsmTranslatorImpl;
import com.osm2xp.translators.impl.WavefrontTranslatorImpl;
import com.osm2xp.translators.xplane.XPlane10TranslatorProvider;
import com.osm2xp.translators.xplane.XPlane9TranslatorProvider;
import com.osm2xp.utils.helpers.GuiOptionsHelper;
import com.osm2xp.utils.helpers.StatsHelper;
import com.osm2xp.utils.helpers.WavefrontOptionsHelper;
import com.osm2xp.writers.IWriter;
import com.osm2xp.writers.impl.BglWriterImpl;
import com.osm2xp.writers.impl.OsmWriterImpl;

import math.geom2d.Point2D;

/**
 * TranslatorBuilder.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class TranslatorBuilder {

	public static ITranslator getTranslator(File currentFile,
			Point2D currentTile, String folderPath) {
		ITranslator result = null;
		// OSM implementation
		if (GuiOptionsHelper.getOptions().getOutputFormat()
				.equals(Perspectives.PERSPECTIVE_OSM)) {
			result = buildOsmTranslator(currentTile, folderPath);
		}
		// DEBUG CONSOLE
		else if (GuiOptionsHelper.getOptions().getOutputFormat()
				.equals(Perspectives.PERSPECTIVE_CONSOLE)) {
			result = buildConsoleTranslator(currentTile);
		}

		// WAVEFRONT OBJECT
		else if (GuiOptionsHelper.getOptions().getOutputFormat()
				.equals(Perspectives.PERSPECTIVE_WAVEFRONT)) {
			result = buildWavefrontTranslator(currentTile, folderPath);
		}

		// FSX TRANSLATOR
		else if (GuiOptionsHelper.getOptions().getOutputFormat()
				.equals(Perspectives.PERSPECTIVE_FSX)) {
			result = buildFsxTranslator(currentFile, currentTile, folderPath);
		}

		// G2XPL TRANSLATOR
		else if (GuiOptionsHelper.getOptions().getOutputFormat()
				.equals(Perspectives.PERSPECTIVE_G2XPL)) {
			result = buildG2xplTranslator(currentTile, folderPath);
		}

		// FLY! LEGACY TRANSLATOR
		else if (GuiOptionsHelper.getOptions().getOutputFormat()
				.equals(Perspectives.PERSPECTIVE_FLY_LEGACY)) {
			result = buildFlyLegacyTranslator(currentTile, folderPath);
		}

		// FLIGHTGEAR TRANSLATOR
		else if (GuiOptionsHelper.getOptions().getOutputFormat()
				.equals(Perspectives.PERSPECTIVE_FLIGHT_GEAR)) {
			result = buildFlightGearTranslator(currentTile, folderPath);
		}

		return result;
	}

	/**
	 * @param currentFile
	 * @param currentTile
	 * @param folderPath
	 * @param processor
	 * @return
	 */
	private static ITranslator buildFsxTranslator(File currentFile,
			Point2D currentTile, String folderPath) {
		IWriter writer = new BglWriterImpl(folderPath);
		GenerationStats stats = StatsHelper.initStats(currentFile, currentTile);
		return new FsxBgTranslatorImpl(stats, writer, currentTile, folderPath);
	}

	/**
	 * @param currentTile
	 * @param folderPath
	 * @param processor
	 * @return
	 */
	private static ITranslator buildWavefrontTranslator(Point2D currentTile,
			String folderPath) {
		return new WavefrontTranslatorImpl(folderPath, currentTile,
				WavefrontOptionsHelper.getOptions()
						.isWaveFrontExportSingleObject());
	}

	/**
	 * @param currentTile
	 * @param processor
	 * @return
	 */
	private static ITranslator buildConsoleTranslator(Point2D currentTile) {
		return new ConsoleTranslatorImpl(currentTile);
	}

	/**
	 * @param currentTile
	 * @param folderPath
	 * @param processor
	 * @return
	 */
	private static ITranslator buildOsmTranslator(Point2D currentTile,
			String folderPath) {
		IWriter writer = new OsmWriterImpl(folderPath);
		return new OsmTranslatorImpl(writer, currentTile);
	}

	/**
	 * @param currentTile
	 * @param folderPath
	 * @param processor
	 * @return
	 */
	private static ITranslator buildG2xplTranslator(Point2D currentTile,
			String folderPath) {
		return new G2xplTranslatorImpl(currentTile, folderPath);
	}

	/**
	 * @param currentTile
	 * @param folderPath
	 * @return
	 */
	private static ITranslator buildFlyLegacyTranslator(Point2D currentTile,
			String folderPath) {
		return new FlyLegacyTranslatorImpl(currentTile, folderPath);
	}

	/**
	 * @param currentTile
	 * @param folderPath
	 * @return
	 */
	private static ITranslator buildFlightGearTranslator(Point2D currentTile,
			String folderPath) {
		return new FlightGearTranslatorImpl(currentTile, folderPath);
	}

//	/**
//	 * @param currentFile
//	 * @param currentTile
//	 * @param folderPath
//	 * @param processor
//	 * @return
//	 * @throws Osm2xpBusinessException
//	 */
//	private static ITranslator buildXplane10Translator(File currentFile,
//			Point2D currentTile, String folderPath) {
//
//		String facadeSetsStr = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID).get(FacadeSetManager.FACADE_SETS_PROP,FacadeSetHelper.getDefaultFacadePath());
//		DsfObjectsProvider dsfObjectsProvider = new DsfObjectsProvider(folderPath, FacadeSetManager.getManager(facadeSetsStr, new File(folderPath)));
//		IHeaderedWriter writer = new DsfWriterImpl(folderPath, currentTile, dsfObjectsProvider);
//		Xplane10TranslatorImpl translatorImpl = new Xplane10TranslatorImpl(writer, currentTile,
//				folderPath, dsfObjectsProvider);
//		if (XplaneOptionsHelper.getOptions().isGenerateDebugImg()) {
//			translatorImpl.setTranslationListener(new ImageDebugTranslationListener());
//		}
//		return translatorImpl;
//	}
//
//	/**
//	 * @param currentFile
//	 * @param currentTile
//	 * @param folderPath
//	 * @param processor
//	 * @return
//	 * @throws Osm2xpBusinessException
//	 */
//	private static ITranslator buildXplane9Translator(File currentFile,
//			Point2D currentTile, String folderPath) {
//
//		String facadeSetsStr = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID).get(FacadeSetManager.FACADE_SETS_PROP,FacadeSetHelper.getDefaultFacadePath());
//		DsfObjectsProvider dsfObjectsProvider = new DsfObjectsProvider(folderPath, FacadeSetManager.getManager(facadeSetsStr, new File(folderPath)));
//		IHeaderedWriter writer = new DsfWriterImpl(folderPath, currentTile, dsfObjectsProvider);
//		Xplane9TranslatorImpl xplane9TranslatorImpl = new Xplane9TranslatorImpl(writer, currentTile,
//				folderPath, dsfObjectsProvider);
//		if (XplaneOptionsHelper.getOptions().isGenerateDebugImg()) {
//			xplane9TranslatorImpl.setTranslationListener(new ImageDebugTranslationListener());
//		}
//		return xplane9TranslatorImpl;
//	}
	
	public static ITranslatorProvider getTranslatorProvider(File currentFile, String folderPath) {
		// XPLANE 9 DSF translator provider
		if (GuiOptionsHelper.getOptions().getOutputFormat()
				.equals(Perspectives.PERSPECTIVE_XPLANE9)) {
			return new XPlane9TranslatorProvider(currentFile, folderPath);
		}
		// XPLANE 10 DSF translator provider
		else if (GuiOptionsHelper.getOptions().getOutputFormat()
				.equals(Perspectives.PERSPECTIVE_XPLANE10)) {
			return new XPlane10TranslatorProvider(currentFile, folderPath);
		}
		return new DefaultTranslatorProvider(currentFile, folderPath);
		
	}

	public static Collection<ISpecificTranslator> createAdditinalAdapters(String folderPath) {
		if (GuiOptionsHelper.getOptions().getOutputFormat()
				.equals(Perspectives.PERSPECTIVE_XPLANE10)) {
			return Collections.singletonList(new XPAirfieldTranslationAdapter(folderPath));
		}
		return Collections.emptyList();
	}
}
