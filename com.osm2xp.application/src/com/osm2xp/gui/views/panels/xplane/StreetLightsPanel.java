package com.osm2xp.gui.views.panels.xplane;

import java.io.File;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.ResourceManager;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.generation.options.ObjectFile;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.generation.options.XmlHelper;
import com.osm2xp.gui.Activator;
import com.osm2xp.gui.components.AbstractPathsTable;
import com.osm2xp.gui.components.FilePathsTable;
import com.osm2xp.utils.helpers.XplaneOptionsHelper;

/**
 * StreetLightsPanel.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class StreetLightsPanel extends Composite {

	final AbstractPathsTable filesPathsTable;

	private static final String[] FILTER_NAMES = { "XML street lights objects file (*.xml)" };
	private static final String[] FILTER_EXTS = { "*.xml" };

	public StreetLightsPanel(final Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));

		ToolBar toolBar = new ToolBar(this, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1));

		ToolItem tltmAdd = new ToolItem(toolBar, SWT.NONE);
		tltmAdd.setToolTipText("add");
		tltmAdd.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID,
				"images/toolbarsIcons/add_16.ico"));
		tltmAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				XPlaneOptionsProvider.getOptions().getStreetLightObjects()
						.getObjects()
						.add(new ObjectFile("path to a light object..."));

				filesPathsTable.getViewer().refresh();
			}
		});

		ToolItem tltmDelete = new ToolItem(toolBar, SWT.NONE);
		tltmDelete.setToolTipText("delete");
		tltmDelete.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID,
				"images/toolbarsIcons/delete_16.ico"));
		tltmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) filesPathsTable
						.getViewer().getSelection();
				ObjectFile selectedFile = (ObjectFile) selection
						.getFirstElement();
				XPlaneOptionsProvider.getOptions().getStreetLightObjects()
						.getObjects().remove(selectedFile);
				filesPathsTable.getViewer().refresh();
			}
		});

		ToolItem tltmSeparator = new ToolItem(toolBar, SWT.SEPARATOR);
		tltmSeparator.setWidth(20);

		ToolItem tltmExport = new ToolItem(toolBar, SWT.NONE);
		tltmExport.setToolTipText("Export");
		tltmExport.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID,
				"images/toolbarsIcons/export_16.ico"));
		tltmExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dlg = new FileDialog(parent.getShell(), SWT.SAVE);
				dlg.setFilterNames(FILTER_NAMES);
				dlg.setFilterExtensions(FILTER_EXTS);
				String fn = dlg.open();
				if (fn != null) {
					try {
						XmlHelper.saveToXml(XPlaneOptionsProvider.getOptions()
								.getStreetLightObjects(), new File(fn));
					} catch (Osm2xpBusinessException e1) {
						Osm2xpLogger.error("Error saving rules to xml file "
								+ new File(fn).getName(), e1);
					}

				}
			}

		});

		ToolItem tltmImport = new ToolItem(toolBar, SWT.NONE);
		tltmImport.setToolTipText("Import");
		tltmImport.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID,
				"images/toolbarsIcons/import_16.ico"));
		tltmImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dlg = new FileDialog(parent.getShell(), SWT.OPEN);
				dlg.setFilterNames(FILTER_NAMES);
				dlg.setFilterExtensions(FILTER_EXTS);
				String fn = dlg.open();
				if (fn != null) {
					XplaneOptionsHelper.importStreetLightObjects(new File(fn));
					try {
						filesPathsTable.updateSelectedItem(XPlaneOptionsProvider
								.getOptions().getStreetLightObjects()
								.getObjects());
					} catch (Osm2xpBusinessException e1) {
						Osm2xpLogger.error("Error exporting rules table", e1);
					}

				}
			}
		});
		Group groupTable = new Group(this, SWT.NONE);
		groupTable.setText("Street Lights objects");
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1);
		groupTable.setLayoutData(gd_composite);
		groupTable.setLayout(new FillLayout(SWT.HORIZONTAL));
		filesPathsTable = new FilePathsTable(groupTable, SWT.NONE,
				"StreetLight object path");
		try {
			filesPathsTable.updateSelectedItem(XPlaneOptionsProvider.getOptions()
					.getStreetLightObjects().getObjects());
		} catch (Osm2xpBusinessException e1) {
			Osm2xpLogger.error("Error exporting rules table", e1);
		}

	}

}
