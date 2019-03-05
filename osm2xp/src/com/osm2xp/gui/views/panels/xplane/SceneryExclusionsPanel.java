package com.osm2xp.gui.views.panels.xplane;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import com.osm2xp.generation.options.XPlaneOptionsProvider;

/**
 * SceneryExclusionsPanel.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class SceneryExclusionsPanel extends Composite {

	public SceneryExclusionsPanel(final Composite parent, final int style) {
		super(parent, style);
		DataBindingContext bindingContext = new DataBindingContext();
		GridLayout gridLayout = new GridLayout(2, true);
		gridLayout.horizontalSpacing = 15;
		gridLayout.verticalSpacing = 15;
		setLayout(gridLayout);
		Button btnCheckExcludeLin = new Button(this, SWT.CHECK);
		btnCheckExcludeLin.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1));
		btnCheckExcludeLin.setText("Exclude Lines");
		bindingContext.bindValue(SWTObservables
				.observeSelection(btnCheckExcludeLin), PojoObservables
				.observeValue(XPlaneOptionsProvider.getOptions(), "excludeLin"));

		Button btnCheckExcludeFor = new Button(this, SWT.CHECK);
		btnCheckExcludeFor.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1));
		btnCheckExcludeFor.setText("Exclude Forests");
		bindingContext.bindValue(SWTObservables
				.observeSelection(btnCheckExcludeFor), PojoObservables
				.observeValue(XPlaneOptionsProvider.getOptions(), "excludeFor"));

		Button btnCheckExcludeObj = new Button(this, SWT.CHECK);
		btnCheckExcludeObj.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1));
		btnCheckExcludeObj.setText("Exclude Objects");
		bindingContext.bindValue(SWTObservables
				.observeSelection(btnCheckExcludeObj), PojoObservables
				.observeValue(XPlaneOptionsProvider.getOptions(), "excludeObj"));

		Button btnCheckExcludeStr = new Button(this, SWT.CHECK);
		btnCheckExcludeStr.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1));
		btnCheckExcludeStr.setText("Exclude Strings");
		bindingContext.bindValue(SWTObservables
				.observeSelection(btnCheckExcludeStr), PojoObservables
				.observeValue(XPlaneOptionsProvider.getOptions(), "excludeStr"));

		Button btnCheckExcludeFac = new Button(this, SWT.CHECK);
		btnCheckExcludeFac.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1));
		btnCheckExcludeFac.setText("Exclude Facades");
		bindingContext.bindValue(SWTObservables
				.observeSelection(btnCheckExcludeFac), PojoObservables
				.observeValue(XPlaneOptionsProvider.getOptions(), "excludeFac"));

		Button btnCheckExcludeNet = new Button(this, SWT.CHECK);
		btnCheckExcludeNet.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1));
		btnCheckExcludeNet.setText("Exclude Network");
		bindingContext.bindValue(SWTObservables
				.observeSelection(btnCheckExcludeNet), PojoObservables
				.observeValue(XPlaneOptionsProvider.getOptions(), "excludeNet"));

		Button btnCheckExcludePol = new Button(this, SWT.CHECK);
		btnCheckExcludePol.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1));
		btnCheckExcludePol.setText("Exclude Pol");
		bindingContext.bindValue(SWTObservables
				.observeSelection(btnCheckExcludePol), PojoObservables
				.observeValue(XPlaneOptionsProvider.getOptions(), "excludePol"));
		Button btnCheckExcludeBch = new Button(this, SWT.CHECK);
		btnCheckExcludeBch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1));
		btnCheckExcludeBch.setText("Exclude Bch");
		bindingContext.bindValue(SWTObservables
				.observeSelection(btnCheckExcludeBch), PojoObservables
				.observeValue(XPlaneOptionsProvider.getOptions(), "excludeBch"));

		new Label(this, SWT.NONE);
		new Label(this, SWT.NONE);
		Button btnCheckSmartExclusions = new Button(this, SWT.CHECK);
		btnCheckSmartExclusions.setLayoutData(new GridData(SWT.FILL,
				SWT.CENTER, false, false, 1, 1));
		btnCheckSmartExclusions.setText("Smart Obj exclusions");
		bindingContext.bindValue(SWTObservables
				.observeSelection(btnCheckSmartExclusions), PojoObservables
				.observeValue(XPlaneOptionsProvider.getOptions(),
						"smartExclusions"));
		new Label(this, SWT.NONE);

		Label labelSmartExclusionDistance = new Label(this, SWT.NONE);
		labelSmartExclusionDistance.setText("Distance");

		Spinner spinnerSmartExclusionDistance = new Spinner(this, SWT.BORDER);
		bindingContext.bindValue(SWTObservables
				.observeSelection(spinnerSmartExclusionDistance),
				PojoObservables.observeValue(XPlaneOptionsProvider.getOptions(),
						"smartExclusionDistance"));
		Label labelSmartExclusionSize = new Label(this, SWT.NONE);
		labelSmartExclusionSize.setText("Min Size");

		Spinner spinnerSmartExclusionSize = new Spinner(this, SWT.BORDER);
		bindingContext.bindValue(SWTObservables
				.observeSelection(spinnerSmartExclusionSize), PojoObservables
				.observeValue(XPlaneOptionsProvider.getOptions(),
						"smartExclusionSize"));

	}
}
