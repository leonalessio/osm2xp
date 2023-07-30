package com.osm2xp.model.stats;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * BuildingsStats.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "buildingStat" })
@XmlRootElement(name = "buildingsStats")
public class BuildingsStats {

	@XmlElement(required = true)
	protected List<BuildingStat> buildingStat;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public BuildingsStats() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public BuildingsStats(final List<BuildingStat> buildingStat) {
		this.buildingStat = buildingStat;
	}

	/**
	 * Gets the value of the buildingStat property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the buildingStat property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getBuildingStat().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link BuildingStat }
	 * 
	 * 
	 */
	public List<BuildingStat> getBuildingStat() {
		if (buildingStat == null) {
			buildingStat = new ArrayList<BuildingStat>();
		}
		return this.buildingStat;
	}

}
