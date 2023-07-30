package com.osm2xp.generation.options;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.osm2xp.generation.options.rules.FlightGearObjectsRulesList;
import com.osm2xp.generation.options.rules.ObjectsRulesList;

/**
 * FlightGearOptions.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "objectsRules" })
@XmlRootElement(name = "FlightGearOptions")
public class FlightGearOptions {

	@XmlElement(name = "ObjectsRules", required = true)
	protected FlightGearObjectsRulesList objectsRules;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public FlightGearOptions() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public FlightGearOptions(final FlightGearObjectsRulesList objectsRules) {

		this.objectsRules = objectsRules;

	}

	/**
	 * Gets the value of the objectsRules property.
	 * 
	 * @return possible object is {@link ObjectsRulesList }
	 * 
	 */
	public FlightGearObjectsRulesList getObjectsRules() {
		return objectsRules;
	}

	/**
	 * Sets the value of the objectsRules property.
	 * 
	 * @param value
	 *            allowed object is {@link ObjectsRulesList }
	 * 
	 */
	public void setObjectsRules(FlightGearObjectsRulesList value) {
		this.objectsRules = value;
	}

}
