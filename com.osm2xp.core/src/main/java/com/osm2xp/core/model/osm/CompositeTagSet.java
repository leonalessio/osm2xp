package com.osm2xp.core.model.osm;

import java.util.ArrayList;
import java.util.List;

public class CompositeTagSet implements IHasTags {
	
	private List<Tag> tags;

	public CompositeTagSet(List<Tag> tags) {
		this.tags = new ArrayList<>(tags);
	}
	
	public CompositeTagSet(IHasTags tagsProvider) {
		this(tagsProvider.getTags());
	}
	
	public CompositeTagSet() {
		tags = new ArrayList<>();
	}

	public void addTags(IHasTags tagsProvider) {
		tags.addAll(tagsProvider.getTags());
	}

	@Override
	public String getTagValue(String tagKey) {
		for (Tag tag : tags) {
			if (tagKey.equals(tag.getKey())) {
				return tag.getValue();
			}
		}
		return null;
	}

	@Override
	public List<Tag> getTags() {
		return tags;
	}

}
