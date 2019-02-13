package com.osm2xp.model.facades;

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
	 * schema derived classes for package: com.osm2xp.model.facades
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link FacadeSet }
	 * 
	 */
	public FacadeSet createFacadeSet() {
		return new FacadeSet();
	}

	/**
	 * Create an instance of {@link Facade }
	 * 
	 */
	public Facade createFacade() {
		return new Facade();
	}

}
