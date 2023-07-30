package com.osm2xp.translators.airfield;

import java.util.List;
import java.util.concurrent.Callable;

import org.geonames.Toponym;
import org.geonames.WebService;
import org.json.simple.JSONObject;

import com.osm2xp.core.logging.Osm2xpLogger;

import math.geom2d.Point2D;

public class GeonameProvidingService extends GeoMetaProvidingService<String>{
	
	private static GeonameProvidingService instance;

	public static synchronized GeonameProvidingService getInstance() {
		if (instance == null) {
			instance = new GeonameProvidingService();
		}
		return instance;
	}
	
	private GeonameProvidingService() {
		super();
	}

	@Override
	protected String getMetaValue(JSONObject current) {
		Object object = current.get("name");
		if (object == null) {
			return null;
		}
		return object.toString();
	}

	@Override
	protected String getInstancePropName() {
		return "GEONAMES";
	}

	@Override
	protected Callable<?> queryOnline(Point2D roundedPoint) {
		return new GetGeonameCallable(roundedPoint);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void putMetaValue(JSONObject object, String value) {
		object.put("name", value);
	}

	@Override
	protected void putObtainedToMap(Object result) {
		Toponym toponym = (Toponym) result;
		Point2D coords = new Point2D(toponym.getLongitude(), toponym.getLatitude());
		metaMap.put(coords, toponym.getName());
	}

	public String getValueSync(Point2D coords) {
		String value = getMeta(coords, false);
		if (value != null) {
			return value;
		}
		double x = Math.floor(coords.x() * 1000000) / 1000000.0;
		double y = Math.floor(coords.y() * 1000000) / 1000000.0;
		WebService.setUserName("osm2xp");
		try {
			List<Toponym> result = WebService.findNearbyPlaceName(y, x);
			if (result.size() > 0) {
				String name = result.get(0).getName();
				metaMap.put(new Point2D(x,y), name);
				saveToPref();
				return name;
			}
		} catch (Exception e) {
			Osm2xpLogger.log(e);
		}
		return null;
	}
}
