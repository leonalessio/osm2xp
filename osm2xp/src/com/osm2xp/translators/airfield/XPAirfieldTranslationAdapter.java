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
import com.vividsolutions.jts.geom.Geometry;

public class XPAirfieldTranslationAdapter implements ITranslationAdapter {
	
	private List<AirfieldData> airfieldList = new ArrayList<AirfieldData>();
	private List<OsmPolyline> runwayList = new ArrayList<>();
	private List<OsmPolyline> apronAreasList = new ArrayList<>();
	private File workFolder;

	public XPAirfieldTranslationAdapter(String outputFolder) {
		workFolder = new File(outputFolder); 
	}
	
	public boolean handlePoly(OsmPolyline osmPolyline) {
		String wayType = osmPolyline.getTagValue("aeroway");
		if ("aerodrome".equalsIgnoreCase(wayType)) {
			airfieldList.add(new AirfieldData(osmPolyline));
			return true;
		} else if ("runway".equalsIgnoreCase(wayType)) {
			runwayList.add(osmPolyline);
		} else if (osmPolyline.getPolyline().isClosed() &&
				("apron".equalsIgnoreCase(wayType) || "taxiway".equalsIgnoreCase(wayType) || "taxilane".equalsIgnoreCase(wayType))) {
			apronAreasList.add(osmPolyline);
		}
		return false;
	}

	@Override
	public void processWays(long wayId, List<Tag> tags, Geometry originalGeometry,
			List<? extends Geometry> fixedGeometries) {
		if (!OsmUtils.isAeroway(tags)) {
			return;
		}
		List<OsmPolyline> polylines = OsmPolylineFactory.createPolylinesFromJTSGeometry(wayId, tags, originalGeometry, false);
		for (OsmPolyline osmPolyline : polylines) {
			handlePoly(osmPolyline);
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
		for (Iterator<OsmPolyline> iterator = apronAreasList.iterator(); iterator.hasNext();) { //Check runways matching airports
			OsmPolyline area = (OsmPolyline) iterator.next();
			for (AirfieldData airfieldData : airfieldList) {
				if (airfieldData.containsPolyline(area)) {
					airfieldData.addApronArea(area);
					iterator.remove();
					break;
				}
			}
		}
		boolean writeAsMainAirfield = (airfieldList.size() + runwayList.size() == 1); //If we have only one airport/only one runway - write it as main airfield of scenario
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
