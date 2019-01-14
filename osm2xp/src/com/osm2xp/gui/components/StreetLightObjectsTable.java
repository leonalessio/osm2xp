package com.osm2xp.gui.components;

import java.util.List;

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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.model.options.ObjectFile;

/**
 * StreetLightObjects
 * 
 * Table Gui table for xplane street lights objects.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class StreetLightObjectsTable extends Composite {
	private TableViewer viewer;
	private Table table;

	public StreetLightObjectsTable(Composite parent, int style) {
		super(parent, style);
		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);
		createViewer(parent);

	}

	public void updateStreetLightObjects(List<ObjectFile> input)
			throws Osm2xpBusinessException {
		if (input != null) {
			viewer.setInput(input);
			viewer.refresh();
		} else {
			throw new Osm2xpBusinessException("table input is null");
		}
	}

	private void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.FULL_SELECTION);
		createColumns(parent, viewer);

		table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setSize(500, 500);
		table.setLinesVisible(true);

		viewer.setContentProvider(new ArrayContentProvider());

		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.widthHint = 485;
		gridData.heightHint = 400;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = SWT.FILL;
		viewer.getControl().setLayoutData(gridData);

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

	private void createColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "file path" };

		TableViewerColumn colPath = createTableViewerColumn(titles[0],
				SWT.FILL, 0);
		colPath.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((ObjectFile) element).getPath();
			}
		});

		colPath.setEditingSupport(new EditingSupport(getViewer()) {
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
				return ((ObjectFile) element).getPath();
			}

			@Override
			protected void setValue(Object element, Object value) {
				((ObjectFile) element).setPath(value.toString());
				viewer.refresh();

			}
		});

	}

	private TableViewerColumn createTableViewerColumn(String title, int bound,
			final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
				SWT.NONE, colNumber);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(500);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;

	}

}
