package com.osm2xp.generation.options.rules;

import java.util.List;

import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.generation.options.GlobalOptionsProvider;
import com.osm2xp.generation.osm.OsmConstants;
import com.osm2xp.utils.osm.OsmUtils;

public class RulesUtil {
	
	public static boolean areaTypeMatches(TagsRule rule, List<Tag> tags) {
		return !GlobalOptionsProvider.getOptions().isAnalyzeAreas() || rule.getAreaTypesDef().matches(OsmUtils.getTagValue(OsmConstants.LANDUSE_TAG, tags));
	}
	
}
