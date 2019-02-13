package com.osm2xp.model.stats;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * ForestStat.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "forestPath", "number" })
@XmlRootElement(name = "forestStat")
public class ForestStat {

	@XmlElement(required = true)
	protected String forestPath;
	protected int number;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public ForestStat() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public ForestStat(final String forestPath, final int number) {
		this.forestPath = forestPath;
		this.number = number;
	}

	/**
	 * Gets the value of the forestPath property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getForestPath() {
		return forestPath;
	}

	/**
	 * Sets the value of the forestPath property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setForestPath(String value) {
		this.forestPath = value;
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
