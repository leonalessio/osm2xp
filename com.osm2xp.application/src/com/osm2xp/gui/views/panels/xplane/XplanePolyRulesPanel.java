package com.osm2xp.gui.views.panels.xplane;

import java.io.File;
import java.text.MessageFormat;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.ResourceManager;

import com.google.common.collect.Lists;
import com.osm2xp.constants.MessagesConstants;
import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.generation.options.Polygon;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.generation.options.XmlHelper;
import com.osm2xp.generation.options.rules.PolygonTagsRule;
import com.osm2xp.gui.Activator;
import com.osm2xp.gui.components.AbstractPathsTable;
import com.osm2xp.gui.components.PolyPathsTable;
import com.osm2xp.gui.components.PolyRulesTable;
import com.osm2xp.gui.components.RulesTable;
import com.osm2xp.utils.helpers.XplaneOptionsHelper;

/**
 * XplaneObjectsRulesPanel.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class XplanePolyRulesPanel extends Composite {

	final RulesTable tagsTable;
	final AbstractPathsTable filesTable;
	final Group grpFiles;
	private static final String[] FILTER_NAMES = { "XML polygon rules file (*.xml)" };
	private static final String[] FILTER_EXTS = { "*.xml" };
	private Composite compositeRuleDetail;
	private Spinner spinnerMinPerimeter;
	protected PolygonTagsRule selectedPolygonTagsRule;

	public XplanePolyRulesPanel(final Composite parent, int style) {
		super(parent, style);

		setLayout(new GridLayout(2, false));

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
				XPlaneOptionsProvider
						.getOptions()
						.getPolygonRules()
						.getRules()
						.add(new PolygonTagsRule(new Tag("amenity",
								""), Lists.newArrayList(new Polygon())));
				tagsTable.getViewer().refresh();

			}
		});

		ToolItem tltmDelete = new ToolItem(toolBar, SWT.NONE);
		tltmDelete.setToolTipText("delete");
		tltmDelete.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID,
				"images/toolbarsIcons/delete_16.ico"));
		tltmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) tagsTable
						.getViewer().getSelection();
				PolygonTagsRule selectedPolygonTagsRule = (PolygonTagsRule) selection
						.getFirstElement();
				XPlaneOptionsProvider.getOptions().getPolygonRules().getRules()
						.remove(selectedPolygonTagsRule);
				compositeRuleDetail.setVisible(false);
				tagsTable.getViewer().refresh();
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
								.getPolygonRules(), new File(fn));
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
					XplaneOptionsHelper.importPolyRules(new File(fn));
					tagsTable.updateInput(XPlaneOptionsProvider.getOptions()
							.getPolygonRules().getRules());

				}
			}
		});
		new Label(this, SWT.NONE);

		Group groupTags = new Group(this, SWT.NONE);
		groupTags.setText("Objects rules - osm tags ");
		GridData gridData = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 2);
//		gridData.heightHint = 320;
//		gridData.widthHint = 329;
		groupTags.setLayoutData(gridData);
		groupTags.setLayout(new FillLayout(SWT.HORIZONTAL));
		tagsTable = new PolyRulesTable(groupTags, SWT.NONE, XPlaneOptionsProvider
				.getOptions().getPolygonRules().getRules());
		tagsTable.setLayout(new FillLayout(SWT.HORIZONTAL));
		tagsTable.getTable().addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				selectedPolygonTagsRule = (PolygonTagsRule) event.item
						.getData();
				updateRuleControls();
			}
		});

		compositeRuleDetail = new Composite(this, SWT.NONE);
		compositeRuleDetail.setVisible(true);
		compositeRuleDetail.setLayout(new GridLayout(2, false));
		GridData gridDataObjectsObjects = new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1);
		// gridDataObjectsObjects.heightHint = 150;
//		gridDataObjectsObjects.widthHint = 608;
		compositeRuleDetail.setLayoutData(gridDataObjectsObjects);

		Label lblNewLabel_3 = new Label(compositeRuleDetail, SWT.NONE);
		lblNewLabel_3.setText("Min perimeter, m: ");
		lblNewLabel_3.setToolTipText("Specify minimal perimeter for generating draped polygon for.");
		GridDataFactory.swtDefaults().applyTo(lblNewLabel_3);
		
		spinnerMinPerimeter = new Spinner(compositeRuleDetail, SWT.BORDER);
		spinnerMinPerimeter.setMaximum(5000);
		spinnerMinPerimeter.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				selectedPolygonTagsRule.setMinPerimeter(spinnerMinPerimeter
						.getSelection());
			}
		});
		spinnerMinPerimeter.setTextLimit(5);
		GridDataFactory.swtDefaults().applyTo(spinnerMinPerimeter);
		
		grpFiles = new Group(compositeRuleDetail, SWT.NONE);
		grpFiles.setLayout(new GridLayout(1, false));
		GridDataFactory.fillDefaults().grab(true,true).span(2,1).applyTo(grpFiles);

		ToolBar toolBarPolygons = new ToolBar(grpFiles, SWT.FLAT | SWT.RIGHT);
		GridDataFactory.swtDefaults().grab(true,false).applyTo(toolBarPolygons);

		ToolItem tltmAddPolygon = new ToolItem(toolBarPolygons, SWT.NONE);
		tltmAddPolygon.setToolTipText("add");
		tltmAddPolygon.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID,
				"images/toolbarsIcons/add_16.ico"));
		tltmAddPolygon.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Polygon file = new Polygon();
//				file.setPath("SomePathTo" + File.separator + "aPolygon.fac");
				selectedPolygonTagsRule.getPolygons().add(file);
				filesTable.getViewer().refresh();
			}
		});

		ToolItem tltmDeletePolygon = new ToolItem(toolBarPolygons,
				SWT.NONE);
		tltmDeletePolygon.setToolTipText("delete");
		tltmDeletePolygon.setImage(ResourceManager.getPluginImage(
				Activator.PLUGIN_ID, "images/toolbarsIcons/delete_16.ico"));
		tltmDeletePolygon.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				IStructuredSelection selection = (IStructuredSelection) filesTable
						.getViewer().getSelection();
				Polygon selectedFile = (Polygon) selection
						.getFirstElement();
				selectedPolygonTagsRule.getPolygons().remove(
						selectedFile);
				filesTable.getViewer().refresh();
			}
		});

		
		filesTable = new PolyPathsTable(grpFiles, SWT.NONE);
//		filesTable.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridDataFactory.fillDefaults().grab(true,true).applyTo(filesTable);
		
	}

	private void updateRuleControls() {

		String selectedTag = selectedPolygonTagsRule.getTag().getKey()
				+ "=" + selectedPolygonTagsRule.getTag().getValue();
		grpFiles.setText(MessageFormat.format(
				MessagesConstants.LABEL_FILES_OBJECT_RULE, selectedTag));
		
		spinnerMinPerimeter.setSelection(selectedPolygonTagsRule.getMinPerimeter());
		try {
			filesTable.updateSelectedItem(selectedPolygonTagsRule
					.getPolygons());
		} catch (Osm2xpBusinessException e) {
			Osm2xpLogger.error("Error updating rules table", e);
		}
		compositeRuleDetail.setVisible(true);

	}
}
