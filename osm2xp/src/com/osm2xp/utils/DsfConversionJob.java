package com.osm2xp.utils;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.osm2xp.core.logging.Osm2xpLogger;

public class DsfConversionJob extends Job {

	private File textFile;
	private File dsfFile;

	public DsfConversionJob(File textFile, File dsfFile) {
		super("Converting " + textFile.getName() + " to DSF");
		this.textFile = textFile;
		this.dsfFile = dsfFile;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(new String[] { DsfUtils.getDsfToolPath(), "--text2dsf",
					textFile.getPath(), dsfFile.getPath() });
			int result = process.waitFor();
			if (result != 0) {
				Osm2xpLogger.error("Error on .dsf conversion - coverter returned invalid response code: " + result + ". Possibly source DSF file is invalid.");
			} else {
				textFile.delete();
			}
		} catch (IOException e) {
			Osm2xpLogger.error("Error on .dsf conversion.", e);
		} catch (InterruptedException e) {
			Osm2xpLogger.error("Error on .dsf conversion - thread interrupted.", e);
		}
		return Status.OK_STATUS;
	}

}
