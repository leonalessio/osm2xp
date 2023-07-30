package com.osm2xp.gui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class BrowserView extends ViewPart {
	
	public static final String ID = "com.osm2xp.application.BrowserView";

	private Browser browser;

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		browser = new Browser(parent, SWT.NONE);
	}

	@Override
	public void setFocus() {
		browser.setFocus();
	}

	public void setUrl(String url) {
		browser.setUrl(url);
	}
}
