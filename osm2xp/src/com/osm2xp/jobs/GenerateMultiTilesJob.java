package com.osm2xp.jobs;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.osm2xp.converters.impl.MultiTileDataConverter;
import com.osm2xp.core.exceptions.DataSinkException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.parsers.IVisitingParser;
import com.osm2xp.parsers.builders.ParserBuilder;
import com.osm2xp.translators.TranslatorBuilder;

/**
 * Job for generating scenario for multiple tiles 
 * 
 * @author Dmitry Karpenko
 * 
 */
public class GenerateMultiTilesJob extends GenerateJob {

	private  String generationMode;

	public GenerateMultiTilesJob(String name, String generationMode, File currentFile,
			String folderPath, String family) {
		super(name, currentFile, folderPath, family);
		this.generationMode = generationMode;
		Osm2xpLogger.info("Starting  generation of several tiles, target folder " + folderPath);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		try {
			IVisitingParser parser = ParserBuilder.getMultiTileParser(currentFile, TranslatorBuilder.getTranslatorProvider(currentFile, folderPath, generationMode));
			parser.process();
			Osm2xpLogger.info("Finished generation of " +  ((MultiTileDataConverter) parser.getVisitor()).getTilesCount() + " tiles, target folder " + folderPath);
		} catch (DataSinkException e) {
			Osm2xpLogger.error("Data sink exception : ", e);
		} 

		return Status.OK_STATUS;

	}

}
