package com.osm2xp.core.model.osm;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.ArrayUtils;

/**
 * Way.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "tag", "nd" })
@XmlRootElement(name = "way")
public class Way {

	protected List<Tag> tags;
	protected List<Nd> nd;
	@XmlAttribute(name = "id", required = true)
	protected long id;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public Way() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public Way(final List<Tag> tags, final List<Nd> nd, final long id) {
		this.tags = tags;
		this.nd = nd;
		this.id = id;
	}

	/**
	 * Gets the value of the tag property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the tag property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getTag().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Tag }
	 * 
	 * 
	 */
	public List<Tag> getTags() {
		if (tags == null) {
			tags = new ArrayList<Tag>();
		}
		return this.tags;
	}

	/**
	 * Gets the value of the nd property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the nd property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getNd().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Nd }
	 * 
	 * 
	 */
	public List<Nd> getNd() {
		if (nd == null) {
			nd = new ArrayList<Nd>();
		}
		return this.nd;
	}
	
	public long[] getNodesArray() {
		return ArrayUtils.toPrimitive(getNd().stream().map(nd -> nd.getRef()).toArray(Long[]::new));
	}

	/**
	 * Gets the value of the id property.
	 * 
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 */
	public void setId(long value) {
		this.id = value;
	}

}
