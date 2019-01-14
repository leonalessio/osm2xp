package com.osm2xp.core.model.osm;

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
	 * schema derived classes for package: com.osm2xp.core.model.osm
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link Member }
	 * 
	 */
	public Member createMember() {
		return new Member();
	}

	/**
	 * Create an instance of {@link Node }
	 * 
	 */
	public Node createNode() {
		return new Node();
	}

	/**
	 * Create an instance of {@link Tag }
	 * 
	 */
	public Tag createTag() {
		return new Tag();
	}

	/**
	 * Create an instance of {@link Nd }
	 * 
	 */
	public Nd createNd() {
		return new Nd();
	}

	/**
	 * Create an instance of {@link Relation }
	 * 
	 */
	public Relation createRelation() {
		return new Relation();
	}

	/**
	 * Create an instance of {@link Way }
	 * 
	 */
	public Way createWay() {
		return new Way();
	}

	/**
	 * Create an instance of {@link Osm }
	 * 
	 */
	public Osm createOsm() {
		return new Osm();
	}

}
