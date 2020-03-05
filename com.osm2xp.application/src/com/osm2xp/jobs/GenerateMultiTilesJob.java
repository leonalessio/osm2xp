package com.osm2xp.jobs;

import java.io.File;
import java.util.Locale;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.osm2xp.converters.impl.MultiTileDataConverter;
import com.osm2xp.core.exceptions.DataSinkException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.parsers.IVisitingParser;
import com.osm2xp.datastore.DataSinkFactory;
import com.osm2xp.datastore.IDataSink;
import com.osm2xp.parsers.builders.ParserBuilder;
import com.osm2xp.stats.StatsProvider;
import com.osm2xp.translators.ITranslatorProvider;
import com.osm2xp.utils.ui.StatusUtil;

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
	}

	@Override
	protected IStatus doGenerate(IProgressMonitor monitor) {
		try {
			Osm2xpLogger.info("Starting  generation of several tiles, target folder " + folderPath);
			long l1 = System.currentTimeMillis();
			IDataSink dataSink = DataSinkFactory.getDataSink();
			try {
				IVisitingParser preprocessParser = ParserBuilder.getPreprocessParser(currentFile, translatorProvider, dataSink);
				if (preprocessParser != null) {
					preprocessParser.process();
				}
			} catch (Exception e1) {
				Osm2xpLogger.error("Error preprocessing input file: ", e1);
			}
			IVisitingParser parser = ParserBuilder.getMultiTileParser(currentFile, translatorProvider, dataSink);
			parser.process();
			Osm2xpLogger.info("Generated: " + StatsProvider.getCommonStats().getSummary());
			Osm2xpLogger.info("Finished generation of " +  ((MultiTileDataConverter) parser.getVisitor()).getTilesCount() + " tiles, target folder " + folderPath);
			Osm2xpLogger.info(String.format(Locale.ROOT,"Generation took %.2f seconds",(System.currentTimeMillis() - l1) / 1000.0));
			
		} catch (DataSinkException e) {
			Osm2xpLogger.error("Data sink exception : ", e);
			return StatusUtil.error(e.getMessage());
		} 

		return Status.OK_STATUS;

	}

}
