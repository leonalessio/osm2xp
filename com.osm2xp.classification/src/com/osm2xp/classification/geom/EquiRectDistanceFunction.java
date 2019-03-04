package com.osm2xp.classification.geom;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.index.strtree.ItemBoundable;
import org.locationtech.jts.index.strtree.ItemDistance;

import com.eatthepath.jeospatial.GeospatialPoint;
import com.eatthepath.jvptree.DistanceFunction;

public class EquiRectDistanceFunction implements DistanceFunction<GeospatialPoint>, ItemDistance {

	private static int R = 6371;  // radius of the earth in km
	
	@Override
	public double getDistance(GeospatialPoint firstPoint, GeospatialPoint secondPoint) {
		double lat1 = firstPoint.getLatitude();
		double lon1 = firstPoint.getLongitude();
		double lat2 = secondPoint.getLatitude();
		double lon2 = secondPoint.getLongitude();
		
		double x = (lon2 - lon1) * Math.cos( 0.5*(lat2+lat1));
		double y = lat2 - lat1;
		return R * Math.sqrt( x*x + y*y );
	}

	@Override
	public double distance(ItemBoundable item1, ItemBoundable item2) {
		Coordinate p1 = ((Envelope) item1.getBounds()).centre();
		Coordinate p2 = ((Envelope) item2.getBounds()).centre();
		double x = (p2.x - p1.x) * Math.cos( 0.5*(p2.y+p1.y));
		double y = p2.y - p1.y;
		return R * Math.sqrt( x*x + y*y );
	}

}
