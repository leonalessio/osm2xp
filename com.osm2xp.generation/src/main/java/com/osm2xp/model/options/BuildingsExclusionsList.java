package com.osm2xp.model.options;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.osm2xp.core.model.osm.Tag;

/**
 * BuildingsExclusionsList.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BuildingsExclusionsList", propOrder = { "exclusions" })
public class BuildingsExclusionsList {

	@XmlElement(required = true)
	protected List<Tag> exclusions;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public BuildingsExclusionsList() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public BuildingsExclusionsList(final List<Tag> exclusions) {
		this.exclusions = exclusions;
	}

	/**
	 * Gets the value of the exclusions property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the exclusions property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getExclusions().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Tag }
	 * 
	 * 
	 */
	public List<Tag> getExclusions() {
		if (exclusions == null) {
			exclusions = new ArrayList<Tag>();
		}
		return this.exclusions;
	}

}
