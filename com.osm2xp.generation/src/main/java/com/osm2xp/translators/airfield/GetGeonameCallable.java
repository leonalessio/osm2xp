package com.osm2xp.translators.airfield;

import java.util.List;
import java.util.concurrent.Callable;

import org.geonames.Toponym;
import org.geonames.WebService;

import com.osm2xp.core.logging.Osm2xpLogger;

import math.geom2d.Point2D;

public class GetGeonameCallable implements Callable<Toponym>{

	private Point2D coords;

	public GetGeonameCallable(Point2D coords) {
		this.coords = coords;
	}

	public Point2D getCoords() {
		return coords;
	}

	@Override
	public Toponym call() throws Exception {
		WebService.setUserName("osm2xp");
		try {
			List<Toponym> result = WebService.findNearbyPlaceName(coords.y(), coords.x());
			if (result != null && result.size() > 0) {
				return result.get(0);
			}
			
		} catch (Exception e) {
			Osm2xpLogger.log(e);
		}
		return null;
	}
	
}
