package com.osm2xp.classification;

import java.util.List;

import com.onpositive.classification.core.util.TagUtil;
import com.osm2xp.core.model.osm.Tag;

public class HeightProvider {
	
	public static int getHeight(List<Tag> tags) {
		String htVal = TagUtil.getValue("height", tags);
		if (htVal != null) {
			try {
				return (int) Math.round(Double.parseDouble(htVal));
			} catch (NumberFormatException e) {
				// Ignore
			}
		}
		return 0;
	}

	public static int getLevels(List<Tag> tags) {
		String levelsVal = TagUtil.getValue("building:levels", tags);
		if (levelsVal != null) {
			try {
				return (int) Math.round(Double.parseDouble(levelsVal));
			} catch (NumberFormatException e) {
				// Ignore
			}
		}
		return 0;
	}
}
