package com.osm2xp.classification.parsing;

import java.util.List;
import java.util.function.Predicate;

import com.osm2xp.classification.BuildingData;
import com.osm2xp.classification.HeightProvider;
import com.osm2xp.classification.TypeProvider;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.core.parsers.IOSMDataVisitor;

public abstract class LearningDataCollector implements IOSMDataVisitor{
	
	private Predicate<List<Tag>> samplePredicate;

	public LearningDataCollector(Predicate<List<Tag>> samplePredicate) {
		this.samplePredicate = samplePredicate;
	}
	
	protected boolean isGoodSample(List<Tag> tags) {
		return samplePredicate.test(tags);
	}

	protected void initDataFromTags(BuildingData buildingData, List<Tag> tags) {
		buildingData.setType(TypeProvider.getBuildingType(tags));
		buildingData.setHeight(HeightProvider.getHeight(tags));
		buildingData.setLevels(HeightProvider.getLevels(tags));
	}

}
