package com.osm2xp.model.stats;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * ObjectsStats.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "objectStat" })
@XmlRootElement(name = "objectsStats")
public class ObjectsStats {

	@XmlElement(required = true)
	protected List<ObjectStat> objectStat;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public ObjectsStats() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public ObjectsStats(final List<ObjectStat> objectStat) {
		this.objectStat = objectStat;
	}

	/**
	 * Gets the value of the objectStat property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the objectStat property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getObjectStat().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link ObjectStat }
	 * 
	 * 
	 */
	public List<ObjectStat> getObjectStat() {
		if (objectStat == null) {
			objectStat = new ArrayList<ObjectStat>();
		}
		return this.objectStat;
	}

}
