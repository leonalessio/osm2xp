package com.osm2xp.translators.airfield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.osgi.service.prefs.BackingStoreException;

import com.osm2xp.gui.Activator;

import math.geom2d.Point2D;

public class ElevationProvidingService {
	
	private static final int MAX_QUERIED_POINTS = 50;

	private static final String ELEVATIONS_ARR_PROP = "elevations";

	private static final String LNG_PROP = "lon";

	private static final String LAT_PROP = "lat";

	private static final String ELEVATION_PROP = "elevation";

	private static ElevationProvidingService instance;
	
	private Map<Point2D, Double> elevMap = new HashMap<Point2D, Double>();
	private List<Point2D> toGet = new ArrayList<Point2D>();
	private List<GetElevationJob> jobList = new ArrayList<>();
	
	public static synchronized ElevationProvidingService getInstance() {
		if (instance == null) {
			instance = new ElevationProvidingService();
		}
		return instance;
	}
	
	private ElevationProvidingService() {
		String elevationsStr = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID).get(ELEVATIONS_ARR_PROP,"");
		if (!StringUtils.isEmpty(elevationsStr)) {
			JSONParser parser = new JSONParser();
			try {
				JSONArray array = (JSONArray) parser.parse(elevationsStr);
				for (int i = 0; i < array.size(); i++) {
					JSONObject current = (JSONObject) array.get(i);
					Double ele = (Double) current.get(ELEVATION_PROP);
					Double lat = (Double) current.get(LAT_PROP);
					Double lon = (Double) current.get(LNG_PROP);
					elevMap.put(new Point2D(lon, lat), ele);
				}
			} catch (ParseException e) {
				//just ignore
			}
		}
	}
	
	public Double getElevation(Point2D point, boolean queryIfAbsent) {
		double x = Math.floor(point.x * 1000000) / 1000000.0;
		double y = Math.floor(point.y * 1000000) / 1000000.0;
		Point2D roundedPoint = new Point2D(x,y);
		Double ele = elevMap.get(roundedPoint);
		if (ele == null && queryIfAbsent) {
			addToQueue(roundedPoint);
		}
		return ele;
	}

	private void addToQueue(Point2D roundedPoint) {
		if (toGet.size() == MAX_QUERIED_POINTS) {
			scheduleGetElevations();
		}
		toGet.add(roundedPoint);
	}

	protected void scheduleGetElevations() {
		GetElevationJob elevationJob = new GetElevationJob(toGet);
		elevationJob.schedule();
		jobList.add(elevationJob);
		toGet = new ArrayList<>();
	}
	
	@SuppressWarnings("unchecked")
	public void finish() {
		if (toGet.size() > 0) {
			scheduleGetElevations();
		}
		for (GetElevationJob job : jobList) {
			try {
				job.join();
				elevMap.putAll(job.getElevMap());
			} catch (InterruptedException e) {
				Activator.log(e);
			}
		}
		JSONArray array = new JSONArray();
		for (Point2D p2d : elevMap.keySet()) {
			JSONObject object = new JSONObject();
			object.put(LAT_PROP, p2d.y);
			object.put(LNG_PROP, p2d.x);
			object.put(ELEVATION_PROP, elevMap.get(p2d));
			array.add(object);
		}
		IEclipsePreferences node = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		node.put(ELEVATIONS_ARR_PROP, array.toJSONString());
		try {
			node.flush();
		} catch (BackingStoreException e) {
			Activator.log(e);
		}
	}

}
