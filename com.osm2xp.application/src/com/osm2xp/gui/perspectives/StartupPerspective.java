package com.osm2xp.gui.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * StartupPerspective.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class StartupPerspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.setFixed(false);

	}

}
