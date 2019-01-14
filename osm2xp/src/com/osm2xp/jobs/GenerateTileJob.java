package com.osm2xp.jobs;

import java.awt.Point;
import java.io.File;
import java.util.List;
import java.util.Locale;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.osm2xp.core.exceptions.DataSinkException;
import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Relation;
import com.osm2xp.core.parsers.IParser;
import com.osm2xp.parsers.builders.ParserBuilder;
import com.osm2xp.utils.helpers.Osm2xpProjectHelper;

import math.geom2d.Point2D;

/**
 * GenerateTileJob.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class GenerateTileJob extends GenerateJob {

	private transient Point2D coordinates;

	public GenerateTileJob(String name, File currentFile, Point2D coordinates,
			String folderPath, List<Relation> relationsList, String family) {
		super(name, currentFile, folderPath, family);
		Osm2xpLogger.info("Starting  generation of " + getCoordinatesStr(coordinates) + ", target folder " + folderPath);
		this.coordinates = coordinates;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		IParser parser;
		try {
			parser = ParserBuilder.getParser(coordinates, currentFile,
					folderPath);
			parser.process();
			Osm2xpProjectHelper.removeTile(coordinates);
			Osm2xpLogger.info("Finished generation of " + getCoordinatesStr(coordinates) + ", target folder " + folderPath);
		} catch (DataSinkException e) {
			Osm2xpLogger.error("Data sink exception : ", e);
		} catch (Osm2xpBusinessException e) {
			Osm2xpLogger.error("Business exception : ", e);
		}

		return Status.OK_STATUS;

	}

	private String getCoordinatesStr(Point2D coords) {
		if (coords == null) {
			return "whole file";
		}
		Point intPt = coords.getAsInt();
		return String.format(Locale.ROOT, "tile (%d,%d)", intPt.x, intPt.y);
	}

}
