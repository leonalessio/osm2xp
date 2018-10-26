package com.osm2xp.gui.views;

import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

import com.osm2xp.gui.views.panels.fsx.FsxOptionsPanel;

/**
 * FsxAdvancedOptionsView.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class FsxAdvancedOptionsView extends AbstractOptionsView implements
		IContextProvider {

	public FsxAdvancedOptionsView() {
		super("Advanced options","images/toolbarsIcons/advanced_32.png");
	}
	
	@Override
	protected void createFormControls() {
		/**
		 * Generated items
		 * 
		 */
		Section sectionFsxOption = toolkit.createSection(form.getBody(),
				Section.TWISTIE | Section.EXPANDED | Section.TITLE_BAR);
		sectionFsxOption.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
		sectionFsxOption.setText("Options");
		FsxOptionsPanel fsxOptionsPanel = new FsxOptionsPanel(sectionFsxOption,
				SWT.BORDER);
		toolkit.adapt(fsxOptionsPanel, true, true);
		sectionFsxOption.setClient(fsxOptionsPanel);
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
