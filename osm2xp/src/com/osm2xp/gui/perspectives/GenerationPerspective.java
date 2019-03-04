package com.osm2xp.gui.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class GenerationPerspective implements IGenerationModeProvider, IPerspectiveFactory{
	
	private final String generationMode;

	public GenerationPerspective(String generationMode) {
		this.generationMode = generationMode;
	}

	@Override
	public String getGenerationMode() {
		return generationMode;
	}

	@Override
	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.setFixed(false);		
	}

}
