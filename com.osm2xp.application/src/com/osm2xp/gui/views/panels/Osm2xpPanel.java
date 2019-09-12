package com.osm2xp.gui.views.panels;


import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Widget;

/**
 * Osm2xp root Panel.
 * 
 * @author Benjamin Blanchet, Dmitry Karpenko
 * 
 */
public abstract class Osm2xpPanel extends Composite {

	protected final DataBindingContext bindingContext = new DataBindingContext();

	public Osm2xpPanel(Composite parent, int style) {
		super(parent, style);
		initComponents();
		bindComponents();
		addComponentsListeners();
		initLayout();
	}

	/**
	 * Bind a widget to a bean property
	 * 
	 * @param component
	 * @param bean
	 * @param property
	 */
	@SuppressWarnings("unchecked")
	protected void bindComponent(Widget component, Object bean, String property) {
		if (component instanceof Spinner || component instanceof Button) {
			bindingContext.bindValue(WidgetProperties.selection().observe(component),
					PojoProperties.value(property).observe(bean));
		} else if (component instanceof Combo) {
			bindingContext.bindValue(WidgetProperties.text().observe(component),
					PojoProperties.value(property).observe(bean));
		}
	}

	@SuppressWarnings("unchecked")
	protected void bindSpinnerToDouble(Spinner spinner, Object bean, String property, int digits) {
		int factor = (int) Math.pow(10, digits);
		bindingContext.bindValue(WidgetProperties.selection().observe(spinner),
				PojoProperties.value(property).observe(bean),
				UpdateValueStrategy.create(new Converter(int.class, double.class) {

					@Override
					public Object convert(Object fromObject) {
						return (Integer) fromObject * 1.0 / factor;
					}
				}), UpdateValueStrategy.create(new Converter(double.class, int.class) {

					@Override
					public Object convert(Object fromObject) {
						return (int) ((Double) fromObject * factor);
					}
				}));
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
