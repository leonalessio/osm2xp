package com.osm2xp.jobs;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.osm2xp.exceptions.DataSinkException;
import com.osm2xp.parsers.IVisitingParser;
import com.osm2xp.parsers.ParserBuilder;
import com.osm2xp.parsers.impl.MultiTileDataConverter;
import com.osm2xp.utils.logging.Osm2xpLogger;

/**
 * Job for generating scenario for multiple tiles 
 * 
 * @author Dmitry Karpenko
 * 
 */
public class GenerateMultiTilesJob extends GenerateJob {

	public GenerateMultiTilesJob(String name, File currentFile, String folderPath,
			String family) {
		super(name, currentFile, folderPath, family);
		Osm2xpLogger.info("Starting  generation of several tiles, target folder " + folderPath);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		try {
			IVisitingParser parser = ParserBuilder.getMultiTileParser(currentFile,
					folderPath);
			parser.process();
			Osm2xpLogger.info("Finished generation of " +  ((MultiTileDataConverter) parser.getVisitor()).getTilesCount() + " tiles, target folder " + folderPath);
		} catch (DataSinkException e) {
			Osm2xpLogger.error("Data sink exception : ", e);
		} 

		return Status.OK_STATUS;

	}

}
