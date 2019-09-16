package com.osm2xp.gui.views;

import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

import com.osm2xp.gui.views.panels.xplane.DebugOptionsPanel;
import com.osm2xp.gui.views.panels.xplane.GeneralXPlaneOptionsPanel;
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
		 * General options
		 */
		Section sectionGeneralOptions = toolkit.createSection(form.getBody(),
				Section.TWISTIE | Section.EXPANDED | Section.TITLE_BAR);
		sectionGeneralOptions.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
		sectionGeneralOptions.setText("GeneralOptions");
		GeneralXPlaneOptionsPanel generalOptionsPanel = new GeneralXPlaneOptionsPanel(
				sectionGeneralOptions, SWT.BORDER);
		toolkit.adapt(generalOptionsPanel, true, true);
		sectionGeneralOptions.setClient(generalOptionsPanel);
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
		 * Debug options
		 */

		Section statsOptionsSection = toolkit.createSection(form.getBody(),
				Section.TWISTIE | Section.TITLE_BAR);
		statsOptionsSection.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
		statsOptionsSection.setText("Debug options (for experienced users!)");
		DebugOptionsPanel statsOptionsPanel = new DebugOptionsPanel(
				statsOptionsSection, SWT.BORDER);

		toolkit.adapt(statsOptionsPanel, true, true);
		statsOptionsSection.setClient(statsOptionsPanel);
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
