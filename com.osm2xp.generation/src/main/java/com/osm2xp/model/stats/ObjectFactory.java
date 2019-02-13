package com.osm2xp.model.stats;

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
	 * schema derived classes for package: com.osm2xp.model.stats
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link ForestsStats }
	 * 
	 */
	public ForestsStats createForestsStats() {
		return new ForestsStats();
	}

	/**
	 * Create an instance of {@link ForestStat }
	 * 
	 */
	public ForestStat createForestStat() {
		return new ForestStat();
	}

	/**
	 * Create an instance of {@link GenerationStats }
	 * 
	 */
	public GenerationStats createGenerationStats() {
		return new GenerationStats();
	}

	/**
	 * Create an instance of {@link ObjectsStats }
	 * 
	 */
	public ObjectsStats createObjectsStats() {
		return new ObjectsStats();
	}

	/**
	 * Create an instance of {@link ObjectStat }
	 * 
	 */
	public ObjectStat createObjectStat() {
		return new ObjectStat();
	}

	/**
	 * Create an instance of {@link BuildingsStats }
	 * 
	 */
	public BuildingsStats createBuildingsStats() {
		return new BuildingsStats();
	}

	/**
	 * Create an instance of {@link BuildingStat }
	 * 
	 */
	public BuildingStat createBuildingStat() {
		return new BuildingStat();
	}

}
