package com.osm2xp.gui.views.panels.xplane;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.gui.views.panels.BindingPanel;
import com.osm2xp.utils.ui.UiUtil;

/**
 * SceneryExclusionsPanel.
 * 
 * @author Benjamin Blanchet, 32kda
 * 
 */
public class SceneryExclusionsPanel extends BindingPanel {

	public SceneryExclusionsPanel(final Composite parent, final int style) {
		super(parent, style);
		DataBindingContext bindingContext = new DataBindingContext();
		GridLayout gridLayout = new GridLayout(1, true);
		setLayout(gridLayout);
		
		Button autoExcludeCheckbox = createCheckBox(this, "Automatic Excludes", "autoExclude", bindingContext);
		autoExcludeCheckbox.setToolTipText("If this is selected - underlying scenery object will be excluded if you choosed to generate corresponding object type.");
		Group mainGrp = new Group(this, SWT.NONE);
		mainGrp.setText("Basic excludes");
		GridLayoutFactory.swtDefaults().spacing(15,15).numColumns(2).applyTo(mainGrp);
		createCheckBox(mainGrp, "Exclude Forests", "excludeFor", bindingContext);
		createCheckBox(mainGrp, "Exclude Facades", "excludeFac", bindingContext).setToolTipText("Exclude Facades - buildings, barriers");
		createCheckBox(mainGrp, "Exclude Objects", "excludeObj", bindingContext).setToolTipText("Exclude 3D objects - can be some buildings and models like chimneys, masts, tanks etc.");
		createCheckBox(mainGrp, "Exclude Network", "excludeNet", bindingContext).setToolTipText("Exclude Vector Network - roads, railways, power lines");
		createCheckBox(mainGrp, "Exclude Polygons", "excludePol", bindingContext).setToolTipText("Exclude Draped polygons, like parking pavements");
		
		autoExcludeCheckbox.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				UiUtil.setEnabledRecursive(mainGrp, !autoExcludeCheckbox.getSelection());
			}
			
		});
		UiUtil.setEnabledRecursive(mainGrp, !XPlaneOptionsProvider.getOptions().isAutoExclude());
		
		Group extendedGrp = new Group(this, SWT.NONE);
		extendedGrp.setText("Extended excludes");
		GridLayoutFactory.swtDefaults().spacing(15,15).numColumns(2).applyTo(extendedGrp);
		createCheckBox(extendedGrp, "Exclude Strings", "excludeStr", bindingContext).setToolTipText("Exclude object strings");
		createCheckBox(extendedGrp, "Exclude Lines", "excludeLin", bindingContext).setToolTipText("Exclude painted lines");
		createCheckBox(extendedGrp, "Exclude Beaches", "excludeBch", bindingContext);
	}

	protected Button createCheckBox(Composite parent, String title, String property, DataBindingContext bindingContext) {
		Button checkbox = new Button(parent, SWT.CHECK);
		checkbox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1));
		checkbox.setText(title);
		bindComponent(checkbox, XPlaneOptionsProvider.getOptions(),
				property);
		return checkbox;
	}
	
}
