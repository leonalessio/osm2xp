package com.osm2xp.generation.options;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Polygon", propOrder = { "path" })
public class Polygon {
	
	@XmlAttribute
	protected String path = "";

	public Polygon(String path) {
		super();
		this.path = path;
	}
	
	public Polygon() {
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
