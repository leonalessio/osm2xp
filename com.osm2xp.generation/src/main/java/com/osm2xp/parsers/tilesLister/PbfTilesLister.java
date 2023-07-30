package com.osm2xp.parsers.tilesLister;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openstreetmap.osmosis.osmbinary.BinaryParser;
import org.openstreetmap.osmosis.osmbinary.Osmformat.*;
import org.openstreetmap.osmosis.osmbinary.file.BlockInputStream;

import math.geom2d.Point2D;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.generation.collections.PointSet;


/**
 * PbfTilesLister.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class PbfTilesLister extends BinaryParser implements TilesLister {

	private Set<Point2D> tilesList = new PointSet();
	private File file;

	public PbfTilesLister(File file) {
		this.file = file;

	}

	/**
	 * 
	 */
	public void complete() {

	}

	@Override
	protected void parseRelations(List<Relation> rels) {
	}

	@Override
	protected void parseDense(DenseNodes nodes) {
		long lastId = 0, lastLat = 0, lastLon = 0;

		// Stuff for dense info
		for (int i = 0; i < nodes.getIdCount(); i++) {

			long lat = nodes.getLat(i) + lastLat;
			lastLat = lat;
			long lon = nodes.getLon(i) + lastLon;
			lastLon = lon;
			long id = nodes.getId(i) + lastId;
			lastId = id;
			double latf = parseLat(lat), lonf = parseLon(lon);

			Point2D cleanedLoc = new Point2D((int) Math.floor(lonf), (int) Math.floor(latf));
			tilesList.add(cleanedLoc);
		}

	}

	@Override
	protected void parseNodes(List<Node> nodes) {
		if (nodes.size() > 0) {
			System.out.println("PbfTilesLister.parseNodes()");
		}
	}

	@Override
	protected void parseWays(List<Way> ways) {

	}

	@Override
	protected void parse(HeaderBlock header) {
	}

	public void process() throws Osm2xpBusinessException {

		InputStream input = null;
		try {
			input = new FileInputStream(this.file);
		} catch (FileNotFoundException e) {
			throw new Osm2xpBusinessException(e.getMessage());
		}
		BlockInputStream bm = new BlockInputStream(input, this);
		try {
			bm.process();
		} catch (IOException e) {
			throw new Osm2xpBusinessException(e.getMessage());

		}
	}

	public Set<Point2D> getTilesList() {
		return this.tilesList;
	}

}
