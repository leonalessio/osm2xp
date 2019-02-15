package com.osm2xp.console;

import java.io.File;

import com.osm2xp.converters.impl.MultiTileDataConverter;
import com.osm2xp.core.parsers.IVisitingParser;
import com.osm2xp.parsers.builders.ParserBuilder;
import com.osm2xp.translators.OutputFormat;
import com.osm2xp.translators.TranslatorBuilder;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	if (args.length < 1) {
    		System.out.println("Input file should be specified!");
    	}
    	String inputFileName = args[0].trim();
    	File inputFile = new File(inputFileName);
    	if (!inputFile.isFile()) {
			System.out.println("Input file is invalid");
		}
    	
    	String outputFormat = OutputFormat.XPLANE10;
    	
		IVisitingParser parser = ParserBuilder.getMultiTileParser(inputFile, TranslatorBuilder.getTranslatorProvider(inputFile, folderPath, outputFormat));
		parser.process();
		System.out.println("Finished generation of " +  ((MultiTileDataConverter) parser.getVisitor()).getTilesCount() + " tiles, target folder " + folderPath);
    }
}
