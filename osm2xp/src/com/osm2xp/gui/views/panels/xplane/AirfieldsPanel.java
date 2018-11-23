package com.osm2xp.gui.views.panels.xplane;

import java.util.List;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.PixelConverter;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.osm2xp.gui.Activator;
import com.osm2xp.gui.views.panels.Osm2xpPanel;
import com.osm2xp.model.options.XplaneAirfieldOptions;
import com.osm2xp.utils.UiUtil;
import com.osm2xp.utils.helpers.XplaneOptionsHelper;

public class AirfieldsPanel extends Osm2xpPanel {

	private ListViewer icaoListViewer;
	private List<String> ignoredICAOList;

	public AirfieldsPanel(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(1, false));
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		toolkit.adapt(this);
		Button createAirfieldsCheck = toolkit.createButton(this, "Generate Airfields", SWT.CHECK);
		GridDataFactory.swtDefaults().applyTo(createAirfieldsCheck);
		XplaneAirfieldOptions options = XplaneOptionsHelper.getOptions().getAirfieldOptions();
		bindComponent(createAirfieldsCheck, options, "generateAirfields");
		Section aptConfigSection = toolkit.createSection(this, Section.TITLE_BAR);
		aptConfigSection.setText("Airfield generation properties");
		Composite con = toolkit.createComposite(aptConfigSection);
		aptConfigSection.setClient(con);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(aptConfigSection);
		createAirfieldsCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				UiUtil.setEnabledRecursive(aptConfigSection, createAirfieldsCheck.getSelection());
			}
		});
		
		con.setLayout(new GridLayout(2,false));
		Composite leftComposite = toolkit.createComposite(con);
		GridDataFactory.fillDefaults().grab(false, true).applyTo(leftComposite);
		leftComposite.setLayout(new GridLayout(2,false));
		Button apronCheck = toolkit.createButton(leftComposite, "Generate apron && taxiways", SWT.CHECK);
		GridDataFactory.swtDefaults().span(2,1).applyTo(apronCheck);
		bindComponent(apronCheck, options, "generateApron");
		Button marksCheck = toolkit.createButton(leftComposite, "Generate apron && taxiway marks", SWT.CHECK);
		marksCheck.setToolTipText("Generate yellow centerline mark where possible");
		GridDataFactory.swtDefaults().span(2,1).applyTo(marksCheck);
		apronCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				marksCheck.setEnabled(apronCheck.getSelection());
			}
		});
		bindComponent(marksCheck, options, "generateApron");
		Button flattenCheck = toolkit.createButton(leftComposite, "Flatten airfield when possible", SWT.CHECK);
		flattenCheck.setToolTipText("Flatten is possible when airfield area and elevation are specified in OSM. "
				+ "Without elevation ('ele' property) flattening would cause airfield look like a giant pit");
		GridDataFactory.swtDefaults().span(2,1).applyTo(flattenCheck);
		bindComponent(flattenCheck, options, "flatten");
		Button preferEnglish = toolkit.createButton(leftComposite, "Prefer English airfield names when possible", SWT.CHECK);
		preferEnglish.setToolTipText("English is international aviation language, so it can be better to use English name instead of local, if it's specified");
		GridDataFactory.swtDefaults().span(2,1).applyTo(preferEnglish);
		bindComponent(preferEnglish, options, "preferEnglish");
		Button getElevCheck = toolkit.createButton(leftComposite, "Try to get Airfield elevation online, if not specified", SWT.CHECK);
		getElevCheck.setToolTipText("Try to get Airfield elevation on-line, if this data is absent in OSM data. "
				+ "This data is required e.g. for airfield area flattening. Internet connection is required for this to work.");
		GridDataFactory.swtDefaults().span(2,1).applyTo(getElevCheck);
		bindComponent(getElevCheck, options, "tryGetElev");
		Button getNameCheck = toolkit.createButton(leftComposite, "Try to get Airfield name online, if not specified", SWT.CHECK);
		getNameCheck.setToolTipText("Try to substitute airfield name online, if this information is missing in tags. Usually it's a name of nearest village/town.");
		GridDataFactory.swtDefaults().span(2,1).applyTo(getNameCheck);
		bindComponent(getNameCheck, options, "tryGetName");
		toolkit.createLabel(leftComposite,"Hard runway default width, m").setLayoutData(GridDataFactory.swtDefaults().create());
		Spinner rwyWidthSpinner = new Spinner(leftComposite, SWT.BORDER);
		rwyWidthSpinner.setMinimum(1);
		bindComponent(rwyWidthSpinner, options, "defaultHardRunwayWidth");
		toolkit.createLabel(leftComposite,"Hard taxiway default width, m").setLayoutData(GridDataFactory.swtDefaults().create());
		Spinner taxiwayWidthSpinner = new Spinner(leftComposite, SWT.BORDER);
		taxiwayWidthSpinner.setMinimum(1);
		bindComponent(taxiwayWidthSpinner, options, "defaultHardTaxiwayWidth");
		toolkit.createLabel(leftComposite,"Non-hard runway default width, m").setLayoutData(GridDataFactory.swtDefaults().create());
		Spinner grassRwyWidthSpinner = new Spinner(leftComposite, SWT.BORDER);
		grassRwyWidthSpinner.setMinimum(1);
		bindComponent(grassRwyWidthSpinner, options, "defaultGrassRunwayWidth");
		toolkit.createLabel(leftComposite,"Non-hard taxiway default width, m").setLayoutData(GridDataFactory.swtDefaults().create());
		Spinner grassTaxiwayWidthSpinner = new Spinner(leftComposite, SWT.BORDER);
		grassTaxiwayWidthSpinner.setMinimum(1);
		bindComponent(grassTaxiwayWidthSpinner, options, "defaultGrassTaxiwayWidth");
		
		Composite rightComposite = toolkit.createComposite(con, SWT.NONE);
		rightComposite.setLayout(new GridLayout(2, false));
		GridDataFactory.fillDefaults().grab(true, true).applyTo(rightComposite);
		createExclusionList(rightComposite, toolkit);
		UiUtil.setEnabledRecursive(aptConfigSection, createAirfieldsCheck.getSelection());
	}

	private void createExclusionList(Composite parent, FormToolkit toolkit) {
		ignoredICAOList = XplaneOptionsHelper.getOptions().getAirfieldOptions().getIgnoredAirfields();
		
		Label lbl = toolkit.createLabel(parent, "Exclude following airports from generation");
		GridDataFactory.swtDefaults().span(2,1).applyTo(lbl);
		icaoListViewer = new ListViewer(parent, SWT.BORDER);
		PixelConverter converter = new PixelConverter(icaoListViewer.getControl());
		GridDataFactory.fillDefaults().hint(converter.convertWidthInCharsToPixels(10),converter.convertWidthInCharsToPixels(30)).span(1,2).applyTo(icaoListViewer.getControl());
		icaoListViewer.setContentProvider(new ArrayContentProvider());
		icaoListViewer.setLabelProvider(new LabelProvider());
		
		Button addButton = new Button(parent, SWT.PUSH);
		addButton.setImage(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/add.png").createImage());
		addButton.setToolTipText("Add facade set by descriptor");
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doAddICAO();
			}
		});
		GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.TOP).applyTo(addButton);
		Button removeButton = new Button(parent, SWT.PUSH);
		removeButton.setImage(AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/remove.gif").createImage());
		removeButton.setToolTipText("Remove selected facade set");
		removeButton.setEnabled(false);
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doRemoveSelected();
			}
		});
		GridDataFactory.swtDefaults().align(SWT.LEFT, SWT.TOP).applyTo(removeButton);
		icaoListViewer.addSelectionChangedListener(event -> {
			boolean hasSelection = !event.getSelection().isEmpty();
			removeButton.setEnabled(hasSelection);
		});
		icaoListViewer.addDoubleClickListener(e -> {
			if (icaoListViewer.getSelection().isEmpty()) {
				doAddICAO();
			} 
		});
		refreshViewer();
	}
	
	protected void doRemoveSelected() {
		ISelection selection = icaoListViewer.getSelection();
		if (selection instanceof StructuredSelection && !selection.isEmpty()) { 
			String selected = ((StructuredSelection) selection).getFirstElement().toString();
			ignoredICAOList.remove(selected);
			refreshViewer();
		}
		XplaneOptionsHelper.getOptions().getAirfieldOptions().setIgnoredAirfields(ignoredICAOList);
	}

	private void refreshViewer() {
		icaoListViewer.setInput(ignoredICAOList);
	}

	private void doAddICAO() {
		InputDialog dialog = new InputDialog(getShell(), "Enter ICAO code", "Enter Airfield ICAO code, which should be ignored during generation","", input -> checkICAO(input));
		if (dialog.open() == Window.OK) {
			String value = dialog.getValue().toUpperCase().trim();
			ignoredICAOList.add(value);
			refreshViewer();
		}
		XplaneOptionsHelper.getOptions().getAirfieldOptions().setIgnoredAirfields(ignoredICAOList);
	}

	private String checkICAO(String input) {
		input = input.toLowerCase().trim();
		if (input.length() != 4) {
			return "ICAO code should be 4 characters long";
		}
		for (int i = 0; i < input.length(); i++) {
			if (!Character.isLetterOrDigit(input.charAt(i))) {
				return "Character '" + input.charAt(i) + "' is not allowed in ICAO code";
			}
		}
		return null;
	}

	@Override
	protected void initLayout() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initComponents() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void bindComponents() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void addComponentsListeners() {
		// TODO Auto-generated method stub

	}

}
