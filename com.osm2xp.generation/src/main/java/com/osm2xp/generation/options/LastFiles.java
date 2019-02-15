package com.osm2xp.generation.options;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * LastFiles.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "osmFile" })
@XmlRootElement(name = "lastFiles")
public class LastFiles {

	@XmlElement(required = true)
	protected List<String> osmFile;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public LastFiles() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public LastFiles(final List<String> osmFile) {
		this.osmFile = osmFile;
	}

	/**
	 * Gets the value of the osmFile property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the osmFile property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getOsmFile().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link String }
	 * 
	 * 
	 */
	public List<String> getOsmFile() {
		if (osmFile == null) {
			osmFile = new ArrayList<String>();
		}
		return this.osmFile;
	}

}
