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
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.generation.options.XmlHelper;
import com.osm2xp.gui.Activator;
import com.osm2xp.gui.components.FacadesExclusionsTable;
import com.osm2xp.utils.helpers.XplaneOptionsHelper;

/**
 * FacadesExclusionsPanel.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class FacadesExclusionsPanel extends Composite {

	final FacadesExclusionsTable facadesExclusionsTable;

	private static final String[] FILTER_NAMES = { "XML exclusions file (*.xml)" };
	private static final String[] FILTER_EXTS = { "*.xml" };

	public FacadesExclusionsPanel(final Composite parent, int style) {
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
				XPlaneOptionsProvider.getOptions().getBuildingsExclusions()
						.getExclusions().add(new Tag("", ""));
				facadesExclusionsTable.getViewer().refresh();
			}
		});

		ToolItem tltmDelete = new ToolItem(toolBar, SWT.NONE);
		tltmDelete.setToolTipText("delete");
		tltmDelete.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID,
				"images/toolbarsIcons/delete_16.ico"));
		tltmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) facadesExclusionsTable
						.getViewer().getSelection();
				Tag selectedTag = (Tag) selection.getFirstElement();
				XPlaneOptionsProvider.getOptions().getBuildingsExclusions()
						.getExclusions().remove(selectedTag);
				facadesExclusionsTable.getViewer().refresh();
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
								.getBuildingsExclusions(), new File(fn));
					} catch (Osm2xpBusinessException e1) {
						Osm2xpLogger.error(
								"Error saving exclusions rules to file"
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
					XplaneOptionsHelper.importExclusions(new File(fn));
					facadesExclusionsTable.updateImportedExclusionsTags();

				}
			}
		});

		Group groupTable = new Group(this, SWT.NONE);
		groupTable.setText("Osm Tags exclusions");
		GridData gd_composite = new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1);
//		gd_composite.widthHint = 523;
		groupTable.setLayoutData(gd_composite);
		groupTable.setLayout(new FillLayout(SWT.HORIZONTAL));
		facadesExclusionsTable = new FacadesExclusionsTable(groupTable,
				SWT.NONE);
	}
}
