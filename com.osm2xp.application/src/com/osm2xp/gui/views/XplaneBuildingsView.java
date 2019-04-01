package com.osm2xp.gui.views;

import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

import com.osm2xp.gui.views.panels.Osm2xpPanel;
import com.osm2xp.gui.views.panels.xplane.BuildingsHeightPanel;
import com.osm2xp.gui.views.panels.xplane.FacadeSetPanel;
import com.osm2xp.gui.views.panels.xplane.FacadesExclusionsPanel;
import com.osm2xp.gui.views.panels.xplane.FacadesRulesPanel;

/**
 * XplaneBuildingsView.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class XplaneBuildingsView extends AbstractOptionsView implements IContextProvider {

	public XplaneBuildingsView() {
		super("Building facades options", "images/toolbarsIcons/house_32.png");
	}

	@Override
	protected void createFormControls() {
		/**
		 * Facade set
		 */

		Section sectionFacadeSet = toolkit.createSection(form.getBody(),
				Section.TWISTIE | Section.EXPANDED | Section.TITLE_BAR);
		sectionFacadeSet.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
		sectionFacadeSet.setText("Facade sets");
		Osm2xpPanel facadeSetPanel = new FacadeSetPanel(sectionFacadeSet,
				SWT.BORDER);

		// display error if there is no facade set found
//		if (!new File(Osm2xpConstants.FACADES_SETS_PATH).exists()
//				|| (new File(Osm2xpConstants.FACADES_SETS_PATH).listFiles().length < 0)) {
//			form.setMessage("No facades sets found", IMessageProvider.ERROR);
//		}
		toolkit.adapt(facadeSetPanel, true, true);
		sectionFacadeSet.setClient(facadeSetPanel);

		/**
		 * Building height
		 */
		Section sectionBuildingHeight = toolkit.createSection(form.getBody(),
				Section.TWISTIE | Section.TITLE_BAR);
		sectionBuildingHeight.setLayoutData(new TableWrapData(
				TableWrapData.FILL, TableWrapData.TOP, 1, 1));

		sectionBuildingHeight.setText("Buildings height settings");
		BuildingsHeightPanel buildingsHeightPanel3 = new BuildingsHeightPanel(
				sectionBuildingHeight, SWT.BORDER);
		toolkit.adapt(buildingsHeightPanel3, true, true);
		sectionBuildingHeight.setClient(buildingsHeightPanel3);

		/**
		 * Facade exclusions
		 */

		Section sectionFacadeExclusions = toolkit.createSection(form.getBody(),
				Section.TWISTIE | Section.TITLE_BAR);
		sectionFacadeExclusions.setLayoutData(new TableWrapData(
				TableWrapData.FILL, TableWrapData.TOP, 1, 1));

		sectionFacadeExclusions.setText("buildings exclusions");
		FacadesExclusionsPanel facadesExclusionsPanel = new FacadesExclusionsPanel(
				sectionFacadeExclusions, SWT.BORDER);
		toolkit.adapt(facadesExclusionsPanel, true, true);
		sectionFacadeExclusions.setClient(facadesExclusionsPanel);

		/**
		 * Facade rules
		 */

		Section sectionFacadeRules = toolkit.createSection(form.getBody(),
				Section.TWISTIE | Section.TITLE_BAR);
		sectionFacadeRules.setLayoutData(new TableWrapData(TableWrapData.FILL,
				TableWrapData.TOP, 1, 1));

		sectionFacadeRules.setText("facades rules");
		FacadesRulesPanel facadesRulesPanel = new FacadesRulesPanel(
				sectionFacadeRules, SWT.BORDER);
		toolkit.adapt(facadesRulesPanel, true, true);
		sectionFacadeRules.setClient(facadesRulesPanel);
	}
	
	@Override
	public int getContextChangeMask() {
		return 0;
	}

	@Override
	public IContext getContext(Object target) {
		return HelpSystem.getContext("com.osm2xp.buildingsHelpContext");
	}

	@Override
	public String getSearchExpression(Object target) {
		return "buildings";
	}
}
