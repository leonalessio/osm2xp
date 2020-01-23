package com.osm2xp.gui.views.panels;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public abstract class CheckBoxPanel extends Osm2xpPanel {
	
	public CheckBoxPanel(Composite parent) {
		super(parent, SWT.BORDER);
	}
	
	@Override
	protected void initLayout() {
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.verticalSpacing = 15;
		gridLayout.horizontalSpacing = 15;
		gridLayout.marginHeight = 15;
		setLayout(gridLayout);
	}
}
