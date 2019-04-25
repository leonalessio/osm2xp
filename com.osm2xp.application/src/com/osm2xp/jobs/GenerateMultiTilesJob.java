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
import com.osm2xp.stats.StatsProvider;
import com.osm2xp.translators.ITranslatorProvider;

/**
 * Job for generating scenario for multiple tiles 
 * 
 * @author Dmitry Karpenko
 * 
 */
public class GenerateMultiTilesJob extends GenerateJob {


	private ITranslatorProvider translatorProvider;

	public GenerateMultiTilesJob(File currentFile, String folderPath, ITranslatorProvider translatorProvider) {
		super("Generate several tiles", currentFile, folderPath, "todoJob");
		this.translatorProvider = translatorProvider;
		Osm2xpLogger.info("Starting  generation of several tiles, target folder " + folderPath);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		try {
			IVisitingParser parser = ParserBuilder.getMultiTileParser(currentFile, translatorProvider);
			parser.process();
			Osm2xpLogger.info("Generated: " + StatsProvider.getCommonStats().getSummary());
			Osm2xpLogger.info("Finished generation of " +  ((MultiTileDataConverter) parser.getVisitor()).getTilesCount() + " tiles, target folder " + folderPath);
		} catch (DataSinkException e) {
			Osm2xpLogger.error("Data sink exception : ", e);
		} 

		return Status.OK_STATUS;

	}

}
