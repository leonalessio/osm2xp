package com.osm2xp.model.options;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * ObjectFile.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObjectFile", propOrder = { "path" })
public class ObjectFile {

	@XmlElement(required = true)
	protected String path;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public ObjectFile() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public ObjectFile(final String path) {
		this.path = path;
	}

	/**
	 * Gets the value of the path property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the value of the path property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPath(String value) {
		this.path = value;
	}

}
