package com.osm2xp.classification;

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
