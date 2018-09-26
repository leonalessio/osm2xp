package com.osm2xp.translators.airfield;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.osm2xp.model.osm.OsmPolyline;

import math.geom2d.Box2D;

public class AirfieldData {
	private OsmPolyline osmPolyline;
	private Box2D boundingBox;
	private List<RunwayData> runways = new ArrayList<>();
	private int elevation;
	private String id;
	private String icao;
	private String name;

	public AirfieldData(OsmPolyline osmPolyline) {
		this.osmPolyline = osmPolyline;
		boundingBox = osmPolyline.getPolyline().getBoundingBox();
		icao = osmPolyline.getTagValue("icao");
		name = osmPolyline.getTagValue("name");
		id = icao;
		if (StringUtils.isEmpty(id)) {
			id = osmPolyline.getTagValue("iata");
		}
		if (StringUtils.isEmpty(id)) {
			id = toId(name);
		}
		if (StringUtils.isEmpty(id)) {
			double lat = (boundingBox.getMaxY() + boundingBox.getMinY()) / 2;
			double lon = (boundingBox.getMaxX() + boundingBox.getMinX()) / 2;
			id = String.format("%1.9f_%2.9f", lat, lon);
		}
		String elevStr = osmPolyline.getTagValue("ele");
		if (!StringUtils.isEmpty(elevStr)) {
			try {
				elevation = Integer.parseInt(elevStr); 
			} catch (Exception e) {
				// Ignore
			}
		}
	}
	
	private String toId(String tagValue) {
		if (StringUtils.isEmpty(tagValue)) {
			return null;
		}
		tagValue = tagValue.replaceAll(" ", "_");
		StringBuilder builder = new StringBuilder(tagValue.length());
		for (int i = 0; i < tagValue.length(); i++) {
			char c = tagValue.charAt(i);
			if (Character.isJavaIdentifierPart(c)) {
				builder.append(c);
			}
		}
		return builder.toString();
	}

	public boolean containsPolyline(OsmPolyline polyline) { //Simplified check for now
		Box2D bBox = polyline.getPolyline().getBoundingBox();
		return boundingBox.contains(bBox.getMinX(), bBox.getMinY());
	}
	
	public void addRunway(OsmPolyline runway) {
		runways.add(new RunwayData(runway));
	}

	public String getId() {
		return id;
	}

	public String getICAO() {
		if (StringUtils.isEmpty(icao)) {
			return "xxxx";
		}
		return icao;
	}

	public String getName() {
		if (StringUtils.isEmpty(name)) {
			return id;
		}
		return name;
	}

	public int getElevation() {
		return elevation;
	}

	public List<RunwayData> getRunways() {
		return runways;
	}
}
