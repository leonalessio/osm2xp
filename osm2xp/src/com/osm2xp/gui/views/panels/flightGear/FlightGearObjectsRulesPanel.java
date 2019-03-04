package com.osm2xp.gui.views.panels.flightGear;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
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
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.ResourceManager;

import com.osm2xp.constants.MessagesConstants;
import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.core.model.osm.Tag;
import com.osm2xp.generation.options.FlightGearObjectTagRule;
import com.osm2xp.generation.options.ObjectFile;
import com.osm2xp.generation.options.XmlHelper;
import com.osm2xp.gui.components.FilesPathsTable;
import com.osm2xp.gui.components.TagsRulesTable;
import com.osm2xp.utils.helpers.FlightGearOptionsProvider;

/**
 * FlightGearObjectsRulesPanel.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class FlightGearObjectsRulesPanel extends Composite {

	final TagsRulesTable tagsTable;
	final FilesPathsTable ObjectsFilesTable;
	final Group grpFiles;
	private static final String[] FILTER_NAMES = { "XML objects rules file (*.xml)" };
	private static final String[] FILTER_EXTS = { "*.xml" };
	private FlightGearObjectTagRule selectedFlightGearObjectTagRule;
	private Composite compositeRuleDetail;
	private Button btnCheckSizeConditions;
	private Button btnCheckUsePolygonAngle;
	private Button btnCheckRandomAngle;
	private Button btnCheckSimplePolygon;
	private Spinner spinnerXVectorMaxSize;
	private Spinner spinnerYVectorMaxSize;
	private Button btnCheckSurfaceCondition;
	private Spinner spinnerMinArea;
	private Spinner spinnerMaxArea;
	private Spinner spinnerAngle;
	private Spinner spinnerRotationPointX;
	private Spinner spinnerRotationPointY;
	private Spinner spinnerXVectorMinSize;
	private Spinner spinnerYVectorMinSize;

	public FlightGearObjectsRulesPanel(final Composite parent, int style) {
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
				FlightGearOptionsProvider
						.getOptions()
						.getObjectsRules()
						.getRules()
						.add(new FlightGearObjectTagRule(new Tag("a tag key",
								"a tag value"), new ArrayList<ObjectFile>() {
							{
								add(new ObjectFile("the path to an Object file"));

							}
						}, 0, true, false, false, 0, 0, 0, 0, false, 0,
								0, false, false));
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
				FlightGearObjectTagRule selectedFlightGearObjectTagRule = (FlightGearObjectTagRule) selection
						.getFirstElement();
				FlightGearOptionsProvider.getOptions().getObjectsRules()
						.getRules().remove(selectedFlightGearObjectTagRule);
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
						XmlHelper.saveToXml(FlightGearOptionsProvider
								.getOptions().getObjectsRules(), new File(fn));
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
					FlightGearOptionsProvider.importObjectsRules(new File(fn));
					tagsTable.updateInput(FlightGearOptionsProvider.getOptions()
							.getObjectsRules().getRules());

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
		tagsTable = new TagsRulesTable(groupTags, SWT.NONE,
				FlightGearOptionsProvider.getOptions().getObjectsRules()
						.getRules());
		tagsTable.setLayout(new FillLayout(SWT.HORIZONTAL));
		tagsTable.getTable().addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				selectedFlightGearObjectTagRule = (FlightGearObjectTagRule) event.item
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
				selectedFlightGearObjectTagRule.getObjectsFiles().add(file);
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
				selectedFlightGearObjectTagRule.getObjectsFiles().remove(
						selectedFile);
				ObjectsFilesTable.getViewer().refresh();
			}
		});

		ObjectsFilesTable = new FilesPathsTable(grpFiles, SWT.NONE, "Object ID");
		ObjectsFilesTable.setLayout(new FillLayout(SWT.HORIZONTAL));
		new Label(grpFiles, SWT.NONE);

		TabItem tabAngle = new TabItem(tabFolder, SWT.NONE);
		tabAngle.setText("Angle");

		Composite compositeAngle = new Composite(tabFolder, SWT.NONE);
		tabAngle.setControl(compositeAngle);
		compositeAngle.setLayout(new GridLayout(1, false));

		Group grpAngle = new Group(compositeAngle, SWT.NONE);
		grpAngle.setSize(316, 90);
		grpAngle.setLayout(new GridLayout(3, false));
		GridData gd_grpAngle = new GridData(SWT.LEFT, SWT.TOP, false, true, 1,
				1);
		gd_grpAngle.heightHint = 107;
		gd_grpAngle.widthHint = 373;
		grpAngle.setLayoutData(gd_grpAngle);
		grpAngle.setText("angle");

		btnCheckRandomAngle = new Button(grpAngle, SWT.CHECK);
		btnCheckRandomAngle.setText("Random angle");
		btnCheckRandomAngle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {

				selectedFlightGearObjectTagRule
						.setRandomAngle(!btnCheckRandomAngle.getSelection());
				if (!btnCheckRandomAngle.getSelection()) {
					btnCheckUsePolygonAngle.setSelection(false);
				}

				if (btnCheckUsePolygonAngle.getSelection()
						|| !btnCheckRandomAngle.getSelection()) {
					spinnerAngle.setEnabled(false);
				} else {
					spinnerAngle.setEnabled(true);
				}

			}
		});
		new Label(grpAngle, SWT.NONE);
		new Label(grpAngle, SWT.NONE);

		btnCheckUsePolygonAngle = new Button(grpAngle, SWT.CHECK);
		btnCheckUsePolygonAngle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				selectedFlightGearObjectTagRule
						.setUsePolygonAngle(!btnCheckUsePolygonAngle
								.getSelection());
				if (!btnCheckUsePolygonAngle.getSelection()) {
					btnCheckRandomAngle.setSelection(false);
				}

				if (!btnCheckUsePolygonAngle.getSelection()
						|| btnCheckRandomAngle.getSelection()) {
					spinnerAngle.setEnabled(false);
				} else {
					spinnerAngle.setEnabled(true);
				}

			}
		});
		btnCheckUsePolygonAngle.setBounds(0, 0, 118, 16);
		btnCheckUsePolygonAngle.setText("Polygon angle");

		Label lblFixedAngle = new Label(grpAngle, SWT.NONE);
		lblFixedAngle.setText("fixed angle :");

		spinnerAngle = new Spinner(grpAngle, SWT.BORDER);
		spinnerAngle.setMaximum(360);
		new Label(grpAngle, SWT.NONE);
		new Label(grpAngle, SWT.NONE);
		new Label(grpAngle, SWT.NONE);
		spinnerAngle.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				selectedFlightGearObjectTagRule.setAngle(spinnerAngle
						.getSelection());
			}
		});

		TabItem tabAdvanced = new TabItem(tabFolder, SWT.NONE);
		tabAdvanced.setText("Advanced options");

		Composite composite = new Composite(tabFolder, SWT.NONE);
		tabAdvanced.setControl(composite);
		GridLayout gl_composite = new GridLayout(1, false);
		gl_composite.marginTop = 10;
		composite.setLayout(gl_composite);

		Group grpOption = new Group(composite, SWT.NONE);
		grpOption.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false,
				1, 1));
		grpOption.setText("option");
		grpOption.setBounds(0, 0, 135, 19);
		grpOption.setLayout(new GridLayout(1, false));

		btnCheckSimplePolygon = new Button(grpOption, SWT.CHECK);
		btnCheckSimplePolygon.setText("Only simple polygons");
		btnCheckSimplePolygon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				selectedFlightGearObjectTagRule
						.setSimplePolygonOnly(!btnCheckSimplePolygon
								.getSelection());

			}
		});

		Group grpSize = new Group(composite, SWT.NONE);
		grpSize.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false,
				1, 1));
		grpSize.setText("size condition");
		grpSize.setLayout(new GridLayout(6, false));

		btnCheckSizeConditions = new Button(grpSize, SWT.CHECK);
		btnCheckSizeConditions.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				selectedFlightGearObjectTagRule
						.setSizeCheck(!btnCheckSizeConditions.getSelection());

			}
		});
		btnCheckSizeConditions.setBounds(0, 0, 91, 16);
		btnCheckSizeConditions.setText("Size conditions");

		Label lblXVectorMaxSize = new Label(grpSize, SWT.NONE);
		lblXVectorMaxSize.setBounds(0, 0, 53, 13);
		lblXVectorMaxSize.setText("X vector max size : ");

		spinnerXVectorMaxSize = new Spinner(grpSize, SWT.BORDER);
		spinnerXVectorMaxSize.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				selectedFlightGearObjectTagRule
						.setxVectorMaxLength(spinnerXVectorMaxSize
								.getSelection());
			}
		});
		spinnerXVectorMaxSize.setBounds(0, 0, 47, 22);
		new Label(grpSize, SWT.NONE);

		Label lblYVectorMaxSize = new Label(grpSize, SWT.NONE);
		lblYVectorMaxSize.setBounds(0, 0, 53, 13);
		lblYVectorMaxSize.setText("Y vector max size : ");

		spinnerYVectorMaxSize = new Spinner(grpSize, SWT.BORDER);
		spinnerYVectorMaxSize.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				selectedFlightGearObjectTagRule
						.setyVectorMaxLength(spinnerYVectorMaxSize
								.getSelection());
			}
		});
		spinnerYVectorMaxSize.setBounds(0, 0, 47, 22);
		new Label(grpSize, SWT.NONE);

		Label lblXVectorMinSize = new Label(grpSize, SWT.NONE);
		lblXVectorMinSize.setText("X vector min size : ");

		spinnerXVectorMinSize = new Spinner(grpSize, SWT.BORDER);
		spinnerXVectorMinSize.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				selectedFlightGearObjectTagRule
						.setxVectorMinLength(spinnerXVectorMinSize
								.getSelection());
			}
		});
		spinnerYVectorMaxSize.setBounds(0, 0, 47, 22);
		new Label(grpSize, SWT.NONE);

		Label lblYVectorMinSize = new Label(grpSize, SWT.NONE);
		lblYVectorMinSize.setText("Y vector min size : ");

		spinnerYVectorMinSize = new Spinner(grpSize, SWT.BORDER);
		spinnerYVectorMinSize.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				selectedFlightGearObjectTagRule
						.setyVectorMinLength(spinnerYVectorMinSize
								.getSelection());
			}
		});
		Group grpArea = new Group(composite, SWT.NONE);
		grpArea.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false,
				1, 1));
		grpArea.setText("area condition");
		grpArea.setLayout(new GridLayout(5, false));

		btnCheckSurfaceCondition = new Button(grpArea, SWT.CHECK);
		btnCheckSurfaceCondition.setText("Surface conditions");
		btnCheckSurfaceCondition.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				selectedFlightGearObjectTagRule
						.setAreaCheck(!btnCheckSurfaceCondition.getSelection());

			}
		});
		Label lblNewLabel_2 = new Label(grpArea, SWT.NONE);
		lblNewLabel_2.setText("min area : ");

		spinnerMinArea = new Spinner(grpArea, SWT.BORDER);
		spinnerMinArea.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				selectedFlightGearObjectTagRule.setMinArea(spinnerMinArea
						.getSelection());
			}
		});
		spinnerMinArea.setBounds(0, 0, 47, 22);

		Label lblNewLabel_3 = new Label(grpArea, SWT.NONE);
		lblNewLabel_3.setText("max area : ");

		spinnerMaxArea = new Spinner(grpArea, SWT.BORDER);
		spinnerMaxArea.setMaximum(1000);
		spinnerMaxArea.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				selectedFlightGearObjectTagRule.setMaxArea(spinnerMaxArea
						.getSelection());
			}
		});
		spinnerMaxArea.setBounds(0, 0, 47, 22);

		Group grpRotationPoint = new Group(composite, SWT.NONE);
		GridLayout gl_grpRotationPoint = new GridLayout(4, true);
		grpRotationPoint.setLayout(gl_grpRotationPoint);
		GridData gd_grpRotationPoint = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_grpRotationPoint.widthHint = 423;
		grpRotationPoint.setLayoutData(gd_grpRotationPoint);
		grpRotationPoint.setText("Rotation point");

		Label lblRotationX = new Label(grpRotationPoint, SWT.NONE);
		lblRotationX.setText("X : ");

		spinnerRotationPointX = new Spinner(grpRotationPoint, SWT.BORDER);
		spinnerRotationPointX.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				selectedFlightGearObjectTagRule
						.setRotationPointX(spinnerRotationPointX.getSelection());
			}
		});
		Label lblRotationY = new Label(grpRotationPoint, SWT.NONE);
		lblRotationY.setText("Y : ");

		spinnerRotationPointY = new Spinner(grpRotationPoint, SWT.BORDER);
		spinnerRotationPointY.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				selectedFlightGearObjectTagRule
						.setRotationPointY(spinnerRotationPointY.getSelection());
			}
		});
	}

	private void updateRuleControls() {

		String selectedTag = selectedFlightGearObjectTagRule.getTag().getKey()
				+ "=" + selectedFlightGearObjectTagRule.getTag().getValue();
		grpFiles.setText(MessageFormat.format(
				MessagesConstants.LABEL_FILES_OBJECT_RULE, selectedTag));
		spinnerAngle.setSelection(selectedFlightGearObjectTagRule.getAngle());

		btnCheckRandomAngle.setSelection(selectedFlightGearObjectTagRule
				.isRandomAngle());
		btnCheckSizeConditions.setSelection(selectedFlightGearObjectTagRule
				.isSizeCheck());
		btnCheckSurfaceCondition.setSelection(selectedFlightGearObjectTagRule
				.isAreaCheck());
		btnCheckSimplePolygon.setSelection(selectedFlightGearObjectTagRule
				.isSimplePolygonOnly());
		btnCheckUsePolygonAngle.setSelection(selectedFlightGearObjectTagRule
				.isUsePolygonAngle());
		btnCheckRandomAngle.setSelection(selectedFlightGearObjectTagRule
				.isRandomAngle());
		spinnerXVectorMaxSize.setSelection(selectedFlightGearObjectTagRule
				.getxVectorMaxLength());
		spinnerAngle.setSelection(selectedFlightGearObjectTagRule.getAngle());
		spinnerYVectorMaxSize.setSelection(selectedFlightGearObjectTagRule
				.getyVectorMaxLength());
		spinnerMinArea.setSelection(selectedFlightGearObjectTagRule
				.getMinArea());
		spinnerMaxArea.setSelection(selectedFlightGearObjectTagRule
				.getMaxArea());
		spinnerRotationPointX.setSelection(selectedFlightGearObjectTagRule
				.getRotationPointX());
		spinnerRotationPointY.setSelection(selectedFlightGearObjectTagRule
				.getRotationPointY());

		// disable angle spinner if random angle or polygon angle is enabled.
		spinnerAngle.setEnabled(!(selectedFlightGearObjectTagRule
				.isRandomAngle() || selectedFlightGearObjectTagRule
				.isUsePolygonAngle()));
		try {
			ObjectsFilesTable
					.updateSelectedRule(selectedFlightGearObjectTagRule
							.getObjectsFiles());
		} catch (Osm2xpBusinessException e) {
			Osm2xpLogger.error("Error updating rules table", e);
		}
		compositeRuleDetail.setVisible(true);

	}
}
