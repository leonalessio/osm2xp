package com.osm2xp.gui.views;

import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

import com.osm2xp.gui.views.panels.xplane.DebugOptionsPanel;
import com.osm2xp.gui.views.panels.xplane.GeneralXPlaneOptionsPanel;
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
		Section sectionGeneralOptions = createSection("General Options", true);
		GeneralXPlaneOptionsPanel generalOptionsPanel = new GeneralXPlaneOptionsPanel(
				sectionGeneralOptions, SWT.BORDER);
		toolkit.adapt(generalOptionsPanel, true, true);
		sectionGeneralOptions.setClient(generalOptionsPanel);

		/**
		 * Scenery exclusions
		 */
		Section sectionExclusions = createSection("Scenery exclusions", true);
		SceneryExclusionsPanel sceneryExclusionsPanel = new SceneryExclusionsPanel(
				sectionExclusions, SWT.BORDER);
		toolkit.adapt(sceneryExclusionsPanel, true, true);
		sectionExclusions.setClient(sceneryExclusionsPanel);

		/**
		 * Scenery options
		 */
		Section sectionSceneryOptions = createSection("Scenery options", false);
		SceneryOptionsPanel sceneryOptionsPanel = new SceneryOptionsPanel(
				sectionSceneryOptions, SWT.BORDER);
		toolkit.adapt(sceneryOptionsPanel, true, true);
		sectionSceneryOptions.setClient(sceneryOptionsPanel);
		/**
		 * Debug options
		 */

		Section statsOptionsSection = createSection("Debug options (for experienced users!)", false);
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
