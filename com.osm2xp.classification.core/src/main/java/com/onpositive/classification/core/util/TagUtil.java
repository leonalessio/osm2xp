package com.onpositive.classification.core.util;

import java.util.List;

import com.osm2xp.core.model.osm.Tag;

public class TagUtil {
	public static String getValue(String key, List<Tag> tags) {
		for (Tag tag : tags) {
			if (key.equalsIgnoreCase(tag.getKey())) {
				return tag.getValue();
			}
		}
		return null;
	}
}
