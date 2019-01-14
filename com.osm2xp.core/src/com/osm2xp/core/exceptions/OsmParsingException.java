package com.osm2xp.core.exceptions;

/**
 * Osm Parsing exception
 * 
 * @author Benjamin Blanchet
 * 
 */
@SuppressWarnings("serial")
public class OsmParsingException extends Exception {

	public OsmParsingException(String cause, Exception e) {
		super(cause, e);

	}

	public OsmParsingException(Exception e) {
		super(e);
	}
}
