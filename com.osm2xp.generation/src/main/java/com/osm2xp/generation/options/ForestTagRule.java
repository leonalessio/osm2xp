package com.osm2xp.generation.options;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.osm2xp.core.model.osm.Tag;

/**
 * ForestTagRule.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ForestTagRule", propOrder = { "forestDensity" })
public class ForestTagRule extends TagsRule {

	protected int forestDensity;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public ForestTagRule() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public ForestTagRule(final Tag tag, final List<ObjectFile> objectsFiles,
			final int forestDensity) {
		super(tag, objectsFiles);
		this.forestDensity = forestDensity;
	}

	/**
	 * Gets the value of the forestDensity property.
	 * 
	 */
	public int getForestDensity() {
		return forestDensity;
	}

	/**
	 * Sets the value of the forestDensity property.
	 * 
	 */
	public void setForestDensity(int value) {
		this.forestDensity = value;
	}

}
