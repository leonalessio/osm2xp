package com.osm2xp.gui.components;

import org.eclipse.swt.widgets.Composite;

import com.osm2xp.generation.options.Polygon;

public class PolyPathsTable extends AbstractPathsTable {

	public PolyPathsTable(Composite parent, int style) {
		super(parent, style, "Path");
	}

	@Override
	protected String getPath(Object element) {
		return ((Polygon) element).getPath();
	}

	@Override
	protected void setPath(Object element, Object value) {
		((Polygon) element).setPath(value.toString());
	}

}
