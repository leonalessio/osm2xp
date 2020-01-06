package com.osm2xp.gui.views.panels;


import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Osm2xp root Panel.
 * 
 * @author Benjamin Blanchet, Dmitry Karpenko
 * 
 */
public abstract class Osm2xpPanel extends BindingPanel {

	public Osm2xpPanel(Composite parent, int style) {
		super(parent, style);
		initComponents();
		bindComponents();
		addComponentsListeners();
		initLayout();
	}


	/**
	 * Components initialization .
	 */
	protected abstract void initComponents();

	/**
	 * Components binding .
	 */
	protected abstract void bindComponents();

	/**
	 * Components action listeners. Override if necessary
	 */
	protected void addComponentsListeners() {

	}

	protected void enableComponents(boolean enable, Control... controls) {
		for (Control control : controls) {
			control.setEnabled(enable);
		}
	}
	
	/**
	 * Layout initialization. Override if needed
	 */
	protected void initLayout() {

	}
}
