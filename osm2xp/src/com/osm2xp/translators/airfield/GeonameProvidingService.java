package com.osm2xp.translators.airfield;

import java.util.List;

import org.eclipse.core.runtime.jobs.Job;
import org.geonames.Toponym;
import org.geonames.WebService;
import org.json.simple.JSONObject;

import com.osm2xp.gui.Activator;

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
		return current.get("name").toString();
	}

	@Override
	protected String getInstancePropName() {
		return "GEONAMES";
	}

	@Override
	protected Job queryOnline(Point2D roundedPoint) {
		GetGeonameJob job = new GetGeonameJob(roundedPoint);
		job.schedule();
		return job;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void putMetaValue(JSONObject object, String value) {
		object.put("name", value);
	}

	@Override
	protected void putObtainedToMap(Job job) {
		metaMap.put(((GetGeonameJob) job).getCoords(), ((GetGeonameJob) job).getObtainedName());
	}

	public String getValueSync(Point2D coords) {
		String value = getMeta(coords, false);
		if (value != null) {
			return value;
		}
		WebService.setUserName("osm2xp");
		try {
			List<Toponym> result = WebService.findNearbyPlaceName(coords.y, coords.x);
			if (result.size() > 0) {
				String name = result.get(0).getName();
				metaMap.put(coords, name);
				saveToPref();
				return name;
			}
		} catch (Exception e) {
			Activator.log(e);
		}
		return null;
	}
}
