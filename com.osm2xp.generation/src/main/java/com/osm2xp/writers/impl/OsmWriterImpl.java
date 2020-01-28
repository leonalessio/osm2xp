package com.osm2xp.writers.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Locale;

import com.osm2xp.core.constants.CoreConstants;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.model.osm.polygon.OsmPolyline;
import com.osm2xp.utils.osm.OsmUtils;
import com.osm2xp.writers.IWriter;

import math.geom2d.Point2D;

/**
 * Osm Writer implementation.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class OsmWriterImpl implements IWriter {
	private String sceneFolder;
	private BufferedWriter output;

	public OsmWriterImpl(String sceneFolder, Point2D currentTile) {
		this.sceneFolder = sceneFolder;
		try {
			new File(sceneFolder).mkdirs();
			String fileName = currentTile != null ? sceneFolder + File.separator + currentTile.y()
			+ "_" + currentTile.x() + ".osm" : sceneFolder + File.separator + "output.osm";
			output = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(fileName), "UTF-8"));
			writerHeader();
		} catch (IOException e) {
			Osm2xpLogger.error("Error initializing writer.", e);
		}
	}

	@Override
	public void init(Point2D coordinates) {

	}

	private void writerHeader() {
		write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		write("<osm version=\"0.6\" generator=\"osm2xp "
				+ CoreConstants.OSM2XP_VERSION + "\">\n");
	}

	private void writerFooter() {
		write("</osm>");
	}

	/**
	 * Ecrit une ligne dans le fichier
	 * 
	 * @param string
	 */
	public void write(Object data) {
		if (data instanceof Node) {
			Node node = (Node) data;
			try {
				if (node.getTags().isEmpty()) {
					output.write(String.format(Locale.ROOT,"<node id=\"%d\" visible=\"true\" version=\"3\" lat=\"%.9f\" lon=\"%.9f\"/>\n",node.getId(), node.getLat(), node.getLon()));
				} else {
					output.write(String.format(Locale.ROOT,"<node id=\"%d\" visible=\"true\" version=\"3\" lat=\"%.9f\" lon=\"%.9f\">\n",node.getId(), node.getLat(), node.getLon()));
					for (Tag tag : node.getTags()) {
						String normalizedTag = OsmUtils.getNormalizedTagText(tag);
						if (normalizedTag != null) {
							output.write(normalizedTag);
						}
					}
					output.write("</node>\n");
					
				}
			} catch (IOException e) {
				Osm2xpLogger.error(e);
			}
		} else if (data instanceof OsmPolyline) {
			try {
				output.write("<way id=\"" + ((OsmPolyline) data).getId()
				+ "\" visible=\"true\" version=\"2\" >\n");

				for (Node node : ((OsmPolyline) data).getNodes()) {
					output.write("<nd ref=\"" + node.getId() + "\"/>\n");
				}

				for (Tag tag : ((OsmPolyline) data).getTags()) {
					String normalizedTag = OsmUtils.getNormalizedTagText(tag);
					if (normalizedTag != null) {
						output.write(normalizedTag);
					}

				}
				output.write("</way>\n");
			} catch (IOException e) {
				Osm2xpLogger.error(e);
			}
		} else if (data instanceof String) {
			try {
				output.write((String) data);
				output.write("\n");
			} catch (IOException e) {
				Osm2xpLogger.error(e);
			}
		} else { 
			Osm2xpLogger.error("Error writing data - unsupported type " + data.getClass().getSimpleName());
		}
	}

	@Override
	public void complete() {
		writerFooter();
	}

}
