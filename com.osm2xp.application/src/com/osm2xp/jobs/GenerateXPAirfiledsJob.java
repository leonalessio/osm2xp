package com.osm2xp.jobs;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.parsers.IParser;
import com.osm2xp.parsers.builders.ParserBuilder;

/**
 * Job for generating scenario for multiple tiles 
 * 
 * @author Dmitry Karpenko
 * 
 */
public class GenerateXPAirfiledsJob extends GenerateJob {

	public GenerateXPAirfiledsJob(File currentFile, String folderPath) {
		super("Generating X-Plane airfield definitions", currentFile, folderPath, "Airfields");
		Osm2xpLogger.info("Starting  generation of X-Plane airfields, target folder " + folderPath);
	}

	@Override
	protected IStatus doGenerate(IProgressMonitor monitor) {
		IParser parser = ParserBuilder.getXPAirfieldGeneratingParser(currentFile,
				folderPath);
		parser.process();
		Osm2xpLogger.info("Finished generation of X-Plane airfields, target folder " + folderPath); 
		return Status.OK_STATUS;

	}

}
