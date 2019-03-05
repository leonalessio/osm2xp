package com.osm2xp.generation.options;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;

/**
 * GlobalOptions.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "databaseMode", "appendHour", "appendTile",
		"simplifyShapes", "currentFilePath", "levelHeight", 
		"allowedHighwayTypes","allowedHighwayLinkTypes", "allowedHighwaySurfaceTypes"})
@XmlRootElement(name = "GlobalOptions")
public class GlobalOptions {

	protected boolean databaseMode = false;
	protected boolean appendHour;
	protected boolean appendTile;
	protected boolean simplifyShapes;
	@XmlElement(required = true)
	protected String currentFilePath;
	@XmlElement(required = true)
	protected double levelHeight = 3;
//	protected boolean singlePass;
	protected String allowedHighwayTypes = "motorway;trunk;primary;secondary;tertiary;unclassified;residential";
	protected String allowedHighwayLinkTypes = "motorway_link;trunk_link;primary_link;secondary_link;tertiary_link";
	protected String allowedHighwaySurfaceTypes = "paved;asphalt;concrete";

	/**
	 * Default no-arg constructor
	 * 
	 */
	public GlobalOptions() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public GlobalOptions(final boolean databaseMode, final boolean appendHour,
			final boolean appendTile, final boolean simplifyShapes,
			final String currentFilePath) {
		this.databaseMode = databaseMode;
		this.appendHour = appendHour;
		this.appendTile = appendTile;
		this.simplifyShapes = simplifyShapes;
		this.currentFilePath = currentFilePath;
	}

	/**
	 * Gets the value of the databaseMode property.
	 * 
	 */
	public boolean isDatabaseMode() {
		return databaseMode;
	}

	/**
	 * Sets the value of the databaseMode property.
	 * 
	 */
	public void setDatabaseMode(boolean value) {
		this.databaseMode = value;
	}

	/**
	 * Gets the value of the appendHour property.
	 * 
	 */
	public boolean isAppendHour() {
		return appendHour;
	}

	/**
	 * Sets the value of the appendHour property.
	 * 
	 */
	public void setAppendHour(boolean value) {
		this.appendHour = value;
	}

	/**
	 * Gets the value of the appendTile property.
	 * 
	 */
	public boolean isAppendTile() {
		return appendTile;
	}

	/**
	 * Sets the value of the appendTile property.
	 * 
	 */
	public void setAppendTile(boolean value) {
		this.appendTile = value;
	}

	/**
	 * Gets the value of the simplifyShapes property.
	 * 
	 */
	public boolean isSimplifyShapes() {
		return simplifyShapes;
	}

	/**
	 * Sets the value of the simplifyShapes property.
	 * 
	 */
	public void setSimplifyShapes(boolean value) {
		this.simplifyShapes = value;
	}

	/**
	 * Gets the value of the currentFilePath property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCurrentFilePath() {
		return currentFilePath;
	}

	/**
	 * Sets the value of the currentFilePath property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCurrentFilePath(String value) {
		this.currentFilePath = value;
	}

	/**
	 * Gets the value of the singlePass property.
	 * 
	 */
//	public boolean isSinglePass() {
//		return singlePass;
//	}
//
//	/**
//	 * Sets the value of the singlePass property.
//	 * 
//	 */
//	public void setSinglePass(boolean value) {
//		this.singlePass = value;
//	}

	/**
	 * @return Building level height, 3m by default
	 */
	public double getLevelHeight() {
		return levelHeight;
	}
	/**
	 * @param levelHeight building level height, meters
	 */
	public void setLevelHeight(double levelHeight) {
		this.levelHeight = levelHeight;
	}

	public String getAllowedHighwayTypes() {
		return allowedHighwayTypes;
	}
	
	public String[] getAllowedHighwayTypesArray() {
		return StringUtils.stripToEmpty(allowedHighwayTypes).split(";");
	}

	public void setAllowedHighwayTypes(String allowedHighwayTypes) {
		this.allowedHighwayTypes = allowedHighwayTypes;
	}

	public String[] getAllowedHighwayLinkTypesArray() {
		return StringUtils.stripToEmpty(allowedHighwayLinkTypes).split(";");
	}
	
	public String getAllowedHighwayLinkTypes() {
		return allowedHighwayLinkTypes;
	}

	public void setAllowedHighwayLinkTypes(String allowedHighwayLinkTypes) {
		this.allowedHighwayLinkTypes = allowedHighwayLinkTypes;
	}

	public String getAllowedHighwaySurfaceTypes() {
		return allowedHighwaySurfaceTypes;
	}
	
	public String[] getAllowedHighwaySurfaceTypesArray() {
		return StringUtils.stripToEmpty(allowedHighwaySurfaceTypes).split(";");
	}

	public void setAllowedHighwaySurfaceTypes(String allowedHighwaySurfaceTypes) {
		this.allowedHighwaySurfaceTypes = allowedHighwaySurfaceTypes;
	}

}
