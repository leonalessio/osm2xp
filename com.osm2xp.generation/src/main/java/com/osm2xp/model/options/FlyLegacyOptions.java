package com.osm2xp.model.options;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * FlyLegacyOptions.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "watchedTagsList" })
@XmlRootElement(name = "FlyLegacyOptions")
public class FlyLegacyOptions {

	@XmlElement(required = true)
	protected WatchedTagsList watchedTagsList;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public FlyLegacyOptions() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public FlyLegacyOptions(final WatchedTagsList watchedTagsList) {
		this.watchedTagsList = watchedTagsList;
	}

	/**
	 * Gets the value of the watchedTagsList property.
	 * 
	 * @return possible object is {@link WatchedTagsList }
	 * 
	 */
	public WatchedTagsList getWatchedTagsList() {
		return watchedTagsList;
	}

	/**
	 * Sets the value of the watchedTagsList property.
	 * 
	 * @param value
	 *            allowed object is {@link WatchedTagsList }
	 * 
	 */
	public void setWatchedTagsList(WatchedTagsList value) {
		this.watchedTagsList = value;
	}

}
