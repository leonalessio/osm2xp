package com.osm2xp.gui.views;

import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

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
				TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));

		sectionObjectsRules.setText("objects rules");
		XplaneObjectsRulesPanel objectsRulesPanel = new XplaneObjectsRulesPanel(
				sectionObjectsRules, SWT.BORDER);
		toolkit.adapt(objectsRulesPanel, true, true);
		sectionObjectsRules.setClient(objectsRulesPanel);
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
