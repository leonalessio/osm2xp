package com.osm2xp.gui.views;

import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

import com.osm2xp.gui.views.panels.xplane.ObjectByPolyPanel;
import com.osm2xp.gui.views.panels.xplane.ObjectOptionsPanel;
import com.osm2xp.gui.views.panels.xplane.XplaneObjectsRulesPanel;

/**
 * Xplane3DObjectsView.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class Xplane3DObjectsView extends AbstractOptionsView implements IContextProvider {

	public Xplane3DObjectsView() {
		super("3D Objects options", "images/toolbarsIcons/objects_32.png");
	}
	
	@Override
	protected void createFormControls() {
		/**
		 * objects rules
		 */

		Section sectionObjectsRules = toolkit.createSection(form.getBody(),
				Section.TWISTIE | Section.EXPANDED | Section.TITLE_BAR);
		sectionObjectsRules.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB, 1, 1));

		sectionObjectsRules.setText("Object generation rules");
		XplaneObjectsRulesPanel objectsRulesPanel = new XplaneObjectsRulesPanel(
				sectionObjectsRules, SWT.BORDER);
		toolkit.adapt(objectsRulesPanel, true, true);
		sectionObjectsRules.setClient(objectsRulesPanel);
		
		Section sectionGlobalObjectOptions = toolkit.createSection(form.getBody(),
				Section.TWISTIE | Section.EXPANDED | Section.TITLE_BAR);
		sectionGlobalObjectOptions.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB, 1, 1));
		
		sectionGlobalObjectOptions.setText("Global object placement options");
		ObjectOptionsPanel objectOptionsPanel = new ObjectOptionsPanel(sectionGlobalObjectOptions, SWT.BORDER);
		toolkit.adapt(objectOptionsPanel, true, true);
		sectionGlobalObjectOptions.setClient(objectOptionsPanel);
		
		Section sectionBuildingObjectOptions = toolkit.createSection(form.getBody(),
				Section.TWISTIE | Section.EXPANDED | Section.TITLE_BAR);
		sectionBuildingObjectOptions.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB, 1, 1));
		
		sectionBuildingObjectOptions.setText("Object by polygon selection options");
		ObjectByPolyPanel panel = new ObjectByPolyPanel(sectionBuildingObjectOptions, SWT.BORDER);
		toolkit.adapt(panel, true, true);
		sectionBuildingObjectOptions.setClient(panel);
	}

	@Override
	public int getContextChangeMask() {
		return 0;
	}

	@Override
	public IContext getContext(Object target) {
		return HelpSystem.getContext("com.osm2xp.objectsHelpContext");
	}

	@Override
	public String getSearchExpression(Object target) {
		return "object";
	}

}
