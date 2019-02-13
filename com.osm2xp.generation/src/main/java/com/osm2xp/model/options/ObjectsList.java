package com.osm2xp.model.options;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * ObjectsList.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ObjectsList", propOrder = { "objects" })
public class ObjectsList {

	@XmlElement(required = true)
	protected List<ObjectFile> objects;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public ObjectsList() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public ObjectsList(final List<ObjectFile> objects) {
		this.objects = objects;
	}

	/**
	 * Gets the value of the objects property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the objects property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getObjects().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link ObjectFile }
	 * 
	 * 
	 */
	public List<ObjectFile> getObjects() {
		if (objects == null) {
			objects = new ArrayList<ObjectFile>();
		}
		return this.objects;
	}

}
