package com.osm2xp.generation.options;

import javax.xml.bind.annotation.XmlRegistry;

import com.osm2xp.core.model.osm.Tag;

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
	 * schema derived classes for package: com.osm2xp.model.options
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link FlyLegacyOptions }
	 * 
	 */
	public FlyLegacyOptions createFlyLegacyOptions() {
		return new FlyLegacyOptions();
	}

	/**
	 * Create an instance of {@link WatchedTagsList }
	 * 
	 */
	public WatchedTagsList createWatchedTagsList() {
		return new WatchedTagsList();
	}

	/**
	 * Create an instance of {@link XplaneOptions }
	 * 
	 */
	public XplaneOptions createXplaneOptions() {
		return new XplaneOptions();
	}

	/**
	 * Create an instance of {@linkFlightGearOptions }
	 * 
	 */
	public FlightGearOptions createFlightGearOptions() {
		return new FlightGearOptions();
	}
	
	/**
	 * Create an instance of {@link BuildingsExclusionsList }
	 * 
	 */
	public BuildingsExclusionsList createBuildingsExclusionsList() {
		return new BuildingsExclusionsList();
	}

	/**
	 * Create an instance of {@link ForestsRulesList }
	 * 
	 */
	public ForestsRulesList createForestsRulesList() {
		return new ForestsRulesList();
	}

	/**
	 * Create an instance of {@link ObjectsRulesList }
	 * 
	 */
	public ObjectsRulesList createObjectsRulesList() {
		return new ObjectsRulesList();
	}

	/**
	 * Create an instance of {@link XplaneObjectsRulesList }
	 * 
	 */
	public XplaneObjectsRulesList createXplaneObjectsRulesList() {
		return new XplaneObjectsRulesList();
	}

	/**
	 * Create an instance of {@link FacadesRulesList }
	 * 
	 */
	public FacadesRulesList createFacadesRulesList() {
		return new FacadesRulesList();
	}

	/**
	 * Create an instance of {@link ObjectsList }
	 * 
	 */
	public ObjectsList createObjectsList() {
		return new ObjectsList();
	}

	/**
	 * Create an instance of {@link GlobalOptions }
	 * 
	 */
	public GlobalOptions createGuiOptions() {
		return new GlobalOptions();
	}

	/**
	 * Create an instance of {@link FsxOptions }
	 * 
	 */
	public FsxOptions createFsxOptions() {
		return new FsxOptions();
	}

	/**
	 * Create an instance of {@link LastFiles }
	 * 
	 */
	public LastFiles createLastFiles() {
		return new LastFiles();
	}

	/**
	 * Create an instance of {@link WavefrontOptions }
	 * 
	 */
	public WavefrontOptions createWavefrontOptions() {
		return new WavefrontOptions();
	}

	/**
	 * Create an instance of {@link ObjectFile }
	 * 
	 */
	public ObjectFile createObjectFile() {
		return new ObjectFile();
	}

	/**
	 * Create an instance of {@link ForestTagRule }
	 * 
	 */
	public ForestTagRule createForestTagRule() {
		return new ForestTagRule();
	}

	/**
	 * Create an instance of {@link FacadeTagRule }
	 * 
	 */
	public FacadeTagRule createFacadeTagRule() {
		return new FacadeTagRule();
	}

	/**
	 * Create an instance of {@link TagsRule }
	 * 
	 */
	public TagsRule createTagsRules() {
		return new TagsRule();
	}

	/**
	 * Create an instance of {@link Tag }
	 * 
	 */
	public Tag createTag() {
		return new Tag();
	}

	/**
	 * Create an instance of {@link ObjectTagRule }
	 * 
	 */
	public ObjectTagRule createObjectTagRule() {
		return new ObjectTagRule();
	}

	/**
	 * Create an instance of {@link XplaneObjectTagRule }
	 * 
	 */
	public XplaneObjectTagRule createXplaneObjectTagRule() {
		return new XplaneObjectTagRule();
	}

}
