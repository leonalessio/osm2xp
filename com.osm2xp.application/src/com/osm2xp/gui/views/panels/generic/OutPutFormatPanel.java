package com.osm2xp.gui.views.panels.generic;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.osm2xp.gui.views.panels.Osm2xpPanel;

/**
 * OutPutFormatPanel.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class OutPutFormatPanel extends Osm2xpPanel {
	private static Browser browser;

	public OutPutFormatPanel(final Composite parent, final int style) {
		super(parent, style);

	}

	@Override
	protected void initLayout() {

	}

	@Override
	protected void initComponents() {
		setLayout(new FillLayout(SWT.HORIZONTAL));
		browser = new Browser(this, SWT.NONE);

	}

	@Override
	protected void bindComponents() {

	}

	/**
	 * update description of the translator
	 */
	public static void updateBrowserUrl(String url) {
		browser.setUrl(url);
	}

	@Override
	protected void addComponentsListeners() {

	}
}
