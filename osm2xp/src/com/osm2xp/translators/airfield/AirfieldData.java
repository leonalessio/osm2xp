package com.osm2xp.translators.airfield;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.osm2xp.model.osm.OsmPolygon;
import com.osm2xp.model.osm.OsmPolyline;
import com.osm2xp.utils.OsmUtils;
import com.osm2xp.utils.geometry.GeomUtils;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.polygon.LinearRing2D;
import math.geom2d.polygon.Polyline2D;

public class AirfieldData extends AerowayData {
	private Box2D boundingBox;
	private List<RunwayData> runways = new ArrayList<>();
	private List<OsmPolygon> apronAreas = new ArrayList<>();
	private List<OsmPolyline> taxiLanes = new ArrayList<>();
	private String icao;
	private Polyline2D polygon;
	
	public AirfieldData(OsmPolyline osmPolyline) {
		super(osmPolyline);
		if (osmPolyline.getPolyline().isClosed() && osmPolyline.getPolyline() instanceof LinearRing2D) {
			polygon = GeomUtils.forceCCW((LinearRing2D) osmPolyline.getPolyline());
		}
		boundingBox = osmPolyline.getPolyline().getBoundingBox();
		icao = osmPolyline.getTagValue("icao");
		if (StringUtils.isEmpty(icao) && OsmUtils.isValidICAO(name)) {
			icao = name.toUpperCase().trim();
		}
		if (icao != null) {
			icao = icao.toUpperCase();
		}
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
	
	public boolean isHard() {
		for (RunwayData runway : runways) {
			if (runway.isHard()) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		return id;
	}

	public void addApronArea(OsmPolyline area) {
		apronAreas.add((OsmPolygon) area);
	}

	public List<OsmPolygon> getApronAreas() {
		return apronAreas;
	}

	public Polyline2D getPolygon() {
		return polygon;
	}

	public void addTaxiLane(OsmPolyline lane) {
		taxiLanes.add(lane);
	}

	public List<OsmPolyline> getTaxiLanes() {
		return taxiLanes;
	}

	public Point2D getAreaCenter() {
		return Point2D.centroid(polygon.getPointArray());
	}
}
