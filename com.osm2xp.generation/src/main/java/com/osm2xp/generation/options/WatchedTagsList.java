package com.osm2xp.generation.options;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.osm2xp.core.model.osm.Tag;

/**
 * WatchedTagsList.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WatchedTagsList", propOrder = { "tags" })
public class WatchedTagsList {

	@XmlElement(required = true)
	protected List<Tag> tags;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public WatchedTagsList() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public WatchedTagsList(final List<Tag> tags) {
		this.tags = tags;
	}

	/**
	 * Gets the value of the tags property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the tags property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getTags().add(newItem);
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

}
