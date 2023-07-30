package com.osm2xp.gui.components;

import java.util.List;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.osm2xp.core.model.osm.Tag;

/**
 * AbstractTagsRulesTable
 * 
 * @author Benjamin Blanchet
 * 
 */
public abstract class AbstractTagsRulesTable extends RulesTable {
	public AbstractTagsRulesTable(Composite parent, int style,
			List<? extends Object> listeTags) {
		super(parent, style);

		GridLayout layout = new GridLayout(2, false);
		setLayout(layout);
		createViewer(listeTags);
	}

	protected String getTagKey(Object element) {
		return getTag(element).getKey();
	}

	protected void setTagKey(Object element, String key) {
		getTag(element).setKey(key);
	}

	protected abstract Tag getTag(Object element);

	protected String getTagValue(Object element) {
		return getTag(element).getValue();
	}

	protected void setTagValue(Object element, String value) {
		getTag(element).setValue(value);
	}

}
