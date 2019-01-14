package com.osm2xp.translators.airfield;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.model.osm.IHasTags;
import com.osm2xp.core.model.osm.Node;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.model.osm.polygon.OsmPolygon;
import com.osm2xp.model.osm.polygon.OsmPolyline;
import com.osm2xp.model.osm.polygon.OsmPolylineFactory;
import com.osm2xp.translators.ISpecificTranslator;
import com.osm2xp.utils.OsmUtils;
import com.osm2xp.utils.helpers.XplaneOptionsHelper;
import com.vividsolutions.jts.geom.Geometry;

import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.line.Line2D;

public class XPAirfieldTranslationAdapter implements ISpecificTranslator {
	
	private static final String RUNWAY_TAG = "runway";
	private static final String HELIPAD_TAG = "helipad";
	private static final String AERODROME_TAG = "aerodrome";
	private static final String HELIPORT_TAG = "heliport";
	private List<AirfieldData> airfieldList = new ArrayList<AirfieldData>();
	private List<OsmPolyline> runwayList = new ArrayList<>();
	private List<OsmPolyline> apronAreasList = new ArrayList<>();
	private List<OsmPolyline> taxiLanesList = new ArrayList<>();
	private List<OsmPolygon> heliAreasList = new ArrayList<>();
	private List<Node> helipadsList = new ArrayList<>();
	private File workFolder;

	public XPAirfieldTranslationAdapter(String outputFolder) {
		workFolder = new File(outputFolder); 
	}
	
	protected boolean handlePoly(OsmPolyline osmPolyline) {
		if (!XplaneOptionsHelper.getOptions().getAirfieldOptions().isGenerateAirfields()) {
			return false;
		}
		String wayType = osmPolyline.getTagValue("aeroway");
		if (AERODROME_TAG.equalsIgnoreCase(wayType) || HELIPORT_TAG.equalsIgnoreCase(wayType)) {
			addAirfiled(osmPolyline);
			return true;
		} else if (RUNWAY_TAG.equalsIgnoreCase(wayType)) {
			runwayList.add(osmPolyline);
		} else if ("apron".equalsIgnoreCase(wayType) || "taxiway".equalsIgnoreCase(wayType) || "taxilane".equalsIgnoreCase(wayType)) {
			if (osmPolyline.getPolyline().isClosed()) {
				apronAreasList.add(osmPolyline);
			} else {
				taxiLanesList.add(osmPolyline);
			}
		} else if (HELIPAD_TAG.equalsIgnoreCase(wayType)) {
			if (osmPolyline instanceof OsmPolygon) {
				heliAreasList.add((OsmPolygon) osmPolyline);
			}
		}
		return false;
	}

	protected void addAirfiled(IHasTags osmEntity) {
		AirfieldData data;
		if (osmEntity instanceof OsmPolyline) {
			data = new PolyAirfieldData((OsmPolyline) osmEntity);
		} else {
			data = new PointAirfieldData((Node) osmEntity);
			if (HELIPORT_TAG.equalsIgnoreCase(osmEntity.getTagValue("type"))) {
				((PointAirfieldData) data).setMaxRadius(500);
			}
		}
		Point2D areaCenter = data.getAreaCenter();
		if (!data.hasActualElevation() &&  XplaneOptionsHelper.getOptions().getAirfieldOptions().isTryGetElev()) {
			Double elevation = ElevationProvidingService.getInstance().getElevation(areaCenter, true);
			if (elevation != null) {
				data.setElevation((int) Math.round(elevation));
			}
		}
		if (data.getName() == null && data.getICAO() == null && XplaneOptionsHelper.getOptions().getAirfieldOptions().isTryGetName()) {
			String name = GeonameProvidingService.getInstance().getMeta(areaCenter, true);
			if (name != null) {
				data.setName(name);
			}
		}
		airfieldList.add(data);
	}

	@Override
	public void processWays(long wayId, List<Tag> tags, Geometry originalGeometry,
			List<? extends Geometry> fixedGeometries) {
		if (!OsmUtils.isAeroway(tags)) {
			return;
		}
		for (Geometry geometry : fixedGeometries) {
			List<OsmPolyline> polylines = OsmPolylineFactory.createPolylinesFromJTSGeometry(wayId, tags, geometry, false);
			for (OsmPolyline osmPolyline : polylines) {
				handlePoly(osmPolyline);
			}
		}
	}

	@Override
	public void init() {
		// Do nothing
	}

	@Override
	public void complete() {
		bindAirways(airfieldList.stream().filter(data -> data instanceof PolyAirfieldData).collect(Collectors.toList())); //Poly-based airfileds should be processed first - they are more precise, than point-based ones
		bindAirways(airfieldList.stream().filter(data -> data instanceof PointAirfieldData).collect(Collectors.toList()));
		if (XplaneOptionsHelper.getOptions().getAirfieldOptions().isTryGetElev()) {		
			ElevationProvidingService.getInstance().finish();
			airfieldList.stream().filter(data -> !data.hasActualElevation()).forEach(data -> {
				Double elevation = ElevationProvidingService.getInstance().getElevation(data.getAreaCenter(), false);
				if (elevation != null) {
					data.setElevation((int) Math.round(elevation));
				}
			});
		}
		if (XplaneOptionsHelper.getOptions().getAirfieldOptions().isTryGetName()) {
			GeonameProvidingService.getInstance().finish();
			airfieldList.stream().filter(data -> data.getName() == null && data.getICAO() == null).forEach(data -> {
				String name = GeonameProvidingService.getInstance().getMeta(data.getAreaCenter(), false);
				if (name != null) {
					data.setName(name);
				}
			});
		}
		boolean writeAsMainAirfield = XplaneOptionsHelper.getOptions().getAirfieldOptions().isUseSingleAptAsMain() && (airfieldList.size() + runwayList.size() == 1); //If we have only one airport/only one runway - write it as main airfield of scenario
		XPAirfieldOutput airfieldOutput = new XPAirfieldOutput(workFolder, writeAsMainAirfield);
		for (AirfieldData airfieldData : airfieldList) {
			if (XplaneOptionsHelper.getOptions().getAirfieldOptions().getIgnoredAirfields().contains(airfieldData.getICAO())) { //We can't check this at earlier stage since we need to ignore associated runways and other stuff as well
				continue;
			}
			airfieldOutput.writeAirfield(airfieldData);
		}
		for (OsmPolyline runway : runwayList) {
			RunwayData data = new RunwayData(runway);
			if (data.getName() == null && XplaneOptionsHelper.getOptions().getAirfieldOptions().isTryGetName()) {
				Line2D line = data.getRunwayLine();
				String name = GeonameProvidingService.getInstance().getValueSync(Point2D.centroid(new Point2D[] {line.p1, line.p2}));
				if (name != null) {
					data.setName(name);
				}
			}
			airfieldOutput.writeSingleRunway(data);
		}
	}

	protected void bindAirways(List<AirfieldData> airfieldList) {
		//First, try getting registered airfield areafor this
		for (Iterator<OsmPolyline> iterator = runwayList.iterator(); iterator.hasNext();) { //Check runways matching airports
			OsmPolyline runway = (OsmPolyline) iterator.next();
			for (AirfieldData airfieldData : airfieldList) {
				if (airfieldData.containsPolyline(runway)) {
					airfieldData.addRunway(runway);
					iterator.remove();
					break;
				}
			}
		}
		for (Iterator<OsmPolyline> iterator = apronAreasList.iterator(); iterator.hasNext();) { //Check apron areas matching airports
			OsmPolyline area = (OsmPolyline) iterator.next();
			for (AirfieldData airfieldData : airfieldList) {
				if (airfieldData.containsPolyline(area)) {
					airfieldData.addApronArea(area);
					iterator.remove();
					break;
				}
			}
		}
		for (Iterator<OsmPolyline> iterator = taxiLanesList.iterator(); iterator.hasNext();) { //Check apron areas matching airports
			OsmPolyline lane = (OsmPolyline) iterator.next();
			for (AirfieldData airfieldData : airfieldList) {
				if (airfieldData.containsPolyline(lane)) {
					airfieldData.addTaxiLane(lane);
					iterator.remove();
					break;
				}
			}
		}
		for (Iterator<OsmPolygon> iterator = heliAreasList.iterator(); iterator.hasNext();) { //Check apron areas matching airports
			OsmPolygon lane = (OsmPolygon) iterator.next();
			for (AirfieldData airfieldData : airfieldList) {
				if (airfieldData.containsPolyline(lane)) {
					airfieldData.addHeliArea(lane);
					iterator.remove();
					break;
				}
			}
		}
		for (Iterator<Node> iterator = helipadsList.iterator(); iterator.hasNext();) { //Check helipad areas matching airports
			Node helipad = (Node) iterator.next();
			for (AirfieldData airfieldData : airfieldList) {
				if (airfieldData.contains(helipad.getLon(), helipad.getLat())) {
					airfieldData.addHelipad(new HelipadData(helipad.getLon(), helipad.getLat()));
					iterator.remove();
					break;
				}
			}
		}
	}

	@Override
	public void processNode(Node node) throws Osm2xpBusinessException {
		String type = node.getTagValue("aeroway");
		if (HELIPAD_TAG.equals(type)) {
			helipadsList.add(node);
		}
		if (AERODROME_TAG.equalsIgnoreCase(type) || HELIPORT_TAG.equalsIgnoreCase(type)) {
			addAirfiled(node);
		}
	}
	

	@Override
	public void processBoundingBox(Box2D bbox) {
		// Do nothing		
	}

	@Override
	public boolean mustProcessPolyline(List<Tag> tags) {
		return OsmUtils.isAeroway(tags);
	}

	@Override
	public boolean mustStoreNode(Node node) {
		return true;
	}

}
