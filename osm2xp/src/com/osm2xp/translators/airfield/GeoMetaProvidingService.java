package com.osm2xp.translators.airfield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.osgi.service.prefs.BackingStoreException;

import com.osm2xp.gui.Activator;

import math.geom2d.Point2D;

public abstract class GeoMetaProvidingService<T> {

	private static final String LNG_PROP = "lon";

	private static final String LAT_PROP = "lat";

	
	protected Map<Point2D, T> metaMap = new HashMap<Point2D, T>();
	protected List<Job> jobList = new ArrayList<>();
	
	public GeoMetaProvidingService() {
		String elevationsStr = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID).get(getInstancePropName(),"");
		if (!StringUtils.isEmpty(elevationsStr)) {
			JSONParser parser = new JSONParser();
			try {
				JSONArray array = (JSONArray) parser.parse(elevationsStr);
				for (int i = 0; i < array.size(); i++) {
					JSONObject current = (JSONObject) array.get(i);
					T ele = getMetaValue(current);
					Double lat = (Double) current.get(LAT_PROP);
					Double lon = (Double) current.get(LNG_PROP);
					metaMap.put(new Point2D(lon, lat), ele);
				}
			} catch (ParseException e) {
				//just ignore
			}
		}
	}
	
	protected abstract T getMetaValue(JSONObject current);

	protected abstract String getInstancePropName();
	
	protected abstract Job queryOnline(Point2D roundedPoint);

	protected abstract void putMetaValue(JSONObject object, T value);

	protected abstract void putObtainedToMap(Job job);

	public T getMeta(Point2D point, boolean queryIfAbsent) {
		double x = Math.floor(point.x * 1000000) / 1000000.0;
		double y = Math.floor(point.y * 1000000) / 1000000.0;
		Point2D roundedPoint = new Point2D(x,y);
		T meta = metaMap.get(roundedPoint);
		if (meta == null && queryIfAbsent) {
			Job queryJob = queryOnline(roundedPoint);
			if (queryJob != null) {
				jobList.add(queryJob);
			}
		}
		return meta;
	}

	public void finish() {
		for (Job job : jobList) {
			try {
				job.join();
				putObtainedToMap(job);
			} catch (InterruptedException e) {
				Activator.log(e);
			}
		}
		saveToPref();
	}

	@SuppressWarnings("unchecked")
	protected void saveToPref() {
		JSONArray array = new JSONArray();
		for (Point2D p2d : metaMap.keySet()) {
			JSONObject object = new JSONObject();
			object.put(LAT_PROP, p2d.y);
			object.put(LNG_PROP, p2d.x);
			putMetaValue(object, metaMap.get(p2d));
			array.add(object);
		}
		IEclipsePreferences node = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		node.put(getInstancePropName(), array.toJSONString());
		try {
			node.flush();
		} catch (BackingStoreException e) {
			Activator.log(e);
		}
	}
	
}
