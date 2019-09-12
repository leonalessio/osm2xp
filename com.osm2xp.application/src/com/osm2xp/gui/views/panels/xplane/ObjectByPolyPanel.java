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
public class ObjectByPolyPanel extends Osm2xpPanel {

	private Spinner spinTolerance;
	private Spinner spinMaxSimlifyPerimeter;

	public ObjectByPolyPanel(final Composite parent, final int style) {
		super(parent, style);

	}

	@Override
	protected void initComponents() {
		setLayout(GridLayoutFactory.swtDefaults().numColumns(2).create());
		Label toleranceLbl = new Label(this, SWT.NONE);
		toleranceLbl.setText("Model size tolerance factor, %:");
		toleranceLbl.setToolTipText("This gives how much selected model size can differ from actual map polygon size. 0% means exact match, 100% - model side can be 2 times longer");
		spinTolerance = new Spinner(this, SWT.BORDER);
		
		Label maxSimplifyLbl = new Label(this, SWT.NONE);
		maxSimplifyLbl.setText("Max building perimeter to treat as rectangle");
		maxSimplifyLbl.setToolTipText("If building perimeter is not greater than this value, it will be treated as a rectangle and osm2xp will try to find suitable model for it");
		spinMaxSimlifyPerimeter = new Spinner(this, SWT.BORDER);		
	}

	@Override
	protected void bindComponents() {		
		bindComponent(spinMaxSimlifyPerimeter, XPlaneOptionsProvider.getOptions(),
				"maxPerimeterToSimplify");
		bindSpinnerToDouble(spinTolerance, XPlaneOptionsProvider.getOptions(),
				"objSizeTolerance",2);

	}

	@Override
	protected void addComponentsListeners() {

	}
}
