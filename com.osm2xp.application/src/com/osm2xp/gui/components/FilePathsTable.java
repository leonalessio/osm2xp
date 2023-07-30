package com.osm2xp.gui.components;

import org.eclipse.swt.widgets.Composite;

import com.osm2xp.generation.options.ObjectFile;

/**
 * FilePathsTable.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class FilePathsTable extends AbstractPathsTable {
	public FilePathsTable(Composite parent, int style, String fileColumnTitle) {
		super(parent, style, fileColumnTitle);
	}

	protected String getPath(Object element) {
		return ((ObjectFile) element).getPath();
	}

	protected void setPath(Object element, Object value) {
		((ObjectFile) element).setPath(value.toString());
	}

}
