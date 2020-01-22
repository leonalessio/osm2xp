package com.osm2xp.gui.views.panels;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

public class BindingPanel extends Composite {

	protected final DataBindingContext bindingContext = new DataBindingContext();
	
	public BindingPanel(Composite parent, int style) {
		super(parent, style);
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
		} else if (component instanceof Combo || component instanceof Text) {
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
	
	@SuppressWarnings("unchecked")
	protected void bindTextToInt(Text widget, Object bean, String property) {
		bindingContext.bindValue(WidgetProperties.text(SWT.Modify).observe(widget),
				PojoProperties.value(property).observe(bean),
				UpdateValueStrategy.create(new Converter(String.class, int.class) {
					
					@Override
					public Object convert(Object fromObject) {
						return Integer.parseInt(fromObject.toString());
					}
				}), UpdateValueStrategy.create(new Converter(int.class, String.class) {
					
					@Override
					public Object convert(Object fromObject) {
						return "" + fromObject.toString();
					}
				}));
	}
}
