package com.osm2xp.model.stats;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * GenerationStats.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "osmFile", "latitude", "longitude", "date",
		"osm2XpVersion", "buildingsNumber", "forestsNumber", "objectsNumber",
		"streetlightsNumber", "objectsStats", "forestsStats", "buildingsStats" })
@XmlRootElement(name = "GenerationStats")
public class GenerationStats {

	@XmlElement(required = true)
	protected String osmFile;
	protected int latitude;
	protected int longitude;
	@XmlElement(required = true)
	protected String date;
	@XmlElement(name = "osm2xpVersion", required = true)
	protected String osm2XpVersion;
	protected int buildingsNumber;
	protected int forestsNumber;
	protected int objectsNumber;
	protected int streetlightsNumber;
	@XmlElement(required = true)
	protected ObjectsStats objectsStats;
	@XmlElement(required = true)
	protected ForestsStats forestsStats;
	@XmlElement(required = true)
	protected BuildingsStats buildingsStats;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public GenerationStats() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public GenerationStats(final String osmFile, final int latitude,
			final int longitude, final String date, final String osm2XpVersion,
			final int buildingsNumber, final int forestsNumber,
			final int objectsNumber, final int streetlightsNumber,
			final ObjectsStats objectsStats, final ForestsStats forestsStats,
			final BuildingsStats buildingsStats) {
		this.osmFile = osmFile;
		this.latitude = latitude;
		this.longitude = longitude;
		this.date = date;
		this.osm2XpVersion = osm2XpVersion;
		this.buildingsNumber = buildingsNumber;
		this.forestsNumber = forestsNumber;
		this.objectsNumber = objectsNumber;
		this.streetlightsNumber = streetlightsNumber;
		this.objectsStats = objectsStats;
		this.forestsStats = forestsStats;
		this.buildingsStats = buildingsStats;
	}

	/**
	 * Gets the value of the osmFile property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOsmFile() {
		return osmFile;
	}

	/**
	 * Sets the value of the osmFile property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOsmFile(String value) {
		this.osmFile = value;
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

	/**
	 * Gets the value of the date property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Sets the value of the date property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDate(String value) {
		this.date = value;
	}

	/**
	 * Gets the value of the osm2XpVersion property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOsm2XpVersion() {
		return osm2XpVersion;
	}

	/**
	 * Sets the value of the osm2XpVersion property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOsm2XpVersion(String value) {
		this.osm2XpVersion = value;
	}

	/**
	 * Gets the value of the buildingsNumber property.
	 * 
	 */
	public int getBuildingsNumber() {
		return buildingsNumber;
	}

	/**
	 * Sets the value of the buildingsNumber property.
	 * 
	 */
	public void setBuildingsNumber(int value) {
		this.buildingsNumber = value;
	}

	/**
	 * Gets the value of the forestsNumber property.
	 * 
	 */
	public int getForestsNumber() {
		return forestsNumber;
	}

	/**
	 * Sets the value of the forestsNumber property.
	 * 
	 */
	public void setForestsNumber(int value) {
		this.forestsNumber = value;
	}

	/**
	 * Gets the value of the objectsNumber property.
	 * 
	 */
	public int getObjectsNumber() {
		return objectsNumber;
	}

	/**
	 * Sets the value of the objectsNumber property.
	 * 
	 */
	public void setObjectsNumber(int value) {
		this.objectsNumber = value;
	}

	/**
	 * Gets the value of the streetlightsNumber property.
	 * 
	 */
	public int getStreetlightsNumber() {
		return streetlightsNumber;
	}

	/**
	 * Sets the value of the streetlightsNumber property.
	 * 
	 */
	public void setStreetlightsNumber(int value) {
		this.streetlightsNumber = value;
	}

	/**
	 * Gets the value of the objectsStats property.
	 * 
	 * @return possible object is {@link ObjectsStats }
	 * 
	 */
	public ObjectsStats getObjectsStats() {
		return objectsStats;
	}

	/**
	 * Sets the value of the objectsStats property.
	 * 
	 * @param value
	 *            allowed object is {@link ObjectsStats }
	 * 
	 */
	public void setObjectsStats(ObjectsStats value) {
		this.objectsStats = value;
	}

	/**
	 * Gets the value of the forestsStats property.
	 * 
	 * @return possible object is {@link ForestsStats }
	 * 
	 */
	public ForestsStats getForestsStats() {
		return forestsStats;
	}

	/**
	 * Sets the value of the forestsStats property.
	 * 
	 * @param value
	 *            allowed object is {@link ForestsStats }
	 * 
	 */
	public void setForestsStats(ForestsStats value) {
		this.forestsStats = value;
	}

	/**
	 * Gets the value of the buildingsStats property.
	 * 
	 * @return possible object is {@link BuildingsStats }
	 * 
	 */
	public BuildingsStats getBuildingsStats() {
		return buildingsStats;
	}

	/**
	 * Sets the value of the buildingsStats property.
	 * 
	 * @param value
	 *            allowed object is {@link BuildingsStats }
	 * 
	 */
	public void setBuildingsStats(BuildingsStats value) {
		this.buildingsStats = value;
	}

}
