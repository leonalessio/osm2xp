package com.osm2xp.model.stats;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * ObjectStat.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "objectPath", "number" })
@XmlRootElement(name = "objectStat")
public class ObjectStat {

	@XmlElement(required = true)
	protected String objectPath;
	protected int number;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public ObjectStat() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public ObjectStat(final String objectPath, final int number) {
		this.objectPath = objectPath;
		this.number = number;
	}

	/**
	 * Gets the value of the objectPath property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getObjectPath() {
		return objectPath;
	}

	/**
	 * Sets the value of the objectPath property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setObjectPath(String value) {
		this.objectPath = value;
	}

	/**
	 * Gets the value of the number property.
	 * 
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Sets the value of the number property.
	 * 
	 */
	public void setNumber(int value) {
		this.number = value;
	}

}
