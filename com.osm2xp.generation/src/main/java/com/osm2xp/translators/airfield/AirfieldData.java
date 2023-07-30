package com.osm2xp.translators.airfield;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.ibm.icu.text.Transliterator;
import com.osm2xp.core.model.osm.IHasTags;
import com.osm2xp.model.osm.polygon.OsmPolygon;
import com.osm2xp.model.osm.polygon.OsmPolyline;
import com.osm2xp.utils.geometry.GeomUtils;
import com.osm2xp.utils.osm.OsmUtils;

import math.geom2d.Point2D;
import math.geom2d.line.Line2D;
import math.geom2d.line.LineSegment2D;
import math.geom2d.polygon.LinearRing2D;

public abstract class AirfieldData extends AerowayData {
	
	protected List<RunwayData> runways = new ArrayList<>();
	
	private static final int MIN_DIFFERENT_RUNWAYS_DISTANCE = 100;
	private List<OsmPolygon> apronAreas = new ArrayList<>();
	private List<TaxiLane> taxiLanes = new ArrayList<>();
	private List<HelipadData> helipads = new ArrayList<>();
	private String icao;
	private String iata;
	private Transliterator transliterator = Transliterator.getInstance("Any-Latin; NFD; [^\\p{Alnum}] Remove");
	
	public AirfieldData(IHasTags osmEntity) {
		super(osmEntity);
		icao = osmEntity.getTagValue("icao");
		if (StringUtils.isEmpty(icao) && OsmUtils.isValidICAO(name)) {
			icao = name.toUpperCase().trim();
		}
		if (icao != null) {
			String fixed = fixICAO(icao);
			if (fixed != null) {
				icao = fixed.toUpperCase();
			}
		}
		id = toId(icao);
		if (StringUtils.isEmpty(id)) {
			String iata = osmEntity.getTagValue("iata");
			if (iata != null) {
				id = toId(iata);
				this.iata = iata;
			}
		}
		if (StringUtils.isEmpty(id)) {
			id = toId(name);
		}
		
	}
	
	protected String fixICAO(String icaoCode) {
		if (OsmUtils.isValidICAO(icaoCode)) {
			return icaoCode;
		}
		icaoCode = transliterator.transliterate(icaoCode);
		if (OsmUtils.isValidICAO(icaoCode)) {
			return icaoCode;
		}
		return null;		
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
	
	public List<RunwayData> getUniqueRunways() {
		List<RunwayData> toRemove = new ArrayList<RunwayData>();
		Collections.sort(runways, (r1,r2) -> r2.getLength() - r1.getLength());
		for (int i = 0; i < runways.size(); i++) {
			RunwayData current = runways.get(i);
			for (int j = i+1; j < runways.size(); j++) {
				RunwayData checked = runways.get(j);
				if (toRemove.contains(checked)) {
					continue;
				}
				Line2D curLine = current.getRunwayLine();
				Line2D checkedLine = checked.getRunwayLine();
				
				double l1 = GeomUtils.latLonDistance(curLine.p1, checkedLine.p1);
				double l2 = GeomUtils.latLonDistance(curLine.p2, checkedLine.p2);
				
				double l01 = GeomUtils.latLonDistance(curLine.p1, checkedLine.p2);
				double l02 = GeomUtils.latLonDistance(curLine.p2, checkedLine.p1);
				if ((l1 < MIN_DIFFERENT_RUNWAYS_DISTANCE && l2 < MIN_DIFFERENT_RUNWAYS_DISTANCE) || 
						(l01 < MIN_DIFFERENT_RUNWAYS_DISTANCE && l02 < MIN_DIFFERENT_RUNWAYS_DISTANCE)) {
					current.setHard(current.isHard() || checked.isHard());
					toRemove.add(checked);
					continue;
				}
				double course = current.getCourse1();
				if (Math.abs(course - checked.getCourse1()) < 3 || Math.abs(course - checked.getCourse2()) < 3) { //Almost parallel
					curLine = GeomUtils.line2DToLocal(curLine, checkedLine.p1);
					double distance = curLine.distance(0,0) * 111000; //XXX change to extracted constant
					if (distance < 100) {
						current.setHard(current.isHard() || checked.isHard());
						toRemove.add(checked);
						continue;
					}
				}
			}
		}
		runways.removeAll(toRemove);
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
	 * In Russian, it's '���' - Control Point of an Aerodrome, usually - centerpoint of the main/single runway
	 * @return
	 */
	public Point2D getDatum() {
		RunwayData longestRunway = getLongestRunway();
		if (longestRunway != null) {
			Line2D runwayLine = longestRunway.getRunwayLine();
			return new Point2D(runwayLine.p1.x() / 2 + runwayLine.p2.x() / 2, runwayLine.p1.y() / 2 + runwayLine.p2.y() / 2);
		}
		return getAreaCenter();
	}
	
	public RunwayData getLongestRunway() {
		double maxLen = 0;
		RunwayData longestRwy = null;
		for (RunwayData runway : runways) {
			double curLen = runway.getRunwayLine().length();
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
		Collection<LineSegment2D> edges = ring.edges();
		HelipadData data = new HelipadData(center.x(), center.y());
		if (edges.size() == 4) { //We fully support only rectangular helipads for now
			double maxLen = 0;
			double minLen = Double.MAX_VALUE;
			double trueBearing = -1;
			for (LineSegment2D lineSegment2D : edges) {
				Point2D p1 = lineSegment2D.firstPoint();
				Point2D p2 = lineSegment2D.lastPoint() ;
				double len = GeomUtils.latLonDistance(p1.y(),p1.x(),p2.y(),p2.x());
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

	public String getIATA() {
		return iata;
	}
}
