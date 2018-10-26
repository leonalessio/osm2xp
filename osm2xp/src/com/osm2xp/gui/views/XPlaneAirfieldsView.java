package com.osm2xp.gui.views;

import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.ui.forms.widgets.TableWrapData;

import com.osm2xp.gui.views.panels.xplane.AirfieldsPanel;

public class XPlaneAirfieldsView extends AbstractOptionsView implements IContextProvider {

	public XPlaneAirfieldsView() {
		super("Airfields options", "images/toolbarsIcons/airport_32.png");
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

	@Override
	protected void createFormControls() {
		AirfieldsPanel airfieldsPanel = new AirfieldsPanel(form.getBody());
		airfieldsPanel.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
	}

}
