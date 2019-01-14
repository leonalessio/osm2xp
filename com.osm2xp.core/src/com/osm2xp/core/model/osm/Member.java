package com.osm2xp.core.model.osm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Member.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "member")
public class Member {

	@XmlAttribute(name = "type", required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String type;
	@XmlAttribute(name = "ref", required = true)
	protected String ref;
	@XmlAttribute(name = "role")
	protected String role;
	private long id;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public Member() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * @param id 
	 * 
	 */
	public Member(long id, final String type, final String ref, final String role) {
		this.id = id;
		this.type = type;
		this.ref = ref;
		this.role = role;
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setType(String value) {
		this.type = value;
	}

	/**
	 * Gets the value of the ref property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRef() {
		return ref;
	}

	/**
	 * Sets the value of the ref property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRef(String value) {
		this.ref = value;
	}

	/**
	 * Gets the value of the role property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Sets the value of the role property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRole(String value) {
		this.role = value;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
