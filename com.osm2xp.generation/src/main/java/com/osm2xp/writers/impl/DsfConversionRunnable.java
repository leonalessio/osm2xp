package com.osm2xp.writers.impl;

import java.io.File;
import java.io.IOException;

import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.utils.DsfUtils;

public class DsfConversionRunnable implements Runnable{

	private File textFile;
	private File dsfFile;
	private boolean deleteSourceFile;

	public DsfConversionRunnable(File textFile, File dsfFile, boolean deleteSourceFile) {
//		super("Converting " + textFile.getName() + " to DSF");
		this.textFile = textFile;
		this.dsfFile = dsfFile;
		this.deleteSourceFile = deleteSourceFile;
	}

	@Override
	public void run() {
		if (!DsfUtils.getDsfTool().isFile()) {
			Osm2xpLogger.error("Error on .dsf conversion - coverter not found at " + DsfUtils.getDsfTool().getAbsolutePath());
			return;
		}
		try {
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(new String[] { DsfUtils.getDsfTool().getAbsolutePath(), "--text2dsf",
					textFile.getPath(), dsfFile.getPath() });
			int result = process.waitFor();
			if (result != 0) {
				Osm2xpLogger.error("Error on .dsf conversion - coverter returned invalid response code: " + result + ". Possibly source DSF file is invalid.");
			} else if (deleteSourceFile) {
				textFile.delete();
			}
		} catch (IOException e) {
			Osm2xpLogger.error("Error on .dsf conversion.", e);
		} catch (InterruptedException e) {
			Osm2xpLogger.error("Error on .dsf conversion - thread interrupted.", e);
		}
	}

}
