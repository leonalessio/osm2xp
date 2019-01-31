package com.osm2xp.classification;

import com.eatthepath.jeospatial.SimpleGeospatialPoint;

public class BuildingPoint extends SimpleGeospatialPoint {

	private final BuildingData buildingData;

	public BuildingPoint(double latitude, double longitude, BuildingData buildingData) {
		super(latitude, longitude);
		this.buildingData = new BuildingData();
		this.buildingData.copyProps(buildingData);
	}

	public BuildingData getBuildingData() {
		return buildingData;
	}

}
