package com.osm2xp.model.options;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;

import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.model.osm.polygon.OsmPolygon;

/**
 * FacadeTagRule.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FacadeTagRule", propOrder = { "sizeMin", "sizeMax" })
public class FacadeTagRule extends TagsRule {

	protected int sizeMin;
	protected int sizeMax;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public FacadeTagRule() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public FacadeTagRule(final Tag tag, final List<ObjectFile> objectsFiles,
			final int sizeMin, final int sizeMax) {
		super(tag, objectsFiles);
		this.sizeMin = sizeMin;
		this.sizeMax = sizeMax;
	}

	/**
	 * Gets the value of the sizeMin property.
	 * 
	 */
	public int getSizeMin() {
		return sizeMin;
	}

	/**
	 * Sets the value of the sizeMin property.
	 * 
	 */
	public void setSizeMin(int value) {
		this.sizeMin = value;
	}

	/**
	 * Gets the value of the sizeMax property.
	 * 
	 */
	public int getSizeMax() {
		return sizeMax;
	}

	/**
	 * Sets the value of the sizeMax property.
	 * 
	 */
	public void setSizeMax(int value) {
		this.sizeMax = value;
	}

	public boolean matches(OsmPolygon osmPolygon) {
		String key = getTag().getKey();
		String value = StringUtils.stripToEmpty(getTag().getValue());
		return value.equals(osmPolygon.getTagValue(key));
	}

}
