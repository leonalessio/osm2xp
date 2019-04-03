package com.osm2xp.utils;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.generation.options.FsxOptionsProvider;
import com.osm2xp.generation.options.rules.ObjectTagRule;
import com.osm2xp.utils.osm.OsmUtils;

/**
 * BglUtils.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class BglUtils {

	/**
	 * return a random object index and the angle for the first matching rule
	 * 
	 * @param tags
	 * @return
	 */
	public static String getRandomBglGuid(List<Tag> tags, Long id) {
		for (Tag tag : tags) {
			for (ObjectTagRule objectTagRule : FsxOptionsProvider.getOptions()
					.getObjectsRules().getRules()) {
				if ((objectTagRule.getTag().getKey().equalsIgnoreCase("id") && objectTagRule
						.getTag().getValue()
						.equalsIgnoreCase(String.valueOf(id)))
						|| (OsmUtils.compareTags(objectTagRule.getTag(), tag))) {
					Random rnd = new Random();
					int i = rnd.nextInt(objectTagRule.getObjectsFiles().size());
					String result = objectTagRule.getObjectsFiles().get(i)
							.getPath();
					return result;
				}
			}
		}
		return null;
	}

}
