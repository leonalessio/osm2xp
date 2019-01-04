package com.osm2xp.translators.airfield;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.osm2xp.model.osm.IHasTags;
import com.osm2xp.model.osm.OsmPolygon;
import com.osm2xp.model.osm.OsmPolyline;
import com.osm2xp.utils.OsmUtils;
import com.osm2xp.utils.geometry.GeomUtils;

import math.geom2d.Point2D;
import math.geom2d.line.Line2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.polygon.LinearRing2D;

public abstract class AirfieldData extends AerowayData {
	private List<RunwayData> runways = new ArrayList<>();
	private List<OsmPolygon> apronAreas = new ArrayList<>();
	private List<TaxiLane> taxiLanes = new ArrayList<>();
	private List<HelipadData> helipads = new ArrayList<>();
	private String icao;
	
	public AirfieldData(IHasTags osmEntity) {
		super(osmEntity);
		icao = osmEntity.getTagValue("icao");
		if (StringUtils.isEmpty(icao) && OsmUtils.isValidICAO(name)) {
			icao = name.toUpperCase().trim();
		}
		if (icao != null) {
			icao = icao.toUpperCase();
		}
		id = toId(icao);
		if (StringUtils.isEmpty(id)) {
			id = toId(osmEntity.getTagValue("iata"));
		}
		if (StringUtils.isEmpty(id)) {
			id = toId(name);
		}
		
	}
	
	public abstract boolean containsPolyline(OsmPolyline polyline);
	
	public void addRunway(OsmPolyline runway) {
		runways.add(new RunwayData(runway));
	}

	public String getICAO() {		
		return icao;
	}

	public String getLabel() {
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

	public void addApronArea(IHasTags area) {
		apronAreas.add((OsmPolygon) area);
	}

	public List<OsmPolygon> getApronAreas() {
		return apronAreas;
	}

	public void addTaxiLane(OsmPolyline lane) {
		TaxiLane taxiLane = new TaxiLane(lane.getPolyline());
		String widthStr = lane.getTagValue("width");
		if (StringUtils.isNotEmpty(widthStr)) {
			try {
				taxiLane.setWidth(Double.parseDouble(widthStr));
			} catch (NumberFormatException e) {
				// ignore
			}
		}
		taxiLanes.add(taxiLane);
	}

	public List<TaxiLane> getTaxiLanes() {
		return taxiLanes;
	}

	public abstract Point2D getAreaCenter();
	
	/**
	 * Return datum point of an airfield
	 * In Russian, it's 'ÊÒÀ' - Control Point of an Aerodrome, usually - centerpoint of the main/single runway
	 * @return
	 */
	public Point2D getDatum() {
		RunwayData longestRunway = getLongestRunway();
		if (longestRunway != null) {
			Line2D runwayLine = longestRunway.getRunwayLine();
			return new Point2D(runwayLine.p1.x / 2 + runwayLine.p2.x / 2, runwayLine.p1.y / 2 + runwayLine.p2.y / 2);
		}
		return getAreaCenter();
	}
	
	public RunwayData getLongestRunway() {
		double maxLen = 0;
		RunwayData longestRwy = null;
		for (RunwayData runway : runways) {
			double curLen = runway.getRunwayLine().getLength();
			if (curLen > maxLen) {
				maxLen = curLen;
				longestRwy = runway;
			}
		}
		return longestRwy;
	}

	public abstract boolean contains(double lon, double lat);

	public void addHelipad(HelipadData data) {
		helipads.add(data);
	}

	public List<HelipadData> getHelipads() {
		return helipads;
	}

	public void addHeliArea(OsmPolygon area) {
		Point2D center = area.getCenter();
		LinearRing2D ring = area.getPolygon();
		Collection<LineSegment2D> edges = ring.getEdges();
		HelipadData data = new HelipadData(center.x, center.y);
		if (edges.size() == 4) { //We fully support only rectangular helipads for now
			double maxLen = 0;
			double minLen = Double.MAX_VALUE;
			double trueBearing = -1;
			for (LineSegment2D lineSegment2D : edges) {
				Point2D p1 = lineSegment2D.getFirstPoint();
				Point2D p2 = lineSegment2D.getLastPoint();
				double len = GeomUtils.latLonDistance(p1.y,p1.x,p2.y,p2.x);
				if (len > maxLen) {
					maxLen = len;
					trueBearing = GeomUtils.getTrueBearing(p1,p2);
				}
				if (len < minLen) {
					minLen = len;
				}
			}
			data.setHeading(trueBearing);
			data.setLength(maxLen);
			data.setWidth(minLen);
		} else {
			addApronArea(area);
		}
		addHelipad(data);
	}
}
