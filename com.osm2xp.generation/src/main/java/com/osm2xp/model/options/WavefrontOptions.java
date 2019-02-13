package com.osm2xp.model.options;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * WavefrontOptions.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "waveFrontExportHeightFilter",
		"waveFrontExportPointsFilter", "waveFrontExportSingleObject",
		"generateWaveFrontDsfPlaceholder" })
@XmlRootElement(name = "WavefrontOptions")
public class WavefrontOptions {

	protected int waveFrontExportHeightFilter;
	protected int waveFrontExportPointsFilter;
	protected boolean waveFrontExportSingleObject;
	protected boolean generateWaveFrontDsfPlaceholder;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public WavefrontOptions() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public WavefrontOptions(final int waveFrontExportHeightFilter,
			final int waveFrontExportPointsFilter,
			final boolean waveFrontExportSingleObject,
			final boolean generateWaveFrontDsfPlaceholder) {
		this.waveFrontExportHeightFilter = waveFrontExportHeightFilter;
		this.waveFrontExportPointsFilter = waveFrontExportPointsFilter;
		this.waveFrontExportSingleObject = waveFrontExportSingleObject;
		this.generateWaveFrontDsfPlaceholder = generateWaveFrontDsfPlaceholder;
	}

	/**
	 * Gets the value of the waveFrontExportHeightFilter property.
	 * 
	 */
	public int getWaveFrontExportHeightFilter() {
		return waveFrontExportHeightFilter;
	}

	/**
	 * Sets the value of the waveFrontExportHeightFilter property.
	 * 
	 */
	public void setWaveFrontExportHeightFilter(int value) {
		this.waveFrontExportHeightFilter = value;
	}

	/**
	 * Gets the value of the waveFrontExportPointsFilter property.
	 * 
	 */
	public int getWaveFrontExportPointsFilter() {
		return waveFrontExportPointsFilter;
	}

	/**
	 * Sets the value of the waveFrontExportPointsFilter property.
	 * 
	 */
	public void setWaveFrontExportPointsFilter(int value) {
		this.waveFrontExportPointsFilter = value;
	}

	/**
	 * Gets the value of the waveFrontExportSingleObject property.
	 * 
	 */
	public boolean isWaveFrontExportSingleObject() {
		return waveFrontExportSingleObject;
	}

	/**
	 * Sets the value of the waveFrontExportSingleObject property.
	 * 
	 */
	public void setWaveFrontExportSingleObject(boolean value) {
		this.waveFrontExportSingleObject = value;
	}

	/**
	 * Gets the value of the generateWaveFrontDsfPlaceholder property.
	 * 
	 */
	public boolean isGenerateWaveFrontDsfPlaceholder() {
		return generateWaveFrontDsfPlaceholder;
	}

	/**
	 * Sets the value of the generateWaveFrontDsfPlaceholder property.
	 * 
	 */
	public void setGenerateWaveFrontDsfPlaceholder(boolean value) {
		this.generateWaveFrontDsfPlaceholder = value;
	}

}
