package com.osm2xp.model.project;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * CoordinatesList.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "coordinates" })
@XmlRootElement(name = "coordinatesList")
public class CoordinatesList {

	@XmlElement(required = true)
	protected List<Coordinates> coordinates;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public CoordinatesList() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public CoordinatesList(final List<Coordinates> coordinates) {
		this.coordinates = coordinates;
	}

	/**
	 * Gets the value of the coordinates property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the coordinates property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getCoordinates().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link Coordinates }
	 * 
	 * 
	 */
	public List<Coordinates> getCoordinates() {
		if (coordinates == null) {
			coordinates = new ArrayList<Coordinates>();
		}
		return this.coordinates;
	}

}
