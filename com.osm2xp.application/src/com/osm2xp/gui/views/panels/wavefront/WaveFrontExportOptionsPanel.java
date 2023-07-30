package com.osm2xp.gui.views.panels.wavefront;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import com.osm2xp.gui.views.panels.Osm2xpPanel;
import com.osm2xp.utils.helpers.WavefrontOptionsHelper;

/**
 * WaveFrontExportOptionsPanel.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class WaveFrontExportOptionsPanel extends Osm2xpPanel {
	private Spinner spinnerPoints;
	private Spinner spinnerMeters;
	private Button btnGenerateSingleObject;
//	private Button btnGeneratedsfPlaceHolder;

	public WaveFrontExportOptionsPanel(final Composite parent, final int style) {
		super(parent, style);

	}

	@Override
	protected void initLayout() {
		setLayout(new GridLayout(3, false));
	}

	@Override
	protected void initComponents() {
		Label labelGenerate = new Label(this, SWT.NONE);
		labelGenerate.setText("Generate buildings with more than ");
		spinnerPoints = new Spinner(this, SWT.BORDER);
		spinnerPoints.setMaximum(1000);
		Label labelPointsFilter = new Label(this, SWT.NONE);
		labelPointsFilter.setText("points ");
		Label labelHeightFilter = new Label(this, SWT.NONE);
		labelHeightFilter.setText("Generate buildings with height more than ");
		spinnerMeters = new Spinner(this, SWT.BORDER);
		spinnerMeters.setMaximum(1000);
		Label labelMeters = new Label(this, SWT.NONE);
		labelMeters.setText("meters ");
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		btnGenerateSingleObject = new Button(this, SWT.CHECK);
		btnGenerateSingleObject
				.setText("Generate a single object for the whole scenery");
		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		Button btnGeneratedsfPlaceHolder = new Button(this, SWT.CHECK);
		btnGeneratedsfPlaceHolder
				.setText("Generate a dsf for generated objects");
	}

	@Override
	protected void bindComponents() {
		bindComponent(spinnerPoints, WavefrontOptionsHelper.getOptions(),
				"waveFrontExportPointsFilter");
		bindComponent(spinnerMeters, WavefrontOptionsHelper.getOptions(),
				"waveFrontExportHeightFilter");
		bindComponent(btnGenerateSingleObject,
				WavefrontOptionsHelper.getOptions(),
				"waveFrontExportSingleObject");
//		bindComponent(btnGeneratedsfPlaceHolder,
//				WavefrontOptionsHelper.getOptions(),
//				"generateWaveFrontDsfPlaceholder");

	}

	@Override
	protected void addComponentsListeners() {

	}

}
