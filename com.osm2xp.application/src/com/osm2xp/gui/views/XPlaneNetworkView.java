package com.osm2xp.gui.views;

import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.gui.views.panels.Osm2xpPanel;

public class XPlaneNetworkView extends AbstractOptionsView {
	
	protected VerifyListener onlyDigitsVerifyListener = new VerifyListener() {
		@Override
		public void verifyText(VerifyEvent e) {
			String string = e.text;
			char[] chars = new char[string.length()];
			string.getChars(0, chars.length, chars, 0);
			for (int i = 0; i < chars.length; i++) {
				if (!('0' <= chars[i] && chars[i] <= '9')) {
					e.doit = false;
					return;
				}
			}
		}
	};
	
	public XPlaneNetworkView() {
		super("Roads/Network", "images/toolbarsIcons/road_32.png");
	}	

	@Override
	protected void createFormControls() {
		/**
		 * Generated items
		 */
		Section sectionGeneratedItems = toolkit.createSection(form.getBody(),
				Section.TWISTIE | Section.EXPANDED | Section.TITLE_BAR);
		sectionGeneratedItems.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
		sectionGeneratedItems.setText("Generated items");
		Osm2xpPanel scGeneratedItemsPanel = new Osm2xpPanel(sectionGeneratedItems, SWT.BORDER) {
			@SuppressWarnings("unchecked")
			@Override
			protected void initComponents() {
				GridLayout gridLayout = new GridLayout(2, false);
				gridLayout.verticalSpacing = 15;
				gridLayout.horizontalSpacing = 15;
				gridLayout.marginHeight = 15;
				setLayout(gridLayout);
				
				Button btnGenerateRoads = new Button(this, SWT.CHECK);
				btnGenerateRoads.setText("Generate Roads");
				GridDataFactory.fillDefaults().applyTo(btnGenerateRoads);
				Button btnGenerateRail = new Button(this, SWT.CHECK);
				btnGenerateRail.setText("Generate Railways");
				GridDataFactory.fillDefaults().applyTo(btnGenerateRail);
				Button btnGeneratePower = new Button(this, SWT.CHECK);
				btnGeneratePower.setText("Generate Powerlines");
				GridDataFactory.fillDefaults().applyTo(btnGeneratePower);

				bindingContext.bindValue(WidgetProperties.selection().observe(btnGenerateRoads),		
						PojoProperties.value("generateRoads").observe(XPlaneOptionsProvider.getOptions()));
				bindingContext.bindValue(WidgetProperties.selection().observe(btnGenerateRail),		
						PojoProperties.value("generateRailways").observe(XPlaneOptionsProvider.getOptions()));
				bindingContext.bindValue(WidgetProperties.selection().observe(btnGeneratePower),		
						PojoProperties.value("generatePowerlines").observe(XPlaneOptionsProvider.getOptions()));
			}
			
		};
				
		toolkit.adapt(scGeneratedItemsPanel, true, true);
		sectionGeneratedItems.setClient(scGeneratedItemsPanel);
		
		Section sectionRoadProperties = toolkit.createSection(form.getBody(),
				Section.TWISTIE | Section.EXPANDED | Section.TITLE_BAR);
		sectionRoadProperties.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
		sectionRoadProperties.setText("Roads");
		Osm2xpPanel scRoadsPanel = new Osm2xpPanel(sectionRoadProperties, SWT.BORDER) {
			
			@Override
			protected void initComponents() {
				GridLayout gridLayout = new GridLayout(4, false);
				gridLayout.verticalSpacing = 15;
				gridLayout.horizontalSpacing = 15;
				gridLayout.marginHeight = 15;
				setLayout(gridLayout);
				
				toolkit.createLabel(this,"City, 3+ lanes highway road type").setLayoutData(GridDataFactory.swtDefaults().create());
				Text city3LaneHighwayRoadType = new Text(this, SWT.BORDER);
				city3LaneHighwayRoadType.addVerifyListener(onlyDigitsVerifyListener);
				bindTextToInt(city3LaneHighwayRoadType, XPlaneOptionsProvider.getOptions(), "city3LaneHighwayRoadType");
				
				toolkit.createLabel(this,"Country, 3+ lanes highway road type").setLayoutData(GridDataFactory.swtDefaults().create());
				Text country3LaneHighwayRoadType = new Text(this, SWT.BORDER);
				country3LaneHighwayRoadType.addVerifyListener(onlyDigitsVerifyListener);
				bindTextToInt(country3LaneHighwayRoadType, XPlaneOptionsProvider.getOptions(), "country3LaneHighwayRoadType");
				
				toolkit.createLabel(this,"City, 2 lanes highway road type").setLayoutData(GridDataFactory.swtDefaults().create());
				Text city2LaneHighwayRoadType = new Text(this, SWT.BORDER);
				city2LaneHighwayRoadType.addVerifyListener(onlyDigitsVerifyListener);
				bindTextToInt(city2LaneHighwayRoadType, XPlaneOptionsProvider.getOptions(), "city2LaneHighwayRoadType");
				
				toolkit.createLabel(this,"Country, 2 lanes highway road type").setLayoutData(GridDataFactory.swtDefaults().create());
				Text country2LaneHighwayRoadType = new Text(this, SWT.BORDER);
				country2LaneHighwayRoadType.addVerifyListener(onlyDigitsVerifyListener);
				bindTextToInt(country2LaneHighwayRoadType, XPlaneOptionsProvider.getOptions(), "country2LaneHighwayRoadType");
				
				toolkit.createLabel(this,"City, 2 lanes, default road type").setLayoutData(GridDataFactory.swtDefaults().create());
				Text cityRoadType = new Text(this, SWT.BORDER);
				cityRoadType.addVerifyListener(onlyDigitsVerifyListener);
				bindTextToInt(cityRoadType, XPlaneOptionsProvider.getOptions(), "cityRoadType");
				
				toolkit.createLabel(this,"Country, 2 lanes, default road type").setLayoutData(GridDataFactory.swtDefaults().create());
				Text countryRoadType = new Text(this, SWT.BORDER);
				countryRoadType.addVerifyListener(onlyDigitsVerifyListener);
				bindTextToInt(countryRoadType, XPlaneOptionsProvider.getOptions(), "countryRoadType");
				
				toolkit.createLabel(this,"One lane road type").setLayoutData(GridDataFactory.swtDefaults().create());
				Text oneLaneRoadType = new Text(this, SWT.BORDER);
				oneLaneRoadType.addVerifyListener(onlyDigitsVerifyListener);
				bindTextToInt(oneLaneRoadType, XPlaneOptionsProvider.getOptions(), "oneLaneRoadType");
			}
			
		};
		
		toolkit.adapt(scRoadsPanel, true, true);
		sectionRoadProperties.setClient(scRoadsPanel);
	}
}
