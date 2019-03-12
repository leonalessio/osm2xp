package com.osm2xp.gui.views;

import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

import com.osm2xp.gui.views.panels.flyLegacy.WatchedTagsListPanel;

/**
 * FlyLegacyAdvancedOptionsView.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class FlyLegacyAdvancedOptionsView extends AbstractOptionsView implements
		IContextProvider {

	public FlyLegacyAdvancedOptionsView() {
		super("Advanced options","images/toolbarsIcons/advanced_32.png");
	}

	@Override
	protected void createFormControls() {
		/**
		 * Watched tags list
		 */

		Section watchedTagsListSection = toolkit.createSection(form.getBody(),
				Section.TWISTIE | Section.EXPANDED | Section.TITLE_BAR);
		watchedTagsListSection.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
		watchedTagsListSection.setText("Watched tags list");
		WatchedTagsListPanel watchedTagsListPanel = new WatchedTagsListPanel(
				watchedTagsListSection, SWT.BORDER);

		toolkit.adapt(watchedTagsListPanel, true, true);
		watchedTagsListSection.setClient(watchedTagsListPanel);
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
