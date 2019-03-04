package com.osm2xp.translators;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.osm2xp.core.parsers.IOSMDataVisitor;
import com.osm2xp.translators.airfield.XPAirfieldGeneratorFactory;
import com.osm2xp.translators.xplane.XP10TranslatorProviderFactory;
import com.osm2xp.translators.xplane.XP9TranslatorProviderFactory;

import math.geom2d.Point2D;

/**
 * TranslatorBuilder.
 * 
 * @author Benjamin Blanchet
 * @author Dmitry Karpenko 
 * 
 */
public class TranslatorBuilder {
	
	private static Map<String, ITranslatorProviderFactory> providerFactories = new HashMap<>();
	private static Map<String, ITranslatorFactory> translatorFactories = new HashMap<>();
	private static Map<String, IDataVisitorFactory> visitorFactories = new HashMap<>();
	
	static {
		registerTranslatorProviderFactory(new XP10TranslatorProviderFactory());
		registerTranslatorProviderFactory(new XP9TranslatorProviderFactory());
		registerVisitorFactory(new XPAirfieldGeneratorFactory());
		registerTranslatorFactory(new ConsoleTranslatorFactory());
		registerTranslatorFactory(new G2XPLTranslatorFactory());
		registerTranslatorFactory(new OSMTranslatorFactory());
		registerTranslatorFactory(new FlightGearTranslatorFactory());
		registerTranslatorFactory(new FlyLegacyTranslatorFactory());
		registerTranslatorFactory(new FSXTranslatorFactory());
	}

	public static void registerTranslatorProviderFactory(ITranslatorProviderFactory factory) {
		providerFactories.put(factory.getOutputType(), factory);
	}
	
	public static void registerTranslatorFactory(ITranslatorFactory factory) {
		translatorFactories.put(factory.getOutputMode(), factory);
	}
	
	public static void registerVisitorFactory(IDataVisitorFactory factory) {
		visitorFactories.put(factory.getOutputType(), factory);
	}
	
	public static ITranslator getTranslator(File currentFile,
			Point2D currentTile, String folderPath, String outputFormat) {
		ITranslatorFactory factory = translatorFactories.get(outputFormat);
		if (factory != null) {
			return factory.getTranslator(currentFile,currentTile, folderPath);
		}
		return null;
	}
	
	public static IOSMDataVisitor getDataVisitor(File currentFile, String folderPath, String outputFomat) {
		 IDataVisitorFactory factory = visitorFactories.get(outputFomat);
		 if (factory != null) {
			return factory.getVisitor(folderPath);
		}
		return null;
	}

	public static ITranslatorProvider getTranslatorProvider(File currentFile, String folderPath, String outputFomat) {
		ITranslatorProviderFactory factory = providerFactories.get(outputFomat);
		if (factory != null) {
			return factory.getTranslatorProvider(currentFile, folderPath);
		}
		return null;
	}

	public static String getRegisteredFormatsStr() {
		List<String> list = new ArrayList<String>(providerFactories.keySet());
		list.addAll(translatorFactories.keySet());
		return list.stream().distinct().sorted().collect(Collectors.joining(", "));
	}
	
	public static boolean isSupported(String format) {
		return providerFactories.containsKey(format) || translatorFactories.containsKey(format) || visitorFactories.containsKey(format);
	}

}
