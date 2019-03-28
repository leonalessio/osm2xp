package com.osm2xp.gui.views;

import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

import com.osm2xp.gui.views.panels.xplane.GeneratedItemsPanel;
import com.osm2xp.gui.views.panels.xplane.SceneryExclusionsPanel;
import com.osm2xp.gui.views.panels.xplane.SceneryOptionsPanel;

/**
 * XplaneAdvancedOptionsView.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class XplaneAdvancedOptionsView extends AbstractOptionsView implements
		IContextProvider {

	public XplaneAdvancedOptionsView() {
		super("Advanced options", "images/toolbarsIcons/advanced_32.png");
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
		GeneratedItemsPanel scGeneratedItemsPanel = new GeneratedItemsPanel(
				sectionGeneratedItems, SWT.BORDER);
		toolkit.adapt(scGeneratedItemsPanel, true, true);
		sectionGeneratedItems.setClient(scGeneratedItemsPanel);

		/**
		 * Scenery exclusions
		 */
		Section sectionExclusions = toolkit.createSection(form.getBody(),
				Section.TWISTIE | Section.TITLE_BAR);
		sectionExclusions.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
		sectionExclusions.setText("Scenery exclusions");
		SceneryExclusionsPanel sceneryExclusionsPanel = new SceneryExclusionsPanel(
				sectionExclusions, SWT.BORDER);
		toolkit.adapt(sceneryExclusionsPanel, true, true);
		sectionExclusions.setClient(sceneryExclusionsPanel);

		/**
		 * Scenery options
		 */
		Section sectionSceneryOptions = toolkit.createSection(form.getBody(),
				Section.TWISTIE | Section.TITLE_BAR);
		sectionSceneryOptions.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
		sectionSceneryOptions.setText("Scenery options");
		SceneryOptionsPanel sceneryOptionsPanel = new SceneryOptionsPanel(
				sectionSceneryOptions, SWT.BORDER);
		toolkit.adapt(sceneryOptionsPanel, true, true);
		sectionSceneryOptions.setClient(sceneryOptionsPanel);
		/**
		 * Stats options - deprecated
		 */
//
//		Section statsOptionsSection = toolkit.createSection(form.getBody(),
//				Section.TWISTIE | Section.TITLE_BAR);
//		statsOptionsSection.setLayoutData(new TableWrapData(
//				TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
//		statsOptionsSection.setText("Stats and Debug options");
//		StatsOptionsPanel statsOptionsPanel = new StatsOptionsPanel(
//				statsOptionsSection, SWT.BORDER);
//
//		toolkit.adapt(statsOptionsPanel, true, true);
//		statsOptionsSection.setClient(statsOptionsPanel);
	}
	
	@Override
	public int getContextChangeMask() {
		return 0;
	}

	@Override
	public IContext getContext(Object target) {
		return HelpSystem.getContext("com.osm2xp.advancedHelpContext");
	}

	@Override
	public String getSearchExpression(Object target) {
		return "advanced";
	}
	
}
