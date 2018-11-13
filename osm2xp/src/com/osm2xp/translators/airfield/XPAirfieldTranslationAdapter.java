package com.osm2xp.translators.airfield;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openstreetmap.osmosis.osmbinary.Osmformat.HeaderBBox;

import com.osm2xp.exceptions.Osm2xpBusinessException;
import com.osm2xp.model.osm.Node;
import com.osm2xp.model.osm.OsmPolyline;
import com.osm2xp.model.osm.OsmPolylineFactory;
import com.osm2xp.model.osm.Tag;
import com.osm2xp.translators.ITranslationAdapter;
import com.osm2xp.utils.OsmUtils;
import com.osm2xp.utils.helpers.XplaneOptionsHelper;
import com.vividsolutions.jts.geom.Geometry;

import math.geom2d.Point2D;

public class XPAirfieldTranslationAdapter implements ITranslationAdapter {
	
	private List<AirfieldData> airfieldList = new ArrayList<AirfieldData>();
	private List<OsmPolyline> runwayList = new ArrayList<>();
	private List<OsmPolyline> apronAreasList = new ArrayList<>();
	private List<OsmPolyline> taxiLanesList = new ArrayList<>();
	private File workFolder;

	public XPAirfieldTranslationAdapter(String outputFolder) {
		workFolder = new File(outputFolder); 
	}
	
	public boolean handlePoly(OsmPolyline osmPolyline) {
		if (!XplaneOptionsHelper.getOptions().getAirfieldOptions().isGenerateAirfields()) {
			return false;
		}
		String wayType = osmPolyline.getTagValue("aeroway");
		if ("aerodrome".equalsIgnoreCase(wayType)) {
			addAirfiled(osmPolyline);
			return true;
		} else if ("runway".equalsIgnoreCase(wayType)) {
			runwayList.add(osmPolyline);
		} else if ("apron".equalsIgnoreCase(wayType) || "taxiway".equalsIgnoreCase(wayType) || "taxilane".equalsIgnoreCase(wayType)) {
			if (osmPolyline.getPolyline().isClosed()) {
				apronAreasList.add(osmPolyline);
			} else {
				taxiLanesList.add(osmPolyline);
			}
		}
		return false;
	}

	protected void addAirfiled(OsmPolyline osmPolyline) {
		AirfieldData data = new AirfieldData(osmPolyline);
		if (XplaneOptionsHelper.getOptions().getAirfieldOptions().getIgnoredAirfields().contains(data.getICAO())) {
			return;
		}
		if (!data.hasActualElevation() &&  XplaneOptionsHelper.getOptions().getAirfieldOptions().isTryGetElev()) {
			Point2D areaCenter = data.getAreaCenter();
			Double elevation = ElevationProvidingService.getInstance().getElevation(areaCenter, true);
			if (elevation != null) {
				data.setElevation((int) Math.round(elevation));
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void complete() {
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
					break;
				}
			}
		}
		for (Iterator<OsmPolyline> iterator = taxiLanesList.iterator(); iterator.hasNext();) { //Check apron areas matching airports
			OsmPolyline lane = (OsmPolyline) iterator.next();
			for (AirfieldData airfieldData : airfieldList) {
				if (airfieldData.containsPolyline(lane)) {
					airfieldData.addTaxiLane(lane);
					break;
				}
			}
		}
		if (XplaneOptionsHelper.getOptions().getAirfieldOptions().isTryGetElev()) {		
			ElevationProvidingService.getInstance().finish();
			airfieldList.stream().filter(data -> !data.hasActualElevation()).forEach(data -> {
				Double elevation = ElevationProvidingService.getInstance().getElevation(data.getAreaCenter(), false);
				if (elevation != null) {
					data.setElevation((int) Math.round(elevation));
				}
			});
		}
		
		boolean writeAsMainAirfield = XplaneOptionsHelper.getOptions().getAirfieldOptions().isUseSingleAptAsMain() && (airfieldList.size() + runwayList.size() == 1); //If we have only one airport/only one runway - write it as main airfield of scenario
		XPAirfieldOutput airfieldOutput = new XPAirfieldOutput(workFolder, writeAsMainAirfield);
		for (AirfieldData airfieldData : airfieldList) {
			airfieldOutput.writeAirfield(airfieldData);
		}
		for (OsmPolyline runway : runwayList) {
			airfieldOutput.writeSingleRunway(new RunwayData(runway));
		}
	}

	@Override
	public void processNode(Node node) throws Osm2xpBusinessException {
		// Do nothing
	}

	@Override
	public void processBoundingBox(HeaderBBox bbox) {
		// Do nothing		
	}

	@Override
	public Boolean mustProcessPolyline(List<Tag> tags) {
		return OsmUtils.isAeroway(tags);
	}

	@Override
	public Boolean mustStoreNode(Node node) {
		return false;
	}

}
