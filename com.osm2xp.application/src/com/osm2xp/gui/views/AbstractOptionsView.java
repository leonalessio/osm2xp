package com.osm2xp.gui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import com.osm2xp.gui.Activator;

public abstract class AbstractOptionsView extends ViewPart {

	protected ScrolledForm form;
	protected FormToolkit toolkit;
	private String heading;
	private String imageId;
	
	public AbstractOptionsView(String heading, String imageId) {
		this.heading = heading;
		this.imageId = imageId;
	}
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		if (toolkit != null) {
			form.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID,
					imageId));
		}
		form.setText(heading);
		toolkit.decorateFormHeading(form.getForm());

		FillLayout fl_parent = new FillLayout(SWT.HORIZONTAL);
		fl_parent.marginWidth = 5;
		fl_parent.spacing = 5;
		fl_parent.marginHeight = 5;
		parent.setLayout(fl_parent);

		TableWrapLayout layout = new TableWrapLayout();
		layout.topMargin = 30;
		layout.verticalSpacing = 20;
		layout.bottomMargin = 10;
		layout.horizontalSpacing = 10;
		form.getBody().setLayout(layout);
		createFormControls();
	}

	protected abstract void createFormControls();

	@Override
	public void setFocus() {
		form.setFocus();
	}
	
	/**
	 * Disposes the toolkit
	 */
	public void dispose() {
		toolkit.dispose();
		super.dispose();
	}

}
