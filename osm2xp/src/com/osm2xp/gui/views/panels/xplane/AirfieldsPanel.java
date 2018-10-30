package com.osm2xp.gui.views.panels.xplane;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import com.osm2xp.gui.views.panels.Osm2xpPanel;
import com.osm2xp.model.options.XplaneAirfieldOptions;
import com.osm2xp.utils.UiUtil;
import com.osm2xp.utils.helpers.XplaneOptionsHelper;

public class AirfieldsPanel extends Osm2xpPanel {

	public AirfieldsPanel(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(1, false));
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		toolkit.adapt(this);
		Button createAirfieldsCheck = toolkit.createButton(this, "Generate Airfields", SWT.CHECK);
		GridDataFactory.swtDefaults().applyTo(createAirfieldsCheck);
		XplaneAirfieldOptions options = XplaneOptionsHelper.getOptions().getAirfieldOptions();
		bindComponent(createAirfieldsCheck, options, "generateAirfields");
		Section aptConfigSection = toolkit.createSection(this, SWT.NONE);
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
		Button apronCheck = toolkit.createButton(con, "Generate apron && taxiways", SWT.CHECK);
		GridDataFactory.swtDefaults().span(2,1).applyTo(apronCheck);
		bindComponent(apronCheck, options, "generateApron");
		Button flattenCheck = toolkit.createButton(con, "Flatten airfield when possible", SWT.CHECK);
		flattenCheck.setToolTipText("Flatten is possible when airfield area and elevation are specified in OSM. "
				+ "Without elevation ('ele' property) flattening would cause airfield look like a giant pit");
		GridDataFactory.swtDefaults().span(2,1).applyTo(flattenCheck);
		bindComponent(flattenCheck, options, "flatten");
		toolkit.createLabel(con,"Ruway default width").setLayoutData(GridDataFactory.swtDefaults().create());
		Spinner rwyWidthSpinner = new Spinner(con, SWT.BORDER);
		rwyWidthSpinner.setMinimum(1);
		bindComponent(rwyWidthSpinner, options, "defaultRunwayWidth");
		toolkit.createLabel(con,"Taxiway default width").setLayoutData(GridDataFactory.swtDefaults().create());
		Spinner taxiwayWidthSpinner = new Spinner(con, SWT.BORDER);
		taxiwayWidthSpinner.setMinimum(1);
		bindComponent(taxiwayWidthSpinner, options, "defaultTaxiwayWidth");
		UiUtil.setEnabledRecursive(aptConfigSection, createAirfieldsCheck.getSelection());
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
