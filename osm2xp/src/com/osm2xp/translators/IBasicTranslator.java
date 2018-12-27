package com.osm2xp.translators;

import java.util.List;

import org.openstreetmap.osmosis.osmbinary.Osmformat.HeaderBBox;

import com.osm2xp.exceptions.Osm2xpBusinessException;
import com.osm2xp.model.osm.Node;
import com.osm2xp.model.osm.Tag;

public interface IBasicTranslator {

	/**
	 * process an open street map node.
	 * 
	 * @param node
	 *            osm node
	 * @throws Osm2xpBusinessException
	 */
	public void processNode(Node node) throws Osm2xpBusinessException;

	/**
	 * initialization of the translator.
	 * 
	 * @throws Osm2xpBusinessException
	 */
	public void init();

	/**
	 * translation of the file is complete.
	 */
	public void complete();

	/**
	 * Tells if the given node must be stored.
	 * 
	 * @param node
	 *            osm node
	 * @return true if this node is of interest for this translator.
	 */
	public boolean mustStoreNode(Node node);

	/**
	 * Tells whether given polyline or polygon should be processed.
	 * 
	 * @param tags list of tags for selected object
	 * @return true if this poly is of interest for this translator.
	 */
	public boolean mustProcessPolyline(List<Tag> tags);
	
	/**
	 * Process bounding box definition
	 * @param bbox Bounding box
	 */
	public void processBoundingBox(HeaderBBox bbox);
}
