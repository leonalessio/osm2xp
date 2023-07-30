package com.osm2xp.gui.components;

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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.generation.options.XPlaneOptionsProvider;

/**
 * FacadesExclusionsTable
 * 
 * @author Benjamin Blanchet
 * 
 */
public class FacadesExclusionsTable extends Composite {
	private TableViewer viewer;
	private Table table;

	public FacadesExclusionsTable(Composite parent, int style) {
		super(parent, style);

		GridLayout layout = new GridLayout(2, false);
		setLayout(layout);
		createViewer(this);

	}

	public void updateImportedExclusionsTags() {
		viewer.setInput(XPlaneOptionsProvider.getOptions()
				.getBuildingsExclusions().getExclusions());
		viewer.refresh();
	}

	private void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.FULL_SELECTION);
		createColumns(parent, viewer);

		table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
//		table.setSize(500, 500);

		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setInput(XPlaneOptionsProvider.getOptions()
				.getBuildingsExclusions().getExclusions());

//		GridData gridData = new GridData();
//		gridData.verticalAlignment = GridData.FILL;
//		gridData.widthHint = 485;
//		gridData.heightHint = 200;
//		gridData.grabExcessVerticalSpace = true;
//		gridData.horizontalAlignment = GridData.FILL;
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

	// This will create the columns for the table
	private void createColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "key", "value" };

		/**
		 * KEY COLUMN
		 */
		TableViewerColumn colKey = createTableViewerColumn(titles[0], SWT.LEFT,
				0);
		colKey.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				Tag p = (Tag) element;
				return p.getKey();
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
				return ((Tag) element).getKey();
			}

			@Override
			protected void setValue(Object element, Object value) {
				((Tag) element).setKey((String) value);
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
				Tag p = (Tag) element;
				return p.getValue();
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
				return ((Tag) element).getValue();
			}

			@Override
			protected void setValue(Object element, Object value) {
				((Tag) element).setValue((String) value);
				getViewer().refresh();
			}
		});

	}

	private TableViewerColumn createTableViewerColumn(String title, int bound,
			final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
				SWT.NONE, colNumber);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(250);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;

	}

}
