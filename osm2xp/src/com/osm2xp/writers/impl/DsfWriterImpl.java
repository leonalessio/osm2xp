package com.osm2xp.writers.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;

import com.osm2xp.constants.XplaneConstants;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.utils.DsfUtils;
import com.osm2xp.writers.IHeaderedWriter;

import math.geom2d.Point2D;

/**
 * Dsf Writer implementation.
 * 
 * @author Benjamin Blanchet, Dmitry Karpenko
 * 
 */
public class DsfWriterImpl implements IHeaderedWriter {

	private File dsfFile;
	private BufferedWriter writer;
	private boolean headerWritten = false;
	private String header;
	
	public DsfWriterImpl(String sceneFolder, Point2D tile) {
		dsfFile = DsfUtils.computeXPlaneDsfFilePath(sceneFolder, tile);
		// create the parent folder file
		File parentFolder = new File(dsfFile.getParent());
		parentFolder.mkdirs();
		// create writer for this file

		try {
			writer = Files.newBufferedWriter(dsfFile.toPath(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			Osm2xpLogger.error("Error writing " + dsfFile.getAbsolutePath(), e);
		}		
	}

	public void write(Object data) {
		try {
			if (!headerWritten) {
				writer.write(header);
				headerWritten = true;
			}
			if (data != null) {
				// write into this dsf file
				writer.write((String) data);
			}
		} catch (IOException e) {
			Osm2xpLogger.error(e.getMessage());
		}
	}

	@Override
	public void complete(Object data) {
		// flush/close all writers
			try {
				writer.flush();
				writer.close();
			} catch (IOException e) {
				Osm2xpLogger.error(e.getMessage());
			}
		if (headerWritten) {
			if (data != null) {
				try {
					injectSmartExclusions((String) data);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			DsfUtils.textToDsf(dsfFile, new File(dsfFile.getPath().replaceAll(".txt", "")));
		} else {
			dsfFile.delete(); //No header writter means empty file - nothing was added to it. So just delete.
		}
	}

	@Override
	public void init(Point2D coordinates) {

	}

	private void injectSmartExclusions(String exclusionText) throws IOException {

		// temp file
		File tempFile = new File(dsfFile.getAbsolutePath() + "_temp");
		BufferedReader br = new BufferedReader(new FileReader(dsfFile));
		String line;
		Boolean exclusionInjected = false;

		FileWriter writer = new FileWriter(tempFile.getPath(), true);
		BufferedWriter output = new BufferedWriter(writer);

		while ((line = br.readLine()) != null) {
			if (!exclusionInjected && line.contains(XplaneConstants.EXCLUSION_PLACEHOLDER)) {
				output.write(exclusionText);
				exclusionInjected = true;
			} else {
				output.write(line + "\n");
			}
		}
		br.close();
		output.flush();
		output.close();

		FileUtils.copyFile(tempFile, dsfFile);
		tempFile.delete();
		tempFile.deleteOnExit();

	}

	@Override
	public void setHeader(Object header) {
		this.header = header.toString();
	}
}
