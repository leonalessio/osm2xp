package com.osm2xp.translators.airfield;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import org.locationtech.jts.geom.Coordinate;

import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.geonames.NamedPoint;
import com.osm2xp.core.model.geonames.PointList;
import com.osm2xp.generation.collections.LonLatKdTree;
import com.osm2xp.generation.collections.NearestSearch;

import math.geom2d.Box2D;

public class LocalGeonameProvider {
	
	protected LonLatKdTree tree = new LonLatKdTree();
	protected NearestSearch nearestSearch;
	
	public LocalGeonameProvider(Box2D bounds, File indexFile) {
		if (!indexFile.isFile()) {
			throw new IllegalArgumentException("Geo names index file " + indexFile.getAbsolutePath() + " does not exist");
		}
		Osm2xpLogger.info("Reading local geo names index");
		int cnt = 0;
		try (ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(indexFile)))) {
			PointList list = (PointList) inputStream.readObject();
			NamedPoint[] points = list.getPoints();
			for (NamedPoint point: points) {
				double x = point.getX();
				double y = point.getY();
				if (bounds.getMinX() <= x && x <= bounds.getMaxX() &&
					bounds.getMinY() <= y && y <= bounds.getMaxY()) {
					tree.insert(new Coordinate(x,y),point.getName());
				}
				cnt++;
				if (cnt % 100000 == 0) {
					Osm2xpLogger.info(cnt + " named points loaded");
				}
			}
			nearestSearch = new NearestSearch(tree);
		} catch (Exception e) {
			throw new IllegalArgumentException("Geo names index file " + indexFile.getAbsolutePath() + " is invalid/cannot be opened",e);
		}
	}
	
	public String getName(double x, double y) {
		Object nearestFeature = nearestSearch.nearestFeature(new Coordinate(x,y));
		if (nearestFeature != null) {
			return nearestFeature.toString();
		}
		return null;
	}
}
