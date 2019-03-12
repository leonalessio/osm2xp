package com.osm2xp.gui.views;

import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

import com.osm2xp.gui.views.panels.wavefront.WaveFrontExportOptionsPanel;

/**
 * WavefrontAdvancedOptionsView.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class WavefrontAdvancedOptionsView extends AbstractOptionsView implements
		IContextProvider {
	public WavefrontAdvancedOptionsView() {
		super("Advanced options","images/toolbarsIcons/advanced_32.png");
	}
	
	@Override
	protected void createFormControls() {
		/**
		 * Wavefront export options
		 */

		Section wavefrontExportSection = toolkit.createSection(form.getBody(),
				Section.TWISTIE | Section.EXPANDED | Section.TITLE_BAR);
		wavefrontExportSection.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
		wavefrontExportSection.setText("WaveFront (.obj) export options");
		WaveFrontExportOptionsPanel objExportOptionsPanel = new WaveFrontExportOptionsPanel(
				wavefrontExportSection, SWT.BORDER);

		toolkit.adapt(objExportOptionsPanel, true, true);
		wavefrontExportSection.setClient(objExportOptionsPanel);		
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
