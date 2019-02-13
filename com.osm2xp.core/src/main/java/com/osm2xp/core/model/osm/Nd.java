package com.osm2xp.core.model.osm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Nd.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "nd")
public class Nd {

	@XmlAttribute(name = "ref", required = true)
	protected long ref;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public Nd() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public Nd(final long ref) {
		this.ref = ref;
	}

	/**
	 * Gets the value of the ref property.
	 * 
	 */
	public long getRef() {
		return ref;
	}

	/**
	 * Sets the value of the ref property.
	 * 
	 */
	public void setRef(long value) {
		this.ref = value;
	}

}
