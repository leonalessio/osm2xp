package com.osm2xp.gui.components;

import java.util.List;

import org.eclipse.swt.widgets.Composite;

import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.generation.options.rules.PolygonTagsRule;

public class PolyRulesTable extends AbstractTagsRulesTable {

	public PolyRulesTable(Composite parent, int style, List<? extends Object> items) {
		super(parent, style, items);
	}

	@Override
	protected Tag getTag(Object element) {
		return ((PolygonTagsRule) element).getTag();
	}

}
