package com.osm2xp.generation.options.rules;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.generation.options.ObjectFile;
import com.osm2xp.generation.options.Polygon;

/**
 * Tags => Polygons rules
 * 
 * @author Benjamin Blanchet
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PolygonTagRules", propOrder = { "tag", "polygons" })
public class PolygonTagsRule {

	@XmlElement(required = true)
	protected Tag tag;
	@XmlElement(name="polygon", required = true)
	protected List<Polygon> polygons;
	
	@XmlAttribute
	protected int minPerimeter = 200;

	/**
	 * Default no-arg constructor
	 * 
	 */
	public PolygonTagsRule() {
		super();
	}

	/**
	 * Fully-initialising value constructor
	 * 
	 */
	public PolygonTagsRule(final Tag tag, final List<Polygon> polygons) {
		this.tag = tag;
		this.polygons = new ArrayList<>(polygons);
	}

	/**
	 * Gets the value of the tag property.
	 * 
	 * @return possible object is {@link Tag }
	 * 
	 */
	public Tag getTag() {
		return tag;
	}

	/**
	 * Sets the value of the tag property.
	 * 
	 * @param value
	 *            allowed object is {@link Tag }
	 * 
	 */
	public void setTag(Tag value) {
		this.tag = value;
	}

	/**
	 * Gets the value of the objectsFiles property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the objectsFiles property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getPolygons().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link ObjectFile }
	 * 
	 * 
	 */
	public List<Polygon> getPolygons() {
		if (polygons == null) {
			polygons = new ArrayList<Polygon>();
		}
		return this.polygons;
	}

	public int getMinPerimeter() {
		return minPerimeter;
	}

	public void setMinPerimeter(int minPerimeter) {
		this.minPerimeter = minPerimeter;
	}

}
