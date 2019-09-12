package com.osm2xp.generation.areas;

import java.util.Iterator;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.index.quadtree.Quadtree;

import com.osm2xp.utils.geometry.Osm2XPGeometryFactory;

public class AreaProvider {
	
	private static AreaProvider instance;
	
	private Quadtree quadtree = new Quadtree();
	
	public static synchronized AreaProvider getInstance() {
		if (instance == null) {
			instance = new AreaProvider();
		}
		return instance;
	}

	private AreaProvider() {
		
	}
	
	public void clear() {
		if (quadtree.size() > 0) {
			quadtree = new Quadtree();
		}
	}

	public void addArea(MapArea area) {
		Envelope envelope = area.polygon.getEnvelopeInternal();
		quadtree.insert(envelope, area);
	}
	
	@SuppressWarnings("unchecked")
	public List<MapArea> queryContainingAreas(Coordinate point) {
		List<MapArea> result = quadtree.query(new Envelope(point));
		Point pt = Osm2XPGeometryFactory.getInstance().createPoint(point);
		for (Iterator<MapArea> iterator = result.iterator(); iterator.hasNext();) {
			MapArea mapArea = (MapArea) iterator.next();
			if (!mapArea.polygon.getEnvelopeInternal().contains(point)) {
				iterator.remove();
				continue;
			}
			if (!mapArea.polygon.contains(pt)) {
				iterator.remove();
			}
		}
		return result;
	}
}
