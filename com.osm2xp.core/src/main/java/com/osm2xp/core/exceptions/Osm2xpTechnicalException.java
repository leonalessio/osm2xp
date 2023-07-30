package com.osm2xp.core.exceptions;

/**
 * Osm2xp unchecked exception.
 * 
 * @author Benjamin Blanchet
 * 
 */
@SuppressWarnings("serial")
public class Osm2xpTechnicalException extends RuntimeException {

	public Osm2xpTechnicalException() {
		super();
	}

	public Osm2xpTechnicalException(String message, Throwable cause) {
		super(message, cause);
	}

	public Osm2xpTechnicalException(String message) {
		super(message);
	}

	public Osm2xpTechnicalException(Throwable cause) {
		super(cause);
	}

}
