package com.osm2xp.gui.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import com.osm2xp.gui.Activator;

public abstract class AbstractOptionsView extends ViewPart {
	
	protected VerifyListener onlyDigitsVerifyListener = new VerifyListener() {
		@Override
		public void verifyText(VerifyEvent e) {
			String string = e.text;
			char[] chars = new char[string.length()];
			string.getChars(0, chars.length, chars, 0);
			for (int i = 0; i < chars.length; i++) {
				if (!('0' <= chars[i] && chars[i] <= '9')) {
					e.doit = false;
					return;
				}
			}
		}
	};

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

	protected Section createSection(String title, boolean expanded) {
		int sectionStyle = expanded ? Section.TWISTIE | Section.EXPANDED | Section.TITLE_BAR : Section.TWISTIE | Section.TITLE_BAR;
		Section section = toolkit.createSection(form.getBody(),
				sectionStyle);
		section.setLayoutData(new TableWrapData(
				TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
		section.setText(title);
		return section;
	}

}
