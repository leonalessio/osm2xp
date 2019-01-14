package com.osm2xp.core.model.osm;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Osm.
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "node", "way", "relation" })
@XmlRootElement(name = "osm")
public class Osm {

	@XmlElement(required = true)
	protected List<Node> node;
	@XmlElement(required = true)
	protected List<Way> way;
	@XmlElement(required = true)
	protected List<Relation> relation;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public Osm() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public Osm(final List<Node> node, final List<Way> way,
			final List<Relation> relation) {
		this.node = node;
		this.way = way;
		this.relation = relation;
	}

	/**
	 * Gets the value of the node property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the node property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getNode().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Node }
	 * 
	 * 
	 */
	public List<Node> getNode() {
		if (node == null) {
			node = new ArrayList<Node>();
		}
		return this.node;
	}

	/**
	 * Gets the value of the way property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the way property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getWay().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Way }
	 * 
	 * 
	 */
	public List<Way> getWay() {
		if (way == null) {
			way = new ArrayList<Way>();
		}
		return this.way;
	}

	/**
	 * Gets the value of the relation property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the relation property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getRelation().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Relation }
	 * 
	 * 
	 */
	public List<Relation> getRelation() {
		if (relation == null) {
			relation = new ArrayList<Relation>();
		}
		return this.relation;
	}

}
