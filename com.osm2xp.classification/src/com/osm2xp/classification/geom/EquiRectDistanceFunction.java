package com.osm2xp.classification.geom;

import com.eatthepath.jeospatial.GeospatialPoint;
import com.eatthepath.jvptree.DistanceFunction;

public class EquiRectDistanceFunction implements DistanceFunction<GeospatialPoint> {

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

}
