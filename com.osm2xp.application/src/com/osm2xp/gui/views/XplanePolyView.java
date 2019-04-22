package com.osm2xp.gui.views;

import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

import com.osm2xp.gui.views.panels.xplane.XplanePolyRulesPanel;

/**
 * Xplane Dpared poly configuration view
 * 
 * @author 32kda
 * 
 */
public class XplanePolyView extends AbstractOptionsView {
	
	public static final String ID = "com.osm2xp.viewPolysTab";

	public XplanePolyView() {
		super("Draped poly options", "images/toolbarsIcons/polygon_32.png");
	}
	
	@Override
	protected void createFormControls() {
		/**
		 * Poly rules
		 */

		Section sectionRules = toolkit.createSection(form.getBody(),
				Section.TWISTIE | Section.EXPANDED | Section.TITLE_BAR);
		sectionRules.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB, 1, 1));
		
		

		sectionRules.setText("Draped poly rules");
		XplanePolyRulesPanel rulesPanel = new XplanePolyRulesPanel(sectionRules, SWT.BORDER);
		toolkit.adapt(rulesPanel, true, true);
		sectionRules.setClient(rulesPanel);
	}

}
