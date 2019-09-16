package com.osm2xp.gui.views.panels.xplane;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.gui.views.panels.Osm2xpPanel;

/**
 * General X-Plane options panel
 * 
 * @author 32kda
 * 
 */
public class GeneralXPlaneOptionsPanel extends Osm2xpPanel {

	private Button btnGenerateLib;
	private Button btnBundleAssets;

	public GeneralXPlaneOptionsPanel(final Composite parent, final int style) {
		super(parent, style);
	}

	@Override
	protected void initComponents() {
		btnBundleAssets = new Button(this, SWT.RADIO);
		btnBundleAssets.setToolTipText("Choose this to bundle all necessary assets (objects, facades...) into generated scenery");
		btnBundleAssets.setText("Bundle assests into scenery");
		GridDataFactory.fillDefaults().applyTo(btnBundleAssets);
		btnGenerateLib = new Button(this, SWT.RADIO);
		btnGenerateLib.setText("Generate assets library");
		btnGenerateLib.setToolTipText("Choose this to put all of the asses (objects, facades...) into separate ibrary");
		GridDataFactory.fillDefaults().applyTo(btnGenerateLib);
		
		btnBundleAssets.setSelection(!XPlaneOptionsProvider.getOptions().isBuildLibrary());
		btnGenerateLib.setSelection(XPlaneOptionsProvider.getOptions().isBuildLibrary());
		
		btnBundleAssets.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				generationSelectionChanged();
			}
		});
		
		btnGenerateLib.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				generationSelectionChanged();
			}
		});
	}

	protected void generationSelectionChanged() {
		XPlaneOptionsProvider.getOptions().setBuildLibrary(btnGenerateLib.getSelection());
	}

	@Override
	protected void bindComponents() {
		//Do nothibg
	}

	@Override
	protected void initLayout() {
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.verticalSpacing = 15;
		gridLayout.horizontalSpacing = 15;
		gridLayout.marginHeight = 15;
		setLayout(gridLayout);
	}

}
