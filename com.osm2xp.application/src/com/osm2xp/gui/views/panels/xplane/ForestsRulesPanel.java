package com.osm2xp.gui.views.panels.xplane;

import java.io.File;

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
import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.generation.options.ObjectFile;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.generation.options.XmlHelper;
import com.osm2xp.generation.options.rules.ForestTagRule;
import com.osm2xp.gui.Activator;
import com.osm2xp.gui.components.AbstractPathsTable;
import com.osm2xp.gui.components.FilePathsTable;
import com.osm2xp.gui.components.RulesTable;
import com.osm2xp.gui.components.TagsRulesTable;
import com.osm2xp.utils.helpers.XplaneOptionsHelper;

/**
 * ForestsRulesPanel.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class ForestsRulesPanel extends Composite {

	final RulesTable tagsTable;
	final AbstractPathsTable forestsFilesTable;
	final Group grpForestsFilesFor;
	final Group grpDensity;
	private static final String[] FILTER_NAMES = { "XML Forests rules file (*.xml)" };
	private static final String[] FILTER_EXTS = { "*.xml" };
	private ForestTagRule selectedForestTagRule;
	private Spinner spinnerForestDensity;
	private Composite compositeRuleDetail;
	private LanduseText landuseText;

	public ForestsRulesPanel(final Composite parent, int style) {
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
						.getForestsRules()
						.getRules()
						.add(new ForestTagRule(new Tag("natural", ""),
								Lists.newArrayList(new ObjectFile("xplane/forests/1.for")), 255));
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
				ForestTagRule selectedForestTagRule = (ForestTagRule) selection
						.getFirstElement();
				XPlaneOptionsProvider.getOptions().getForestsRules().getRules()
						.remove(selectedForestTagRule);
				compositeRuleDetail.setVisible(true);
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
				// export forests rules to xml file
				if (fn != null) {
					try {
						XmlHelper.saveToXml(XPlaneOptionsProvider.getOptions()
								.getForestsRules(), new File(fn));
					} catch (Osm2xpBusinessException e1) {
						Osm2xpLogger.error("Error exporting forests rules to "
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
					XplaneOptionsHelper.importForestsRules(new File(fn));
					tagsTable.updateInput(XPlaneOptionsProvider.getOptions()
							.getForestsRules().getRules());

				}
			}
		});
		new Label(this, SWT.NONE);
		Group groupTags = new Group(this, SWT.NONE);
		groupTags.setText("Forests rules - osm tags ");
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
//		gridData.heightHint = 320;
//		gridData.widthHint = 329;
		groupTags.setLayoutData(gridData);
		groupTags.setLayout(new FillLayout(SWT.HORIZONTAL));
		tagsTable = new TagsRulesTable(groupTags, SWT.NONE, XPlaneOptionsProvider
				.getOptions().getForestsRules().getRules());
		tagsTable.getTable().addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				selectedForestTagRule = (ForestTagRule) event.item.getData();
				landuseText.setRule(selectedForestTagRule);
				String selectedTag = selectedForestTagRule.getTag().getKey()
						+ "=" + selectedForestTagRule.getTag().getValue();
				grpForestsFilesFor.setText("Forests files for tag ("
						+ selectedTag + ")");
				grpDensity.setText("Forest density for tag (" + selectedTag
						+ ")");
				spinnerForestDensity.setSelection(selectedForestTagRule
						.getForestDensity());
				try {
					forestsFilesTable.updateSelectedItem(selectedForestTagRule
							.getObjectsFiles());
				} catch (Osm2xpBusinessException e) {
					Osm2xpLogger.error("Error updating rules table", e);
				}
				compositeRuleDetail.setVisible(true);

			}
		});

		compositeRuleDetail = new Composite(this, SWT.NONE);
		compositeRuleDetail.setVisible(false);
		compositeRuleDetail.setLayout(new GridLayout(1, false));
		GridData gridDataForestsObjects = new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1);
//		gridDataForestsObjects.heightHint = 347;
//		gridDataForestsObjects.widthHint = 608;
		compositeRuleDetail.setLayoutData(gridDataForestsObjects);
		
		landuseText = new LanduseText(compositeRuleDetail);		
		GridDataFactory.fillDefaults().grab(true,false).applyTo(landuseText);
		grpDensity = new Group(compositeRuleDetail, SWT.NONE);
		grpDensity.setLayout(new GridLayout(2, false));
		GridData gd_grpDensity = new GridData(SWT.FILL, SWT.BOTTOM, true,
				false, 1, 1);
//		gd_grpDensity.widthHint = 368;
		grpDensity.setLayoutData(gd_grpDensity);
		grpDensity.setText("Density ");

		Label lblDensity = new Label(grpDensity, SWT.NONE);
		lblDensity.setText("Density : ");

		spinnerForestDensity = new Spinner(grpDensity, SWT.BORDER);
		spinnerForestDensity.setMaximum(255);
		spinnerForestDensity.setMinimum(0);
		spinnerForestDensity.setToolTipText("maximum forest density 255");
		spinnerForestDensity.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				selectedForestTagRule.setForestDensity(spinnerForestDensity
						.getSelection());
			}
		});

		grpForestsFilesFor = new Group(compositeRuleDetail, SWT.NONE);
		GridData gd_grpForestsFilesFor = new GridData(SWT.FILL, SWT.BOTTOM,
				true, false, 1, 1);
		gd_grpForestsFilesFor.heightHint = 261;
		grpForestsFilesFor.setLayoutData(gd_grpForestsFilesFor);
		grpForestsFilesFor.setText("Forests files");
		grpForestsFilesFor.setLayout(new GridLayout(1, false));

		ToolBar toolBarForestFiles = new ToolBar(grpForestsFilesFor, SWT.FLAT
				| SWT.RIGHT);

		ToolItem tltmAddForestFile = new ToolItem(toolBarForestFiles, SWT.NONE);
		tltmAddForestFile.setToolTipText("add");
		tltmAddForestFile.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID,
				"images/toolbarsIcons/add_16.ico"));
		tltmAddForestFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ObjectFile file = new ObjectFile();
				file.setPath("forests/1.for");
				selectedForestTagRule.getObjectsFiles().add(file);
				forestsFilesTable.getViewer().refresh();
			}
		});

		ToolItem tltmDeleteForestFile = new ToolItem(toolBarForestFiles,
				SWT.NONE);
		tltmDeleteForestFile.setToolTipText("delete");
		tltmDeleteForestFile.setImage(ResourceManager.getPluginImage(
				Activator.PLUGIN_ID, "images/toolbarsIcons/delete_16.ico"));
		tltmDeleteForestFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				IStructuredSelection selection = (IStructuredSelection) forestsFilesTable
						.getViewer().getSelection();
				ObjectFile selectedFile = (ObjectFile) selection
						.getFirstElement();
				selectedForestTagRule.getObjectsFiles().remove(selectedFile);
				forestsFilesTable.getViewer().refresh();
			}
		});

		forestsFilesTable = new FilePathsTable(grpForestsFilesFor, SWT.NONE,
				"Forest file path");
		new Label(grpForestsFilesFor, SWT.NONE);

	}
}
