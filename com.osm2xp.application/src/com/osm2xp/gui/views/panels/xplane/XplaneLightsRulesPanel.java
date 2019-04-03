package com.osm2xp.gui.views.panels.xplane;

import java.io.File;
import java.text.MessageFormat;

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
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.ResourceManager;

import com.google.common.collect.Lists;
import com.osm2xp.constants.MessagesConstants;
import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.generation.options.ObjectFile;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.generation.options.XmlHelper;
import com.osm2xp.generation.options.rules.XplaneLightTagRule;
import com.osm2xp.gui.components.FilesPathsTable;
import com.osm2xp.gui.components.TagsRulesTable;
import com.osm2xp.utils.helpers.XplaneOptionsHelper;

/**
 * XplaneLightsRulesPanel.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class XplaneLightsRulesPanel extends Composite {

	final TagsRulesTable tagsTable;
	final FilesPathsTable ObjectsFilesTable;
	final Group grpFiles;
	private static final String[] FILTER_NAMES = { "XML lights rules file (*.xml)" };
	private static final String[] FILTER_EXTS = { "*.xml" };
	private XplaneLightTagRule selectedXplaneLightTagRule;
	private Composite compositeRuleDetail;
	private Spinner spinnerHeight;
	private Spinner spinnerOffset;
	private Spinner spinnerPercentage;

	public XplaneLightsRulesPanel(final Composite parent, int style) {
		super(parent, style);

		setLayout(new GridLayout(2, false));

		ToolBar toolBar = new ToolBar(this, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1));

		ToolItem tltmAdd = new ToolItem(toolBar, SWT.NONE);
		tltmAdd.setToolTipText("add");
		tltmAdd.setImage(ResourceManager.getPluginImage("com.osm2xp",
				"images/toolbarsIcons/add_16.ico"));
		tltmAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				XPlaneOptionsProvider
						.getOptions()
						.getLightsRules()
						.getRules()
						.add(new XplaneLightTagRule(new Tag("a tag key",
								"a tag value"),Lists.newArrayList(new ObjectFile(
										"the path to a light Object file")), 5, 0, 20));
				tagsTable.getViewer().refresh();

			}
		});

		ToolItem tltmDelete = new ToolItem(toolBar, SWT.NONE);
		tltmDelete.setToolTipText("delete");
		tltmDelete.setImage(ResourceManager.getPluginImage("com.osm2xp",
				"images/toolbarsIcons/delete_16.ico"));
		tltmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) tagsTable
						.getViewer().getSelection();
				XplaneLightTagRule selectedXplaneLightTagRule = (XplaneLightTagRule) selection
						.getFirstElement();
				XPlaneOptionsProvider.getOptions().getLightsRules().getRules()
						.remove(selectedXplaneLightTagRule);
				compositeRuleDetail.setVisible(false);
				tagsTable.getViewer().refresh();
			}
		});

		ToolItem tltmSeparator = new ToolItem(toolBar, SWT.SEPARATOR);
		tltmSeparator.setWidth(20);

		ToolItem tltmExport = new ToolItem(toolBar, SWT.NONE);
		tltmExport.setToolTipText("Export");
		tltmExport.setImage(ResourceManager.getPluginImage("com.osm2xp",
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
								.getLightsRules(), new File(fn));
					} catch (Osm2xpBusinessException e1) {
						Osm2xpLogger.error("Error saving rules to xml file "
								+ new File(fn).getName(), e1);
					}

				}
			}

		});

		ToolItem tltmImport = new ToolItem(toolBar, SWT.NONE);
		tltmImport.setToolTipText("Import");
		tltmImport.setImage(ResourceManager.getPluginImage("com.osm2xp",
				"images/toolbarsIcons/import_16.ico"));
		tltmImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dlg = new FileDialog(parent.getShell(), SWT.OPEN);
				dlg.setFilterNames(FILTER_NAMES);
				dlg.setFilterExtensions(FILTER_EXTS);
				String fn = dlg.open();
				if (fn != null) {
					XplaneOptionsHelper.importObjectsRules(new File(fn));
					tagsTable.updateInput(XPlaneOptionsProvider.getOptions()
							.getLightsRules().getRules());

				}
			}
		});
		new Label(this, SWT.NONE);

		Group groupTags = new Group(this, SWT.NONE);
		groupTags.setText("Objects rules - osm tags ");
		GridData gridData = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gridData.heightHint = 320;
		gridData.widthHint = 329;
		groupTags.setLayoutData(gridData);
		groupTags.setLayout(new FillLayout(SWT.HORIZONTAL));
		tagsTable = new TagsRulesTable(groupTags, SWT.NONE, XPlaneOptionsProvider
				.getOptions().getLightsRules().getRules());
		tagsTable.setLayout(new FillLayout(SWT.HORIZONTAL));
		tagsTable.getTable().addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				selectedXplaneLightTagRule = (XplaneLightTagRule) event.item
						.getData();
				updateRuleControls();
			}
		});

		compositeRuleDetail = new Composite(this, SWT.NONE);
		compositeRuleDetail.setVisible(true);
		compositeRuleDetail.setLayout(new GridLayout(1, false));
		GridData gridDataObjectsObjects = new GridData(SWT.FILL, SWT.TOP,
				false, false, 1, 1);
		// gridDataObjectsObjects.heightHint = 150;
		gridDataObjectsObjects.grabExcessVerticalSpace = true;
		gridDataObjectsObjects.widthHint = 608;
		compositeRuleDetail.setLayoutData(gridDataObjectsObjects);

		TabFolder tabFolder = new TabFolder(compositeRuleDetail, SWT.NONE);
		GridData gd_tabFolder = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_tabFolder.heightHint = 300;
		tabFolder.setLayoutData(gd_tabFolder);

		TabItem tabObjects = new TabItem(tabFolder, SWT.NONE);
		tabObjects.setText("3D objects");

		grpFiles = new Group(tabFolder, SWT.NONE);
		tabObjects.setControl(grpFiles);
		grpFiles.setLayout(new GridLayout(1, false));

		ToolBar toolBarObjectFiles = new ToolBar(grpFiles, SWT.FLAT | SWT.RIGHT);

		ToolItem tltmAddObjectFile = new ToolItem(toolBarObjectFiles, SWT.NONE);
		tltmAddObjectFile.setToolTipText("add");
		tltmAddObjectFile.setImage(ResourceManager.getPluginImage("com.osm2xp",
				"images/toolbarsIcons/add_16.ico"));
		tltmAddObjectFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ObjectFile file = new ObjectFile();
				file.setPath("SomePathTo" + File.separator + "aObjectFile.fac");
				selectedXplaneLightTagRule.getObjectsFiles().add(file);
				ObjectsFilesTable.getViewer().refresh();
			}
		});

		ToolItem tltmDeleteObjectFile = new ToolItem(toolBarObjectFiles,
				SWT.NONE);
		tltmDeleteObjectFile.setToolTipText("delete");
		tltmDeleteObjectFile.setImage(ResourceManager.getPluginImage(
				"com.osm2xp", "images/toolbarsIcons/delete_16.ico"));
		tltmDeleteObjectFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				IStructuredSelection selection = (IStructuredSelection) ObjectsFilesTable
						.getViewer().getSelection();
				ObjectFile selectedFile = (ObjectFile) selection
						.getFirstElement();
				selectedXplaneLightTagRule.getObjectsFiles().remove(
						selectedFile);
				ObjectsFilesTable.getViewer().refresh();
			}
		});

		ObjectsFilesTable = new FilesPathsTable(grpFiles, SWT.NONE,
				"Object file path");
		ObjectsFilesTable.setLayout(new FillLayout(SWT.HORIZONTAL));
		new Label(grpFiles, SWT.NONE);

		TabItem tabOptions = new TabItem(tabFolder, SWT.NONE);
		tabOptions.setText("Options");

		Composite compositeOptions = new Composite(tabFolder, SWT.NONE);
		tabOptions.setControl(compositeOptions);
		compositeOptions.setLayout(new GridLayout(1, false));

		Group grpOptions = new Group(compositeOptions, SWT.NONE);
		grpOptions.setSize(316, 90);
		grpOptions.setLayout(new GridLayout(2, false));
		GridData gd_grpAngle = new GridData(SWT.LEFT, SWT.TOP, false, true, 1,
				1);
		gd_grpAngle.heightHint = 107;
		gd_grpAngle.widthHint = 373;
		grpOptions.setLayoutData(gd_grpAngle);
		grpOptions.setText("angle");

		Label labelHeight = new Label(grpOptions, SWT.NONE);
		labelHeight.setText("Height");

		spinnerHeight = new Spinner(grpOptions, SWT.BORDER);
		spinnerHeight.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				selectedXplaneLightTagRule.setHeight(spinnerHeight
						.getSelection());
			}
		});

		Label labelOffset = new Label(grpOptions, SWT.NONE);
		labelOffset.setText("offset");
		spinnerOffset = new Spinner(grpOptions, SWT.BORDER);
		spinnerOffset.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				selectedXplaneLightTagRule.setOffset(spinnerOffset
						.getSelection());
			}
		});

		Label lblPercentage = new Label(grpOptions, SWT.NONE);
		lblPercentage.setText("Percentage");

		spinnerPercentage = new Spinner(grpOptions, SWT.BORDER);
		spinnerPercentage.setMaximum(100);
		spinnerPercentage.setMinimum(0);
		spinnerPercentage.setIncrement(1);
		spinnerPercentage.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				selectedXplaneLightTagRule.setPercentage(spinnerPercentage
						.getSelection());
			}
		});

	}

	private void updateRuleControls() {

		String selectedTag = selectedXplaneLightTagRule.getTag().getKey() + "="
				+ selectedXplaneLightTagRule.getTag().getValue();
		grpFiles.setText(MessageFormat.format(
				MessagesConstants.LABEL_FILES_OBJECT_RULE, selectedTag));
		spinnerHeight.setSelection(selectedXplaneLightTagRule.getHeight());
		spinnerOffset.setSelection(selectedXplaneLightTagRule.getOffset());
		spinnerPercentage.setSelection(selectedXplaneLightTagRule
				.getPercentage());

		try {
			ObjectsFilesTable.updateSelectedRule(selectedXplaneLightTagRule
					.getObjectsFiles());
		} catch (Osm2xpBusinessException e) {
			Osm2xpLogger.error("Error updating rules table", e);
		}
		compositeRuleDetail.setVisible(true);

	}
}
