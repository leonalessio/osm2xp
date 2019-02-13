package com.osm2xp.translators.airfield;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.json.simple.JSONObject;

import math.geom2d.Point2D;

public class ElevationProvidingService extends GeoMetaProvidingService<Double> {
	
	private static final int MAX_QUERIED_POINTS = 10;

	private static final String ELEVATIONS_ARR_PROP = "elevations";

	private static final String ELEVATION_PROP = "elevation";

	private static ElevationProvidingService instance;
	
	private List<Point2D> toGet = new ArrayList<Point2D>();
	
	public static synchronized ElevationProvidingService getInstance() {
		if (instance == null) {
			instance = new ElevationProvidingService();
		}
		return instance;
	}
		
	public Double getElevation(Point2D point, boolean queryIfAbsent) {
		return getMeta(point, queryIfAbsent);
	}

	protected GetElevationCallable scheduleGetElevations() {
		GetElevationCallable elevationCallable = new GetElevationCallable(toGet);
		toGet = new ArrayList<>();
		return elevationCallable;
	}
	
	public void finish() {
		if (toGet.size() > 0) {
			submit(scheduleGetElevations());
		}
		super.finish();
	}

	@Override
	protected Double getMetaValue(JSONObject current) {
		return (Double) current.get(ELEVATION_PROP);
	}

	@Override
	protected String getInstancePropName() {
		return ELEVATIONS_ARR_PROP;
	}

	@Override
	protected Callable<?> queryOnline(Point2D roundedPoint) {
		GetElevationCallable job = null;
		if (toGet.size() == MAX_QUERIED_POINTS) {
			job = scheduleGetElevations();
		}
		toGet.add(roundedPoint);
		return job;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void putMetaValue(JSONObject object, Double value) {
		object.put(ELEVATION_PROP, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void putObtainedToMap(Object result) {
		metaMap.putAll((Map<? extends Point2D, ? extends Double>) result);
	}

}
