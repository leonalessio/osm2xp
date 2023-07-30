package com.osm2xp.translators;

import java.util.List;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.model.osm.polygon.OsmPolyline;

/**
 * ITranslator.
 * 
 * @author Benjamin Blanchet
 * 
 */
public interface ITranslator extends IBasicTranslator{

	/**
	 * process an open street map polygon.
	 * 
	 * @param OsmPolygon
	 *            osm polygon
	 * @throws Osm2xpBusinessException
	 */
	public void processPolyline(OsmPolyline polyline)
			throws Osm2xpBusinessException;
	
	/**
	 * Returns maximum hole count for polyline with given tags. 0 would mean no holes allowed
	 * @param tags Tags for polyline being analyzed
	 * @return max hole count for polygon with given tags. 0 - no holes allowed, you can use Integer.MAX_VALUE to indicate no restriction
	 */
	public int getMaxHoleCount(List<Tag> tags);

}
