package com.osm2xp.gui.views.panels.xplane;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;

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

import com.osm2xp.constants.MessagesConstants;
import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.generation.options.ObjectFile;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.generation.options.XmlHelper;
import com.osm2xp.generation.options.rules.FacadeTagRule;
import com.osm2xp.gui.Activator;
import com.osm2xp.gui.components.AbstractPathsTable;
import com.osm2xp.gui.components.FilesPathsTable;
import com.osm2xp.gui.components.RulesTable;
import com.osm2xp.gui.components.TagsRulesTable;
import com.osm2xp.gui.views.panels.Osm2xpPanel;
import com.osm2xp.utils.helpers.XplaneOptionsHelper;

/**
 * FacadesRulesPanel.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class FacadesRulesPanel extends Osm2xpPanel {

	final RulesTable tagsTable;
	final AbstractPathsTable facadesFilesTable;
	final Group grpFiles;
	final Group grpSize;
	private static final String[] FILTER_NAMES = { "XML facades rules file (*.xml)" };
	private static final String[] FILTER_EXTS = { "*.xml" };
	private FacadeTagRule selectedFacadeTagRule;
	private Spinner spinnerMinSize;
	private Spinner spinnerMaxSize;
	private Composite compositeRuleDetail;
	private ToolBar toolBar;
	private ToolItem tltmAdd;
	private ToolItem tltmDelete;
	private ToolItem tltmSeparator;
	private ToolItem tltmExport;
	private ToolItem tltmImport;

	public FacadesRulesPanel(final Composite parent, int style) {
		super(parent, style);

		new Label(this, SWT.NONE);

		Group groupTags = new Group(this, SWT.NONE);
		groupTags.setText("facades rules - osm tags ");
		GridData gridData = new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1);
		gridData.heightHint = 320;
		gridData.widthHint = 329;
		groupTags.setLayoutData(gridData);
		groupTags.setLayout(new FillLayout(SWT.HORIZONTAL));
		tagsTable = new TagsRulesTable(groupTags, SWT.NONE, XPlaneOptionsProvider
				.getOptions().getFacadesRules().getRules());
		tagsTable.setLayout(new FillLayout(SWT.HORIZONTAL));
		tagsTable.getTable().addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				selectedFacadeTagRule = (FacadeTagRule) event.item.getData();
				String selectedTag = selectedFacadeTagRule.getTag().getKey()
						+ "=" + selectedFacadeTagRule.getTag().getValue();
				grpFiles.setText(MessageFormat.format(
						MessagesConstants.LABEL_FILES_FACADE_RULE, selectedTag));
				grpSize.setText(MessageFormat
						.format(MessagesConstants.LABEL_HEIGHT_FACADE_RULE,
								selectedTag));
				spinnerMinSize.setSelection(selectedFacadeTagRule.getSizeMin());
				spinnerMaxSize.setSelection(selectedFacadeTagRule.getSizeMax());
				try {
					facadesFilesTable.updateSelectedItem(selectedFacadeTagRule
							.getObjectsFiles());
				} catch (Osm2xpBusinessException e) {
					Osm2xpLogger.error("Error updating rules table.", e);
				}
				compositeRuleDetail.setVisible(true);

			}
		});

		compositeRuleDetail = new Composite(this, SWT.NONE);
		compositeRuleDetail.setVisible(false);
		compositeRuleDetail.setLayout(new GridLayout(1, false));
		GridData gridDataFacadesObjects = new GridData(SWT.FILL, SWT.TOP, true,
				false, 1, 1);
		gridDataFacadesObjects.heightHint = 347;
		gridDataFacadesObjects.widthHint = 608;
		compositeRuleDetail.setLayoutData(gridDataFacadesObjects);
		grpSize = new Group(compositeRuleDetail, SWT.NONE);
		GridData gridDataSize = new GridData(SWT.LEFT, SWT.TOP, true, false, 1,
				1);

		grpSize.setLayoutData(gridDataSize);
		grpSize.setLayout(new GridLayout(6, false));

		Label lblMinSize = new Label(grpSize, SWT.NONE);
		lblMinSize.setBounds(0, 0, 53, 13);
		lblMinSize.setText("Min : ");

		spinnerMinSize = new Spinner(grpSize, SWT.BORDER);
		spinnerMinSize.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				selectedFacadeTagRule.setSizeMin(spinnerMinSize.getSelection());
			}
		});
		new Label(grpSize, SWT.NONE);
		new Label(grpSize, SWT.NONE);

		Label lblMaxSize = new Label(grpSize, SWT.NONE);
		lblMaxSize.setBounds(0, 0, 53, 13);
		lblMaxSize.setText("Max : ");

		spinnerMaxSize = new Spinner(grpSize, SWT.BORDER);
		spinnerMaxSize.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		spinnerMaxSize.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				selectedFacadeTagRule.setSizeMax(spinnerMaxSize.getSelection());
			}
		});

		grpFiles = new Group(compositeRuleDetail, SWT.NONE);
		GridData gd_grpFiles = new GridData(SWT.LEFT, SWT.BOTTOM, true, false,
				1, 1);
		gd_grpFiles.heightHint = 261;
		grpFiles.setLayoutData(gd_grpFiles);
		grpFiles.setLayout(new GridLayout(1, false));

		ToolBar toolBarFacadeFiles = new ToolBar(grpFiles, SWT.FLAT | SWT.RIGHT);

		ToolItem tltmAddFacadeFile = new ToolItem(toolBarFacadeFiles, SWT.NONE);
		tltmAddFacadeFile.setToolTipText("add");
		tltmAddFacadeFile.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID,
				"images/toolbarsIcons/add_16.ico"));
		tltmAddFacadeFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ObjectFile file = new ObjectFile();
				file.setPath("SomePathTo" + File.separator + "aFacadeFile.fac");
				selectedFacadeTagRule.getObjectsFiles().add(file);
				facadesFilesTable.getViewer().refresh();
			}
		});

		ToolItem tltmDeleteFacadeFile = new ToolItem(toolBarFacadeFiles,
				SWT.NONE);
		tltmDeleteFacadeFile.setToolTipText("delete");
		tltmDeleteFacadeFile.setImage(ResourceManager.getPluginImage(
				Activator.PLUGIN_ID, "images/toolbarsIcons/delete_16.ico"));
		tltmDeleteFacadeFile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				IStructuredSelection selection = (IStructuredSelection) facadesFilesTable
						.getViewer().getSelection();
				ObjectFile selectedFile = (ObjectFile) selection
						.getFirstElement();
				selectedFacadeTagRule.getObjectsFiles().remove(selectedFile);
				facadesFilesTable.getViewer().refresh();
			}
		});

		facadesFilesTable = new FilesPathsTable(grpFiles, SWT.NONE,
				"Facade path");
//		facadesFilesTable.setLayout(new FillLayout(SWT.HORIZONTAL));
		new Label(grpFiles, SWT.NONE);

	}

	@Override
	protected void initLayout() {
		setLayout(new GridLayout(2, false));

		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1));

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

		tltmSeparator = new ToolItem(toolBar, SWT.SEPARATOR);
		tltmSeparator.setWidth(20);

		tltmExport = new ToolItem(toolBar, SWT.NONE);
		tltmExport.setToolTipText("Export");
		tltmExport.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID,
				"images/toolbarsIcons/export_16.ico"));
		tltmImport = new ToolItem(toolBar, SWT.NONE);
		tltmImport.setToolTipText("Import");
		tltmImport.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID,
				"images/toolbarsIcons/import_16.ico"));
	}

	@Override
	protected void bindComponents() {
		tltmAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			// add a facade rule
			public void widgetSelected(SelectionEvent e) {
				XPlaneOptionsProvider
						.getOptions()
						.getFacadesRules()
						.getRules()
						.add(new FacadeTagRule(new Tag("a tag key",
								"a tag value"), new ArrayList<ObjectFile>() {
							{
								add(new ObjectFile("the path to an Object file"));

							}
						}, 10, 40));

				tagsTable.getViewer().refresh();
			}
		});
		tltmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) tagsTable
						.getViewer().getSelection();
				FacadeTagRule selectedFacadeTagRule = (FacadeTagRule) selection
						.getFirstElement();
				XPlaneOptionsProvider.getOptions().getFacadesRules().getRules()
						.remove(selectedFacadeTagRule);
				compositeRuleDetail.setVisible(false);
				tagsTable.getViewer().refresh();
			}
		});
	}

	@Override
	protected void addComponentsListeners() {
		tltmExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dlg = new FileDialog(getParent().getShell(),
						SWT.SAVE);
				dlg.setFilterNames(FILTER_NAMES);
				dlg.setFilterExtensions(FILTER_EXTS);
				String fn = dlg.open();
				if (fn != null) {
					try {
						XmlHelper.saveToXml(XPlaneOptionsProvider.getOptions()
								.getFacadesRules(), new File(fn));
					} catch (Osm2xpBusinessException e1) {
						Osm2xpLogger.error("Error saving facades rules to file"
								+ new File(fn).getName(), e1);
					}

				}
			}

		});
		tltmImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dlg = new FileDialog(getParent().getShell(),
						SWT.OPEN);
				dlg.setFilterNames(FILTER_NAMES);
				dlg.setFilterExtensions(FILTER_EXTS);
				String fn = dlg.open();
				if (fn != null) {
					XplaneOptionsHelper.importFacadesRules(new File(fn));
					tagsTable.updateInput(XPlaneOptionsProvider.getOptions()
							.getFacadesRules().getRules());

				}
			}
		});
	}
}
