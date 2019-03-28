package com.osm2xp.gui.views;

import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * Osm2xpNewsView.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class Osm2xpNewsView extends ViewPart implements IContextProvider {
	private FormToolkit toolkit;
	private Composite form;
	private Browser browser;

	public Osm2xpNewsView() {
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createComposite(parent);
		form.setLayout(new FillLayout(SWT.HORIZONTAL));

		browser = new Browser(form, SWT.NONE);

		toolkit.adapt(browser);
		toolkit.paintBordersFor(browser);

		FillLayout fl_parent = new FillLayout(SWT.HORIZONTAL);
		fl_parent.marginWidth = 5;
		fl_parent.spacing = 5;
		fl_parent.marginHeight = 5;
		parent.setLayout(fl_parent);

	}

	/**
	 * Passing the focus request to the form.
	 */
	public void setFocus() {
		form.setFocus();
		browser.setUrl("http://osm2xp.com");
	}

	/**
	 * Disposes the toolkit
	 */
	public void dispose() {
		toolkit.dispose();
		super.dispose();
	}

	@Override
	public int getContextChangeMask() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public IContext getContext(Object target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSearchExpression(Object target) {
		// TODO Auto-generated method stub
		return null;
	}

}
