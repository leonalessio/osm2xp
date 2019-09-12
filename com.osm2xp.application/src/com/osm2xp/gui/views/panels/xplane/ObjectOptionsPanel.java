package com.osm2xp.gui.views.panels.xplane;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.gui.views.panels.Osm2xpPanel;

/**
 * 3D object by polygon selection options
 * 
 * @author 32kda
 * 
 */
public class ObjectOptionsPanel extends Osm2xpPanel {

	private Spinner spinRenderLevel;

	public ObjectOptionsPanel(final Composite parent, final int style) {
		super(parent, style);

	}

	@Override
	protected void initComponents() {
		setLayout(GridLayoutFactory.swtDefaults().numColumns(2).create());
		Label toleranceLbl = new Label(this, SWT.NONE);
		toleranceLbl.setText("Object render level, 1-6:");
		toleranceLbl.setToolTipText("Minimal object detail setting, for which generated 3D objects should be displayed");
		spinRenderLevel = new Spinner(this, SWT.BORDER);
		spinRenderLevel.setMinimum(1);
		spinRenderLevel.setMaximum(6);
	}

	@Override
	protected void bindComponents() {		
		bindComponent(spinRenderLevel, XPlaneOptionsProvider.getOptions(),
				"objectRenderLevel");
	}

}
