package com.osm2xp.model.stats;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * ForestsStats.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "forestStat" })
@XmlRootElement(name = "forestsStats")
public class ForestsStats {

	@XmlElement(required = true)
	protected List<ForestStat> forestStat;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public ForestsStats() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public ForestsStats(final List<ForestStat> forestStat) {
		this.forestStat = forestStat;
	}

	/**
	 * Gets the value of the forestStat property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the forestStat property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getForestStat().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link ForestStat }
	 * 
	 * 
	 */
	public List<ForestStat> getForestStat() {
		if (forestStat == null) {
			forestStat = new ArrayList<ForestStat>();
		}
		return this.forestStat;
	}

}
