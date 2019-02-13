package com.osm2xp.translators.airfield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.osgi.service.prefs.BackingStoreException;

import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.gui.Activator;

import math.geom2d.Point2D;

public abstract class GeoMetaProvidingService<T> {

	private static final String LNG_PROP = "lon";

	private static final String LAT_PROP = "lat";

	
	protected Map<Point2D, T> metaMap = new HashMap<Point2D, T>();
	protected ExecutorService executor = Executors.newFixedThreadPool(10);
	List<Future<?>> futureList = new ArrayList<>();
	
	public GeoMetaProvidingService() {
		String metaStr = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID).get(getInstancePropName(),"");
		if (!StringUtils.isEmpty(metaStr)) {
			JSONParser parser = new JSONParser();
			try {
				JSONArray array = (JSONArray) parser.parse(metaStr);
				for (int i = 0; i < array.size(); i++) {
					JSONObject current = (JSONObject) array.get(i);
					T ele = getMetaValue(current);
					if (ele != null) {
						Double lat = (Double) current.get(LAT_PROP);
						Double lon = (Double) current.get(LNG_PROP);
						metaMap.put(new Point2D(lon, lat), ele);
					}
				}
			} catch (ParseException e) {
				//just ignore
			}
		}
	}
	
	protected abstract T getMetaValue(JSONObject current);

	protected abstract String getInstancePropName();
	
	protected abstract Callable<?> queryOnline(Point2D roundedPoint);

	protected abstract void putMetaValue(JSONObject object, T value);

	protected abstract void putObtainedToMap(Object result);

	public T getMeta(Point2D point, boolean queryIfAbsent) {
		double x = Math.floor(point.x() * 1000000) / 1000000.0;
		double y = Math.floor(point.y() * 1000000) / 1000000.0;
		Point2D roundedPoint = new Point2D(x,y);
		T meta = metaMap.get(roundedPoint);
		if (meta == null && queryIfAbsent) {
			Callable<?> queryJob = queryOnline(roundedPoint);
			if (queryJob != null) {
				submit(queryJob);
			}
		}
		return meta;
	}

	protected void submit(Callable<?> queryJob) {
		Future<?> submitted = executor.submit(queryJob);
		futureList.add(submitted);
	}

	public void finish() {
		for (Future<?> future: futureList) {
			try {
				Object result = future.get();
				putObtainedToMap(result);
			} catch (Exception e) {
				Osm2xpLogger.log(e);
			}
		}
		saveToPref();
	}

	@SuppressWarnings("unchecked")
	protected void saveToPref() {
		JSONArray array = new JSONArray();
		for (Point2D p2d : metaMap.keySet()) {
			JSONObject object = new JSONObject();
			object.put(LAT_PROP, p2d.y());
			object.put(LNG_PROP, p2d.x());
			putMetaValue(object, metaMap.get(p2d));
			array.add(object);
		}
		IEclipsePreferences node = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		node.put(getInstancePropName(), array.toJSONString());
		try {
			node.flush();
		} catch (BackingStoreException e) {
			Osm2xpLogger.log(e);
		}
	}
	
}
