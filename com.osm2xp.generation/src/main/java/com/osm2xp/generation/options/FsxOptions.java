package com.osm2xp.generation.options;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.osm2xp.generation.options.rules.ObjectsRulesList;

/**
 * FsxOptions.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "objectsRules", "bglCompPath" })
@XmlRootElement(name = "FsxOptions")
public class FsxOptions {

	@XmlElement(name = "ObjectsRules", required = true)
	protected ObjectsRulesList objectsRules;
	@XmlElement(required = true)
	protected String bglCompPath;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public FsxOptions() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public FsxOptions(final ObjectsRulesList objectsRules,
			final String bglCompPath) {
		this.objectsRules = objectsRules;
		this.bglCompPath = bglCompPath;
	}

	/**
	 * Gets the value of the objectsRules property.
	 * 
	 * @return possible object is {@link ObjectsRulesList }
	 * 
	 */
	public ObjectsRulesList getObjectsRules() {
		return objectsRules;
	}

	/**
	 * Sets the value of the objectsRules property.
	 * 
	 * @param value
	 *            allowed object is {@link ObjectsRulesList }
	 * 
	 */
	public void setObjectsRules(ObjectsRulesList value) {
		this.objectsRules = value;
	}

	/**
	 * Gets the value of the bglCompPath property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getBglCompPath() {
		return bglCompPath;
	}

	/**
	 * Sets the value of the bglCompPath property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setBglCompPath(String value) {
		this.bglCompPath = value;
	}

}
