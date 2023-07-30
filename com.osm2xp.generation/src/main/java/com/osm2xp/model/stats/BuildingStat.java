package com.osm2xp.model.stats;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * BuildingStat.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "type", "number" })
@XmlRootElement(name = "buildingStat")
public class BuildingStat {

	@XmlElement(required = true)
	protected String type;
	protected int number;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public BuildingStat() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public BuildingStat(final String type, final int number) {
		this.type = type;
		this.number = number;
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setType(String value) {
		this.type = value;
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
