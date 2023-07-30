package com.osm2xp.model.project;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * ObjectFactory.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlRegistry
public class ObjectFactory {

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.osm2xp.model.project
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link Osm2XpProject }
	 * 
	 */
	public Osm2XpProject createOsm2XpProject() {
		return new Osm2XpProject();
	}

	/**
	 * Create an instance of {@link CoordinatesList }
	 * 
	 */
	public CoordinatesList createCoordinatesList() {
		return new CoordinatesList();
	}

	/**
	 * Create an instance of {@link Coordinates }
	 * 
	 */
	public Coordinates createCoordinates() {
		return new Coordinates();
	}

}
