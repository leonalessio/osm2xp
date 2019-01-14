package com.osm2xp.utils;

import java.util.Collections;
import java.util.List;

import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.model.options.ObjectTagRule;
import com.osm2xp.utils.helpers.FsxOptionsHelper;

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
			for (ObjectTagRule objectTagRule : FsxOptionsHelper.getOptions()
					.getObjectsRules().getRules()) {
				if ((objectTagRule.getTag().getKey().equalsIgnoreCase("id") && objectTagRule
						.getTag().getValue()
						.equalsIgnoreCase(String.valueOf(id)))
						|| (OsmUtils.compareTags(objectTagRule.getTag(), tag))) {
					Collections.shuffle(objectTagRule.getObjectsFiles());
					String result = objectTagRule.getObjectsFiles().get(0)
							.getPath();
					return result;
				}
			}
		}
		return null;
	}

}
