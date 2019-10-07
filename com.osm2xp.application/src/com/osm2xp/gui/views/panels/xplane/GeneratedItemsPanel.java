package com.osm2xp.gui.views.panels.xplane;

import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.gui.views.panels.Osm2xpPanel;

/**
 * GeneratedItemsPanel.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class GeneratedItemsPanel extends Osm2xpPanel {

	private Button btnGenerateBuildings;
	private Button btnGenerateObjects;
	private Button btnGenerateObjBuildings;
	private Button btnGenerateForests;
	private Button btnGeneratePolys;
	private Button btnGenerateRoads;
	private Button btnGenerateRail;
	private Button btnGeneratePower;
	private Button btnGenerateFence;
	private Button btnGenerateTanks;
	private Button btnGenerateChimneys;
	private Button btnGenerateCoolingTowers;
	private Button btnGenerateBridges;

	public GeneratedItemsPanel(final Composite parent, final int style) {
		super(parent, style);
	}

	@Override
	protected void initComponents() {
		btnGenerateBuildings = new Button(this, SWT.CHECK);
		btnGenerateBuildings.setText("Generate Buildings(facades)");
		btnGenerateBuildings.setToolTipText("Generate buildings using X-Plane Facades feature. It allows to generate simple building model based on almost every contour on map.");
		GridDataFactory.fillDefaults().applyTo(btnGenerateBuildings);
		btnGenerateObjBuildings = new Button(this, SWT.CHECK);
		btnGenerateObjBuildings.setToolTipText("Generate buildings by choosing a model with closest size  from objects/<building type> folder");
		btnGenerateObjBuildings.setText("Generate Buildings(objects)");
		GridDataFactory.fillDefaults().applyTo(btnGenerateObjBuildings);
		btnGenerateObjects = new Button(this, SWT.CHECK);
		btnGenerateObjects.setText("Generate 3D objects");
		btnGenerateObjects.setToolTipText("Generate 3D objects based on rules specified on 3D Objects tab");
		GridDataFactory.fillDefaults().applyTo(btnGenerateObjects);
		btnGenerateForests = new Button(this, SWT.CHECK);
		btnGenerateForests.setText("Generate Forests");
		GridDataFactory.fillDefaults().applyTo(btnGenerateForests);
		btnGeneratePolys= new Button(this, SWT.CHECK);
		btnGeneratePolys.setText("Generate Draped Polygons");
		btnGeneratePolys.setToolTipText("Generate draped polygons - for items like parking areas and other paved surfaces");
		GridDataFactory.fillDefaults().applyTo(btnGeneratePolys);
		btnGenerateRoads = new Button(this, SWT.CHECK);
		btnGenerateRoads.setText("Generate Roads");
		GridDataFactory.fillDefaults().applyTo(btnGenerateRoads);
		btnGenerateRail = new Button(this, SWT.CHECK);
		btnGenerateRail.setText("Generate Railways");
		GridDataFactory.fillDefaults().applyTo(btnGenerateRail);
		btnGeneratePower= new Button(this, SWT.CHECK);
		btnGeneratePower.setText("Generate Powerlines");
		GridDataFactory.fillDefaults().applyTo(btnGeneratePower);
		btnGenerateFence= new Button(this, SWT.CHECK);
		btnGenerateFence.setText("Generate Fence");
		GridDataFactory.fillDefaults().applyTo(btnGenerateFence);
		
		btnGenerateTanks= new Button(this, SWT.CHECK);
		btnGenerateTanks.setText("Generate Tanks/Gasometers");
		btnGenerateTanks.setToolTipText("Generate Tanks/Gasometers using special facade");
		GridDataFactory.fillDefaults().applyTo(btnGenerateTanks);
		
		btnGenerateChimneys = new Button(this, SWT.CHECK);
		btnGenerateChimneys.setText("Generate Chimneys");
		btnGenerateChimneys.setToolTipText("Generate Chimneys by selecting best-fit model");
		GridDataFactory.fillDefaults().applyTo(btnGenerateChimneys);
		
		btnGenerateCoolingTowers = new Button(this, SWT.CHECK);
		btnGenerateCoolingTowers.setText("Generate Cooling Towers");
		btnGenerateCoolingTowers.setToolTipText("Generate Cooling Towers by selecting best-fit model");
		GridDataFactory.fillDefaults().applyTo(btnGenerateCoolingTowers);
		
		btnGenerateBridges = new Button(this, SWT.CHECK);
		btnGenerateBridges.setText("Generate bridges");
		btnGenerateBridges.setToolTipText("Generate bridges for roads and railways.");
		GridDataFactory.fillDefaults().applyTo(btnGenerateBridges);
	
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void bindComponents() {
		bindingContext.bindValue(WidgetProperties.selection().observe(btnGenerateBuildings),		
				PojoProperties.value("generateBuildings").observe(XPlaneOptionsProvider.getOptions()));
		bindingContext.bindValue(WidgetProperties.selection().observe(btnGenerateObjects),		
				PojoProperties.value("generateObj").observe(XPlaneOptionsProvider.getOptions()));
		bindingContext.bindValue(WidgetProperties.selection().observe(btnGenerateObjBuildings),		
				PojoProperties.value("generateObjBuildings").observe(XPlaneOptionsProvider.getOptions()));
		bindingContext.bindValue(WidgetProperties.selection().observe(btnGenerateForests),		
				PojoProperties.value("generateFor").observe(XPlaneOptionsProvider.getOptions()));
		bindingContext.bindValue(WidgetProperties.selection().observe(btnGeneratePolys),		
				PojoProperties.value("generatePolys").observe(XPlaneOptionsProvider.getOptions()));
//		bindingContext.bindValue(SWTObservables
//				.observeSelection(btnGenerateBuildings), PojoObservables
//				.observeValue(XPlaneOptionsProvider.getOptions(),
//						"generateBuildings"));
//		bindingContext.bindValue(SWTObservables
//				.observeSelection(btnGenerateObjects), PojoObservables
//				.observeValue(XPlaneOptionsProvider.getOptions(), "generateObj"));
//		bindingContext.bindValue(SWTObservables
//				.observeSelection(btnGenerateForests), PojoObservables
//				.observeValue(XPlaneOptionsProvider.getOptions(), "generateFor"));
//		bindingContext.bindValue(SWTObservables
//				.observeSelection(btnGenerateStreetLights), PojoObservables
//				.observeValue(XPlaneOptionsProvider.getOptions(),
//						"generateStreetLights"));
		bindingContext.bindValue(WidgetProperties.selection().observe(btnGenerateRoads),		
				PojoProperties.value("generateRoads").observe(XPlaneOptionsProvider.getOptions()));
		bindingContext.bindValue(WidgetProperties.selection().observe(btnGenerateRail),		
				PojoProperties.value("generateRailways").observe(XPlaneOptionsProvider.getOptions()));
		bindingContext.bindValue(WidgetProperties.selection().observe(btnGeneratePower),		
				PojoProperties.value("generatePowerlines").observe(XPlaneOptionsProvider.getOptions()));
		bindingContext.bindValue(WidgetProperties.selection().observe(btnGenerateFence),		
				PojoProperties.value("generateFence").observe(XPlaneOptionsProvider.getOptions()));
		bindingContext.bindValue(WidgetProperties.selection().observe(btnGenerateTanks),		
				PojoProperties.value("generateTanks").observe(XPlaneOptionsProvider.getOptions()));
		bindingContext.bindValue(WidgetProperties.selection().observe(btnGenerateChimneys),		
				PojoProperties.value("generateChimneys").observe(XPlaneOptionsProvider.getOptions()));
		bindingContext.bindValue(WidgetProperties.selection().observe(btnGenerateCoolingTowers),		
				PojoProperties.value("generateCoolingTowers").observe(XPlaneOptionsProvider.getOptions()));
		bindingContext.bindValue(WidgetProperties.selection().observe(btnGenerateBridges),		
				PojoProperties.value("generateBridges").observe(XPlaneOptionsProvider.getOptions()));
	}

	@Override
	protected void addComponentsListeners() {

	}

	@Override
	protected void initLayout() {
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.verticalSpacing = 15;
		gridLayout.horizontalSpacing = 15;
		gridLayout.marginHeight = 15;
		setLayout(gridLayout);
	}

}
