package com.osm2xp.gui.views.panels.xplane;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.osm2xp.generation.options.XPlaneOptionsProvider;

/**
 * StreetLightsDensityPanel.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class StreetLightsDensityPanel extends Composite {

	public StreetLightsDensityPanel(final Composite parent, final int style) {
		super(parent, style);
		final GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginHeight = 15;
		gridLayout.horizontalSpacing = 15;
		gridLayout.verticalSpacing = 15;
		setLayout(gridLayout);

		final Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblNewLabel.setText("Street lights density : ");

		final Combo combo = new Combo(this, SWT.NONE);

		combo.setItems(new String[] { "Minimum", "Normal", "Maximum" });
		combo.setText(combo.getItem(XPlaneOptionsProvider.getOptions()
				.getLightsDensity()));
		final GridData gridData = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
//		gridData.widthHint = 187;
		combo.setLayoutData(gridData);
		combo.addModifyListener(new ModifyListener() {
			public void modifyText(final ModifyEvent event) {
				XPlaneOptionsProvider.getOptions().setLightsDensity(
						combo.getSelectionIndex());
			}
		});

	}
}
