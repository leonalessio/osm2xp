package com.osm2xp.translators.airfield;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.osm2xp.model.osm.OsmPolyline;
import com.osm2xp.translators.IPolyHandler;

public class XPAirfieldTranslator implements IPolyHandler {
	
	List<AirfieldData> airfieldList = new ArrayList<AirfieldData>();
	List<OsmPolyline> runwayList = new ArrayList<>();
	private File workFolder;

	public XPAirfieldTranslator(String outputFolder) {
		workFolder = new File(outputFolder, "airports"); 
	}
	
	@Override
	public boolean handlePoly(OsmPolyline osmPolyline) {
		String wayType = osmPolyline.getTagValue("aeroway");
		if ("aerodrome".equalsIgnoreCase(wayType)) {
			airfieldList.add(new AirfieldData(osmPolyline));
			return true;
		} else if ("runway".equalsIgnoreCase(wayType)) {
			runwayList.add(osmPolyline);
		}
		return false;
	}

	@Override
	public void translationComplete() {
		XPAirfieldOutput airfieldOutput = new XPAirfieldOutput(workFolder);
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
		for (AirfieldData airfieldData : airfieldList) {
			airfieldOutput.writeAirfield(airfieldData);
		}
	}

}
