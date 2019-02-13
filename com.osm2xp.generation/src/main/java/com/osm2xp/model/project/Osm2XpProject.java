package com.osm2xp.model.project;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Osm2XpProject.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "file", "coordinatesList" })
@XmlRootElement(name = "osm2xpProject")
public class Osm2XpProject {

	@XmlElement(required = true)
	protected String file;
	@XmlElement(required = true)
	protected CoordinatesList coordinatesList;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public Osm2XpProject() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public Osm2XpProject(final String file,
			final CoordinatesList coordinatesList) {
		this.file = file;
		this.coordinatesList = coordinatesList;
	}

	/**
	 * Gets the value of the file property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFile() {
		return file;
	}

	/**
	 * Sets the value of the file property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFile(String value) {
		this.file = value;
	}

	/**
	 * Gets the value of the coordinatesList property.
	 * 
	 * @return possible object is {@link CoordinatesList }
	 * 
	 */
	public CoordinatesList getCoordinatesList() {
		return coordinatesList;
	}

	/**
	 * Sets the value of the coordinatesList property.
	 * 
	 * @param value
	 *            allowed object is {@link CoordinatesList }
	 * 
	 */
	public void setCoordinatesList(CoordinatesList value) {
		this.coordinatesList = value;
	}

}
