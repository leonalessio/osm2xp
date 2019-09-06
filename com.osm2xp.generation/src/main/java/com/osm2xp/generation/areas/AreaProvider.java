package com.osm2xp.generation.areas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.index.bintree.Bintree;
import org.locationtech.jts.index.bintree.Interval;

import com.osm2xp.utils.geometry.Osm2XPGeometryFactory;

public class AreaProvider {
	
	private static AreaProvider instance;
	
//	private ArrayBasedIndex<MapArea> areas = new ArrayBasedIndex<>();
	private Bintree xTree = new Bintree();
	private Bintree yTree = new Bintree();
	
	public static synchronized AreaProvider getInstance() {
		if (instance == null) {
			instance = new AreaProvider();
		}
		return instance;
	}

	private AreaProvider() {
		
	}
	
	public void clear() {
		if (xTree.size() > 0) {
			xTree = new Bintree();
			yTree = new Bintree();
		}
	}

	public void addArea(MapArea area) {
		Envelope envelope = area.polygon.getEnvelopeInternal();
		xTree.insert(new Interval(envelope.getMinX(), envelope.getMaxX()), area);
		yTree.insert(new Interval(envelope.getMinY(), envelope.getMaxY()), area);
	}
	
	@SuppressWarnings("unchecked")
	public List<MapArea> queryContainingAreas(Coordinate point) {
		List<MapArea> xMatch = xTree.query(point.x);
		List<MapArea> yMatch = yTree.query(point.y);
		ArrayList<MapArea> result = new ArrayList<MapArea>(xMatch);
		result.retainAll(yMatch);
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
