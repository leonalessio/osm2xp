package com.osm2xp.gui.views.panels.fsx;

import java.text.MessageFormat;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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
import com.osm2xp.generation.options.FsxOptionsProvider;
import com.osm2xp.generation.options.ObjectFile;
import com.osm2xp.generation.options.rules.ObjectTagRule;
import com.osm2xp.gui.Activator;
import com.osm2xp.gui.components.AbstractPathsTable;
import com.osm2xp.gui.components.FilePathsTable;
import com.osm2xp.gui.components.RulesTable;
import com.osm2xp.gui.components.TagsRulesTable;
import com.osm2xp.gui.views.panels.Osm2xpPanel;

/**
 * FsxObjectsRulesPanel.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class FsxObjectsRulesPanel extends Osm2xpPanel {

	private RulesTable tagsTable;
	private AbstractPathsTable objectsFilesTable;
	private Group grpFiles;
	private Group grpAngle;
	private static final String[] FILTER_NAMES = { "XML objects rules file (*.xml)" };
	private static final String[] FILTER_EXTS = { "*.xml" };
	private ObjectTagRule selectedObjectTagRule;
	private Spinner spinnerAngle;
	private Composite compositeRuleDetail;
	private Button btnCheckRandomAngle;
	private ToolBar toolBar;
	private ToolItem tltmAdd;
	private ToolItem tltmDelete;
	private ToolItem tltmExport;
	private ToolItem tltmImport;
	private Group groupTags;
	private GridData gridData;
	private GridData gridDataObjects;
	private GridData gridAngle;
	private GridData gridFiles;
	private ToolBar toolBarObjectFiles;
	private ToolItem tltmAddObjectFile;
	private ToolItem tltmDeleteObjectFile;

	public FsxObjectsRulesPanel(final Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected void initLayout() {
		setLayout(new GridLayout(2, false));
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1));
		groupTags.setLayoutData(gridData);
		groupTags.setLayout(new GridLayout(1, false));
		compositeRuleDetail.setLayout(new GridLayout(1, false));
		compositeRuleDetail.setLayoutData(gridDataObjects);
		grpAngle.setLayoutData(gridAngle);
		grpAngle.setLayout(new GridLayout(5, false));
		grpFiles.setLayoutData(gridFiles);
		grpFiles.setLayout(new GridLayout(1, false));
	}

	@Override
	protected void initComponents() {
		toolBar = new ToolBar(this, SWT.FLAT | SWT.RIGHT);
		tltmAdd = new ToolItem(toolBar, SWT.NONE);
		tltmAdd.setToolTipText("add");
		tltmAdd.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID,
				"images/toolbarsIcons/add_16.ico"));
		tltmDelete = new ToolItem(toolBar, SWT.NONE);
		tltmDelete.setToolTipText("delete");
		tltmDelete.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID,
				"images/toolbarsIcons/delete_16.ico"));
		ToolItem tltmSeparator = new ToolItem(toolBar, SWT.SEPARATOR);
		tltmSeparator.setWidth(20);
		tltmExport = new ToolItem(toolBar, SWT.NONE);
		tltmExport.setToolTipText("Export");
		tltmExport.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID,
				"images/toolbarsIcons/export_16.ico"));
		tltmImport = new ToolItem(toolBar, SWT.NONE);
		tltmImport.setToolTipText("Import");
		tltmImport.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID,
				"images/toolbarsIcons/import_16.ico"));
		new Label(this, SWT.NONE);
		groupTags = new Group(this, SWT.NONE);
		groupTags.setText("Objects rules - osm tags ");
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
//		gridData.heightHint = 320;
//		gridData.widthHint = 329;
		tagsTable = new TagsRulesTable(groupTags, SWT.NONE, FsxOptionsProvider
				.getOptions().getObjectsRules().getRules());

		compositeRuleDetail = new Composite(this, SWT.NONE);
		compositeRuleDetail.setVisible(false);
		gridDataObjects = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
//		gridDataObjects.heightHint = 347;
//		gridDataObjects.widthHint = 608;
		grpAngle = new Group(compositeRuleDetail, SWT.NONE);

		gridAngle = new GridData(SWT.LEFT, SWT.TOP, true, false, 1, 1);
		gridAngle.heightHint = 28;
		btnCheckRandomAngle = new Button(grpAngle, SWT.CHECK);
		btnCheckRandomAngle.setText("Random angle");
		new Label(grpAngle, SWT.NONE);

		Label lblAngle = new Label(grpAngle, SWT.NONE);
		lblAngle.setBounds(0, 0, 20, 13);
		lblAngle.setText("Angle :");

		spinnerAngle = new Spinner(grpAngle, SWT.BORDER);
		spinnerAngle.setMaximum(360);
		new Label(grpAngle, SWT.NONE);
		new Label(grpAngle, SWT.NONE);
		new Label(grpAngle, SWT.NONE);
		new Label(grpAngle, SWT.NONE);
		new Label(grpAngle, SWT.NONE);
		new Label(grpAngle, SWT.NONE);
		grpFiles = new Group(compositeRuleDetail, SWT.NONE);
		gridFiles = new GridData(SWT.LEFT, SWT.BOTTOM, true, false, 1, 1);
		gridFiles.heightHint = 264;

		toolBarObjectFiles = new ToolBar(grpFiles, SWT.FLAT | SWT.RIGHT);
		tltmAddObjectFile = new ToolItem(toolBarObjectFiles, SWT.NONE);
		tltmAddObjectFile.setToolTipText("add");
		tltmAddObjectFile.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID,
				"images/toolbarsIcons/add_16.ico"));
		tltmDeleteObjectFile = new ToolItem(toolBarObjectFiles, SWT.NONE);
		tltmDeleteObjectFile.setToolTipText("delete");
		tltmDeleteObjectFile.setImage(ResourceManager.getPluginImage(
				Activator.PLUGIN_ID, "images/toolbarsIcons/delete_16.ico"));
		objectsFilesTable = new FilePathsTable(grpFiles, SWT.NONE,
				"Object GUID");

		new Label(grpFiles, SWT.NONE);
	}

	@Override
	protected void bindComponents() {

	}

	@Override
	protected void addComponentsListeners() {
		// add an empty object rule
		tltmAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FsxOptionsProvider
						.getOptions()
						.getObjectsRules()
						.getRules()
						.add(new ObjectTagRule(new Tag("",
								""), Lists.newArrayList(new ObjectFile("object GUID")), 0, true));
				tagsTable.getViewer().refresh();

			}
		});
		// delete an object rule
		tltmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) tagsTable
						.getViewer().getSelection();
				FsxOptionsProvider.getOptions().getObjectsRules().getRules()
						.remove((ObjectTagRule) selection.getFirstElement());
				compositeRuleDetail.setVisible(false);
				tagsTable.getViewer().refresh();
			}
		});
		// Export action listener
		tltmExport.addSelectionListener(new SelectionAdapter() {
			@Override
			// export current rules to a xml file
			public void widgetSelected(SelectionEvent e) {
				FileDialog dlg = new FileDialog(getParent().getShell(),
						SWT.SAVE);
				dlg.setFilterNames(FILTER_NAMES);
				dlg.setFilterExtensions(FILTER_EXTS);
				String fn = dlg.open();
				if (fn != null) {
//					FsxOptionsProvider.exportObjectsRules(new File(fn)); //TODO not supported yet

				}
			}

		});
		// import action listener
		tltmImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dlg = new FileDialog(getParent().getShell(),
						SWT.OPEN);
				dlg.setFilterNames(FILTER_NAMES);
				dlg.setFilterExtensions(FILTER_EXTS);
				String fn = dlg.open();
				if (fn != null) {
//					FsxOptionsProvider.importObjectsRules(new File(fn)); //TODO not supported yet
					tagsTable.updateInput(FsxOptionsProvider.getOptions()
							.getObjectsRules().getRules());

				}
			}
		});
		// rules selection listener
		tagsTable.getTable().addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				selectedObjectTagRule = (ObjectTagRule) event.item.getData();
				String selectedTag = selectedObjectTagRule.getTag().getKey()
						+ "=" + selectedObjectTagRule.getTag().getValue();
				grpFiles.setText(MessageFormat.format(
						MessagesConstants.LABEL_FILES_OBJECT_RULE, selectedTag));
				grpAngle.setText(MessageFormat.format(
						MessagesConstants.LABEL_ANGLE_OBJECT_RULE, selectedTag));
				spinnerAngle.setSelection(selectedObjectTagRule.getAngle());
				btnCheckRandomAngle.setSelection(selectedObjectTagRule
						.isRandomAngle());

				try {
					objectsFilesTable.updateSelectedItem(selectedObjectTagRule
							.getObjectsFiles());
				} catch (Osm2xpBusinessException e) {
					Osm2xpLogger.error("Error updating rules table.", e);
				}
				compositeRuleDetail.setVisible(true);

			}
		});
		// random angle button listener
		btnCheckRandomAngle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				selectedObjectTagRule.setRandomAngle(!btnCheckRandomAngle
						.getSelection());

			}
		});

		// spinner angle action listener
		spinnerAngle.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				selectedObjectTagRule.setAngle(spinnerAngle.getSelection());
			}
		});
		tltmAddObjectFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ObjectFile file = new ObjectFile();
				file.setPath("object GUID");
				selectedObjectTagRule.getObjectsFiles().add(file);
				objectsFilesTable.getViewer().refresh();
			}
		});
		// delete file action listener
		tltmDeleteObjectFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				IStructuredSelection selection = (IStructuredSelection) objectsFilesTable
						.getViewer().getSelection();
				ObjectFile selectedFile = (ObjectFile) selection
						.getFirstElement();
				selectedObjectTagRule.getObjectsFiles().remove(selectedFile);
				objectsFilesTable.getViewer().refresh();
			}
		});
	}

}
