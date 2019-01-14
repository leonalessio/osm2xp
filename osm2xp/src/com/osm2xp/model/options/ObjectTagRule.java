package com.osm2xp.model.options;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.osm2xp.core.model.osm.Tag;

/**
 * ObjectTagRule.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObjectTagRule", propOrder = { "angle", "randomAngle" })
public class ObjectTagRule extends TagsRule {

	protected int angle;
	protected boolean randomAngle;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public ObjectTagRule() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public ObjectTagRule(final Tag tag, final List<ObjectFile> objectsFiles,
			final int angle, final boolean randomAngle) {
		super(tag, objectsFiles);
		this.angle = angle;
		this.randomAngle = randomAngle;

	}

	/**
	 * Gets the value of the angle property.
	 * 
	 */
	public int getAngle() {
		return angle;
	}

	/**
	 * Sets the value of the angle property.
	 * 
	 */
	public void setAngle(int value) {
		this.angle = value;
	}

	/**
	 * Gets the value of the randomAngle property.
	 * 
	 */
	public boolean isRandomAngle() {
		return randomAngle;
	}

	/**
	 * Sets the value of the randomAngle property.
	 * 
	 */
	public void setRandomAngle(boolean value) {
		this.randomAngle = value;
	}

}
