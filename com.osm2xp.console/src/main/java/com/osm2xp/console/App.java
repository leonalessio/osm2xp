package com.osm2xp.console;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.osm2xp.converters.impl.MultiTileDataConverter;
import com.osm2xp.core.exceptions.DataSinkException;
import com.osm2xp.core.parsers.IVisitingParser;
import com.osm2xp.generation.options.GlobalOptions;
import com.osm2xp.generation.options.GlobalOptionsProvider;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.generation.paths.PathsService;
import com.osm2xp.generation.preferences.BasicPreferences;
import com.osm2xp.generation.preferences.PreferenceService;
import com.osm2xp.parsers.builders.ParserBuilder;
import com.osm2xp.translators.OutputFormat;
import com.osm2xp.translators.TranslatorBuilder;


/**
 * Main console app class
 *
 */
public class App 
{
    private static final String DBMODE = "dbmode";
	private static final String FACADE_SETS = "facade-sets";
	private static final String CONFIG_FOLDER = "config-folder";
	private static final String SCENERY_NAME = "scenery-name";
	private static final String MODE = "mode";

	public static void main( String[] args )
    {
    	if (args.length < 1) {
    		System.out.println("Input file should be specified!");
    	}
    	if (args.length > 0) {
    		CommandLine commandLine = parseCommandLine(args);
    		if (commandLine == null) {
    			return;
    		}
    		if (commandLine.getArgList().size() < 1) {
    			System.out.println("Input file should be specified");
    		}
    		String inputFileName = commandLine.getArgs()[0].trim();
    		File inputFile = new File(inputFileName);
    		if (!inputFile.isFile()) {
    			System.out.println("Input file " + inputFileName + " is invalid");
    		}
    		String customFolder = commandLine.getOptionValue(CONFIG_FOLDER);
    		if (customFolder != null) {
    			File dir = new File(customFolder);
    			if (!dir.isDirectory()) {
    				System.out.println("Specified folder " + customFolder + " does not exist.");
    				return;
    			}
				PathsService.getPathsProvider().setBasicFolder(dir);
			}
    		XPlaneOptionsProvider.loadDefaultOptions(); //TODO support custom options location
    		PreferenceService.setProgramPreferences(new BasicPreferences());
    		String[] optionValues = commandLine.getOptionValues(FACADE_SETS);
    		if (optionValues != null && optionValues.length > 0) {
				XPlaneOptionsProvider.getOptions().setFacadeSets(StringUtils.join(optionValues,';'));
			} else if (XPlaneOptionsProvider.getOptions().getFacadeSets() == null) {
				String defaultFacadeSets = XPlaneOptionsProvider.getDefaultFacadeSets();
				System.out.println("No facade sets configured, will use default: " + defaultFacadeSets);
				XPlaneOptionsProvider.getOptions().setFacadeSets(defaultFacadeSets);
			}
    		
    		String modeStr = commandLine.getOptionValue(MODE);
    		String outputFormat;
    		if (modeStr != null) {
				outputFormat = modeStr.toUpperCase();
			} else {
				outputFormat = OutputFormat.XPLANE10;
			}
    		//TODO no validation for outputFormat. Should this be validated or refactored? 
    		//TODO interpret other args
    		
    		
    		String sceneryName = commandLine.getOptionValue(SCENERY_NAME);
    		if (sceneryName == null) {
    			sceneryName = computeDefaultSceneName(inputFile);
			}
    		File targetFolder = new File(inputFile.getParent(), sceneryName);
    		if (targetFolder.exists()) {
    			System.out.println("Target folder " + sceneryName + " exists and will be deleted.");
    			try {
					FileUtils.deleteDirectory(targetFolder);
				} catch (IOException e) {
					System.out.println("Target folder " + sceneryName + " deletion failed, this can cause generation to fail. Reason:");
					e.printStackTrace();
				}
    		}
    		GlobalOptionsProvider.setOptions(new GlobalOptions()); //TODO loading actual global options
    		GlobalOptionsProvider.getOptions().setCurrentFilePath(inputFile.getAbsolutePath());
    		IVisitingParser parser;
    		try {
    			parser = ParserBuilder.getMultiTileParser(inputFile, TranslatorBuilder.getTranslatorProvider(inputFile, targetFolder.getAbsolutePath(), outputFormat));
    			parser.process();
    			System.out.println("Finished generation of " +  ((MultiTileDataConverter) parser.getVisitor()).getTilesCount() + " tiles, target folder " + sceneryName);
    		} catch (DataSinkException e) {
    			e.printStackTrace();
    		}
		}
    }

	protected static String computeDefaultSceneName(File inputFile) {
		String sceneName = inputFile.getName();
    	int idx = sceneName.lastIndexOf('.');
    	if (idx > 0) {
			sceneName = sceneName.substring(0, idx);
		}
    	return sceneName;
	}
	
	private static CommandLine parseCommandLine(String[] args) {
		Options options = new Options();
		options.addOption( "m", MODE, true, "Generation mode, 'xplane10' by default");
		options.addOption( "s", SCENERY_NAME, true, "scenery name");
		options.addOption( "c", CONFIG_FOLDER, true, "use this folder instead of current one when looking for "
				+ "configuration, resources, tools etc. Can be used to have generation profiles.");
		Option facadeSetsOption = new Option("f", FACADE_SETS, true, "facade sets for X-Plane scenario generation");
		facadeSetsOption.setArgs(Option.UNLIMITED_VALUES);				
		options.addOption( "o", "options", true, "Generation options file for given mode. Will use default if it's not specified");
		options.addOption(facadeSetsOption);
		options.addOption( "d", DBMODE, false, "Use database mode - will store some data during generation on disk, which allows to process larger input files");
		CommandLineParser parser = new DefaultParser();
		try {
			return parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println("Invalid command line.");
			e.printStackTrace();
		}
		return null;
	}
}
