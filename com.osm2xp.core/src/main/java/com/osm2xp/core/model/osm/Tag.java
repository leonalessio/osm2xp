package com.osm2xp.core.model.osm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Tag.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "tag")
public class Tag {

	@Override
	public String toString() {
		return "Tag[key=" + key + ", value=" + value + "]";
	}

	@XmlAttribute(name = "value", required = true)
	protected String value;
	@XmlAttribute(name = "key", required = true)
	protected String key;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public Tag() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public Tag(final String key, final String value) {
		this.key = key;
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	// @Override
	// public boolean equals(Object obj) {
	// Boolean result = false;
	// if (obj instanceof Tag) {
	// Tag anotherTag = (Tag) obj;
	// result = this.key.equalsIgnoreCase(anotherTag.key)
	// && this.value.equalsIgnoreCase(anotherTag.value);
	// }
	// return result;
	// }

}
