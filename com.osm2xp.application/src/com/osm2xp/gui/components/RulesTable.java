package com.osm2xp.gui.components;

import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public abstract class RulesTable extends Composite {

	private static final int DEFAULT_ITEMS = 10;
	private TableViewer viewer;
	private Table table;

	public RulesTable(Composite parent, int style) {
		super(parent, style);
	}

	public void updateInput(List<? extends Object> tagsList) {
		viewer.setInput(tagsList);
		viewer.refresh();
	}

	protected void createViewer(List<? extends Object> items) {
		viewer = new TableViewer(this, SWT.FULL_SELECTION);
		createColumns(viewer);
	
		table = viewer.getTable();
		table.setHeaderVisible(true);
//		table.setSize(300, 500);
		table.setLinesVisible(true);
	
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setInput(items);
		
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.heightHint = table.getHeaderHeight() + table.getItemHeight() * DEFAULT_ITEMS;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		GridDataFactory.fillDefaults().grab(true, true).applyTo(viewer.getControl());
	
		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(
				viewer) {
			protected boolean isEditorActivationEvent(
					ColumnViewerEditorActivationEvent event) {
				return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
						|| event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION
						|| event.eventType == ColumnViewerEditorActivationEvent.PROGRAMMATIC;
			}
		};
	
		TableViewerEditor.create(viewer, actSupport,
				ColumnViewerEditor.TABBING_HORIZONTAL
						| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
						| ColumnViewerEditor.TABBING_VERTICAL
						| ColumnViewerEditor.KEYBOARD_ACTIVATION);
	
	}

	public TableViewer getViewer() {
		return viewer;
	}

	private void createColumns(final TableViewer viewer) {
		String[] titles = { "key", "value" };
	
		/**
		 * KEY COLUMN
		 */
		TableViewerColumn colKey = createTableViewerColumn(titles[0], SWT.LEFT,
				0);
		colKey.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return getKey(element);
			}
		});
	
		colKey.setEditingSupport(new EditingSupport(getViewer()) {
			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
	
			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor((Table) getViewer().getControl());
			}
	
			@Override
			protected Object getValue(Object element) {
				return getKey(element);
			}
	
			@Override
			protected void setValue(Object element, Object value) {
				String str = (String)value;
				setKey(element, str);
				getViewer().refresh();
			}
		});
	
		/**
		 * VALUE COLUMN
		 */
		TableViewerColumn colValue = createTableViewerColumn(titles[1],
				SWT.RIGHT, 1);
		colValue.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return getValue(element);
			}
		});
	
		colValue.setEditingSupport(new EditingSupport(getViewer()) {
			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
	
			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor((Table) getViewer().getControl());
			}
	
			@Override
			protected Object getValue(Object element) {
				return getValue(element);
			}
	
			@Override
			protected void setValue(Object element, Object value) {
				String str = (String) value;
				setValue(element, str);
				getViewer().refresh();
			}
		});
	
	}

	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
				SWT.NONE, colNumber);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(150);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	
	}

	public Table getTable() {
		return table;
	}
	
	protected abstract String getKey(Object element);

	protected abstract void setKey(Object element, String key);

	protected abstract String getValue(Object element);

	protected abstract void setValue(Object element, String value);

}