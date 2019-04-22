package com.osm2xp.gui.components;

import java.util.List;

import org.eclipse.swt.widgets.Composite;

import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.generation.options.rules.TagsRule;

public class TagsRulesTable extends AbstractTagsRulesTable {

	public TagsRulesTable(Composite parent, int style, List<? extends Object> listeTags) {
		super(parent, style, listeTags);
	}

	@Override
	protected Tag getTag(Object element)  {
		return ((TagsRule) element).getTag();
	}

}
