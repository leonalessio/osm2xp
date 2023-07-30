package com.osm2xp.model.facades;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import math.geom2d.Point2D;

public class FacadeDefinition {
	
	private Multimap<String, String> properties = HashMultimap.create();
	
	private List<Wall> walls = new ArrayList<Wall>();
	
	public void addWall(Wall currentWall) {
		walls.add(currentWall);
	}

	public List<Wall> getWalls() {
		return walls;
	}

	public void setWalls(List<Wall> walls) {
		this.walls = walls;
	}

	public Multimap<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Multimap<String, String> properties) {
		this.properties = properties;
	}
	
	public Point2D getTexSize() {
		Collection<String> strs = properties.get("TEX_SIZE");
		if (strs != null && strs.size() > 0) {
			String str = strs.iterator().next();
			String[] parts = str.split("\\s+");
			if (parts.length > 1) {
				try {
					return new Point2D(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
				} catch (NumberFormatException e) {
					// Best effort
				}
			}
			
		}
		return null;
	}

}
