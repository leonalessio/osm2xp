package com.osm2xp.utils.geometry;

import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

public class Osm2XPGeometryFactory extends GeometryFactory {

	private static final long serialVersionUID = 1320771321884181583L;
	
	private static Osm2XPGeometryFactory instance;
	
	public static synchronized Osm2XPGeometryFactory getInstance() {
		if (instance == null) {
			instance = new Osm2XPGeometryFactory();
		}
		return instance;
	}
	
	private Osm2XPGeometryFactory() {
		super(new PrecisionModel(1000000), 0, NodeCoordinateArraySequenceFactory.instance());
	}
	
}
