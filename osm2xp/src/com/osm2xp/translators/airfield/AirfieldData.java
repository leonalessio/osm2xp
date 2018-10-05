package com.osm2xp.translators.airfield;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.osm2xp.model.osm.OsmPolyline;

import math.geom2d.Box2D;

public class AirfieldData extends AerowayData {
	private Box2D boundingBox;
	private List<RunwayData> runways = new ArrayList<>();
	int elevation;
	private String icao;
	
	public AirfieldData(OsmPolyline osmPolyline) {
		super(osmPolyline);
		boundingBox = osmPolyline.getPolyline().getBoundingBox();
		icao = osmPolyline.getTagValue("icao");
		id = toId(icao);
		if (StringUtils.isEmpty(id)) {
			id = toId(osmPolyline.getTagValue("iata"));
		}
		if (StringUtils.isEmpty(id)) {
			id = toId(name);
		}
		if (StringUtils.isEmpty(id)) {
			double lat = (boundingBox.getMaxY() + boundingBox.getMinY()) / 2;
			double lon = (boundingBox.getMaxX() + boundingBox.getMinX()) / 2;
			id = String.format("%1.9f_%2.9f", lat, lon);
		}
	}
	
	public boolean containsPolyline(OsmPolyline polyline) { //Simplified check for now
		Box2D bBox = polyline.getPolyline().getBoundingBox();
		return boundingBox.contains(bBox.getMinX(), bBox.getMinY());
	}
	
	public void addRunway(OsmPolyline runway) {
		runways.add(new RunwayData(runway));
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

	public List<RunwayData> getRunways() {
		return runways;
	}
	
	@Override
	public String toString() {
		return id;
	}
}
