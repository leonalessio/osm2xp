package com.osm2xp.model.project;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Coordinates.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "latitude", "longitude" })
@XmlRootElement(name = "coordinates")
public class Coordinates {

	protected int latitude;
	protected int longitude;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public Coordinates() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public Coordinates(final int latitude, final int longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * Gets the value of the latitude property.
	 * 
	 */
	public int getLatitude() {
		return latitude;
	}

	/**
	 * Sets the value of the latitude property.
	 * 
	 */
	public void setLatitude(int value) {
		this.latitude = value;
	}

	/**
	 * Gets the value of the longitude property.
	 * 
	 */
	public int getLongitude() {
		return longitude;
	}

	/**
	 * Sets the value of the longitude property.
	 * 
	 */
	public void setLongitude(int value) {
		this.longitude = value;
	}

}
