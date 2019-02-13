package com.osm2xp.core.exceptions;

/**
 * Osm2xp business exception.
 * 
 * @author Benjamin Blanchet
 * 
 */
@SuppressWarnings("serial")
public class Osm2xpBusinessException extends Exception {

	public Osm2xpBusinessException(String cause, Exception e) {
		super(cause, e);
	}

	public Osm2xpBusinessException(String cause) {
		super(cause);

	}
}
