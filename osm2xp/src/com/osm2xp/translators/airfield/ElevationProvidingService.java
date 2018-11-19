package com.osm2xp.translators.airfield;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.jobs.Job;
import org.json.simple.JSONObject;

import math.geom2d.Point2D;

public class ElevationProvidingService extends GeoMetaProvidingService<Double> {
	
	private static final int MAX_QUERIED_POINTS = 50;

	private static final String ELEVATIONS_ARR_PROP = "elevations";

	private static final String ELEVATION_PROP = "elevation";

	private static ElevationProvidingService instance;
	
	private Map<Point2D, Double> elevMap = new HashMap<Point2D, Double>();
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

	protected GetElevationJob scheduleGetElevations() {
		GetElevationJob elevationJob = new GetElevationJob(toGet);
		elevationJob.schedule();
		toGet = new ArrayList<>();
		return elevationJob;
	}
	
	public void finish() {
		if (toGet.size() > 0) {
			jobList.add(scheduleGetElevations());
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
	protected Job queryOnline(Point2D roundedPoint) {
		GetElevationJob job = null;
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

	@Override
	protected void putObtainedToMap(Job job) {
		elevMap.putAll(((GetElevationJob) job).getElevMap());
	}

}
