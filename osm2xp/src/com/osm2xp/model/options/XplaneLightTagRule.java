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
@XmlType(name = "XplaneLightTagRule", propOrder = { "height", "offset",
		"percentage" })
public class XplaneLightTagRule extends TagsRule {

	protected int height;
	protected int offset;
	protected int percentage;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public XplaneLightTagRule() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public XplaneLightTagRule(final Tag tag,
			final List<ObjectFile> objectsFiles, final int height,
			final int offset, final int percentage) {
		super(tag, objectsFiles);
		this.height = height;
		this.offset = offset;
		this.percentage = percentage;

	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

}
