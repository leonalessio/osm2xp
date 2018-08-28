package com.osm2xp.gui.views.panels.xplane;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import com.osm2xp.gui.views.panels.Osm2xpPanel;
import com.osm2xp.utils.helpers.XplaneOptionsHelper;

/**
 * BuildingsHeightPanel.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class BuildingsHeightPanel extends Osm2xpPanel {

	private Group grpRes;
	private Spinner spinResMin;
	private Spinner spinResMax;
	private Spinner spinBldMin;
	private Spinner spinBldMax;
	private Group grpBuildings;
	private Spinner spinLevelHeight;

	public BuildingsHeightPanel(final Composite parent, final int style) {
		super(parent, style);

	}

	@Override
	protected void initLayout() {
		final GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 15;
		gridLayout.verticalSpacing = 15;
		gridLayout.horizontalSpacing = 15;
		setLayout(gridLayout);
		final GridLayout gridRes = new GridLayout(4, false);
		gridRes.marginHeight = 20;
		gridRes.horizontalSpacing = 15;
		gridRes.verticalSpacing = 15;
		grpRes.setLayout(gridRes);
		grpRes.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1,
				1));
		final GridLayout gridBuildings = new GridLayout(4, false);
		gridBuildings.marginHeight = 20;
		gridBuildings.horizontalSpacing = 15;
		gridBuildings.verticalSpacing = 15;
		grpBuildings.setLayout(gridBuildings);

	}

	@Override
	protected void initComponents() {
		Group grpCommon = new Group(this, SWT.NONE); 
		grpCommon.setText("Common");
		grpCommon.setLayout(new GridLayout(2, false));
		GridDataFactory.fillDefaults().span(2,1).applyTo(grpCommon);
		Label levelHeightLbl = new Label(grpCommon, SWT.NONE);
		levelHeightLbl.setText("Building Level height, m:");
		spinLevelHeight = new Spinner(grpCommon, SWT.BORDER);
		spinLevelHeight.setDigits(1);
		
		grpRes = new Group(this, SWT.NONE);
		grpRes.setText("Residential height (meters)");
		Label labelResMin = new Label(grpRes, SWT.NONE);
		labelResMin.setText("Min :");
		spinResMin = new Spinner(grpRes, SWT.BORDER);
		Label labelResMax = new Label(grpRes, SWT.NONE);
		labelResMax.setText("Max :");
		spinResMax = new Spinner(grpRes, SWT.BORDER);
		grpBuildings = new Group(this, SWT.NONE);
		grpBuildings.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false,
				true, 1, 1));
		grpBuildings.setText("Commercial / Industrial height (meters)");
		final Label labelBldMin = new Label(grpBuildings, SWT.NONE);
		labelBldMin.setText("Min :");
		spinBldMin = new Spinner(grpBuildings, SWT.BORDER);
		final Label labelBldMax = new Label(grpBuildings, SWT.NONE);
		labelBldMax.setText("Max :");
		spinBldMax = new Spinner(grpBuildings, SWT.BORDER);
	}

	@Override
	protected void bindComponents() {
		bindComponent(spinResMin, XplaneOptionsHelper.getOptions(),
				"residentialMin");
		bindComponent(spinBldMin, XplaneOptionsHelper.getOptions(),
				"buildingMin");
		bindComponent(spinBldMax, XplaneOptionsHelper.getOptions(),
				"buildingMax");
		bindComponent(spinResMax, XplaneOptionsHelper.getOptions(),
				"residentialMax");
		bindSpinnerToDouble(spinLevelHeight, XplaneOptionsHelper.getOptions(),
				"levelHeight",1);

	}

	@Override
	protected void addComponentsListeners() {

	}
}
