package com.osm2xp.gui.views;

import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

import com.osm2xp.gui.views.panels.xplane.XplaneLightsRulesPanel;

/**
 * XplaneLightsView.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class XplaneLightsView extends AbstractOptionsView implements IContextProvider {

	public XplaneLightsView() {
		super("3D Objects options", "images/toolbarsIcons/objects_32.png");
	}
	
	@Override
	protected void createFormControls() {
		/**
		 * lights
		 */

		Section sectionLights = toolkit.createSection(form.getBody(),
				Section.TWISTIE | Section.EXPANDED | Section.TITLE_BAR);
		sectionLights.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB,
				TableWrapData.TOP, 1, 1));

		sectionLights.setText("Lights");
		XplaneLightsRulesPanel lightsPanel = new XplaneLightsRulesPanel(
				sectionLights, SWT.BORDER);
		toolkit.adapt(lightsPanel, true, true);
		sectionLights.setClient(lightsPanel);		
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
