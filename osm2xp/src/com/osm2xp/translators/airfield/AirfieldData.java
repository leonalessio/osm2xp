package com.osm2xp.translators.airfield;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.osm2xp.model.osm.IHasTags;
import com.osm2xp.model.osm.Node;
import com.osm2xp.model.osm.OsmPolygon;
import com.osm2xp.model.osm.OsmPolyline;
import com.osm2xp.utils.OsmUtils;
import com.osm2xp.utils.geometry.GeomUtils;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.polygon.LinearRing2D;

public abstract class AirfieldData extends AerowayData {
	private List<RunwayData> runways = new ArrayList<>();
	private List<OsmPolygon> apronAreas = new ArrayList<>();
	private List<OsmPolyline> taxiLanes = new ArrayList<>();
	private List<Node> helipads = new ArrayList<>();
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

	public void addApronArea(IHasTags area) {
		apronAreas.add((OsmPolygon) area);
	}

	public List<OsmPolygon> getApronAreas() {
		return apronAreas;
	}

	public void addTaxiLane(OsmPolyline lane) {
		taxiLanes.add(lane);
	}

	public List<OsmPolyline> getTaxiLanes() {
		return taxiLanes;
	}

	public abstract Point2D getAreaCenter();

	public abstract boolean contains(double lon, double lat);

	public void addHelipad(Node helipad) {
		helipads.add(helipad);
	}

	public List<Node> getHelipads() {
		return helipads;
	}

	public void addHeliArea(OsmPolygon area) {
		Point2D center = area.getCenter();
		Node heliNode = new Node(area.getTags(), center.y, center.x, -1);
		addHelipad(heliNode);
		addApronArea(area);
	}
}
