package com.osm2xp.gui.views.panels.xplane;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

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
	private Spinner spinHeightTolerance;
	private Spinner spinMaxSimlifyPerimeter;
	private Text allowedDifference;

	public ObjectByPolyPanel(final Composite parent, final int style) {
		super(parent, style);

	}

	@Override
	protected void initComponents() {
		setLayout(GridLayoutFactory.swtDefaults().numColumns(4).create());
		Label toleranceLbl = new Label(this, SWT.NONE);
		toleranceLbl.setText("Model size tolerance factor, %:");
		toleranceLbl.setToolTipText("This gives how much selected model size can differ from actual map polygon size. 0% means exact match, 100% - model side can be 2 times longer");
		spinTolerance = new Spinner(this, SWT.BORDER);
		
		Label maxSimplifyLbl = new Label(this, SWT.NONE);
		maxSimplifyLbl.setText("Max building perimeter to treat as rectangle");
		maxSimplifyLbl.setToolTipText("If building perimeter is not greater than this value, it will be treated as a rectangle and osm2xp will try to find suitable model for it");
		spinMaxSimlifyPerimeter = new Spinner(this, SWT.BORDER);
		
		Label heightToleranceLbl = new Label(this, SWT.NONE);
		heightToleranceLbl.setText("Model height tolerance factor, %:");
		heightToleranceLbl.setToolTipText("This gives how much selected model height can differ from actual map polygon height (where present). 0% means exact match, 100% - model can be 2 times lower/higher");
		spinHeightTolerance = new Spinner(this, SWT.BORDER);
		
		Label maxHeightDiffLbl = new Label(this, SWT.NONE);
		maxHeightDiffLbl.setText("Model height allowed difference, m:");
		maxHeightDiffLbl.setToolTipText("If model height and poly height difference is less than or equal to this value - model will be considered good even if height tolerance % doesn't match");
		allowedDifference = new Text(this, SWT.BORDER);
		allowedDifference.addVerifyListener(new VerifyListener() {
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
		});
	}

	@Override
	protected void bindComponents() {		
		bindComponent(spinMaxSimlifyPerimeter, XPlaneOptionsProvider.getOptions(),
				"maxPerimeterToSimplify");
		bindSpinnerToDouble(spinTolerance, XPlaneOptionsProvider.getOptions(),
				"objSizeTolerance",2);
		bindSpinnerToDouble(spinHeightTolerance, XPlaneOptionsProvider.getOptions(),
				"objHeightTolerance",2);
		bindTextToInt(allowedDifference, XPlaneOptionsProvider.getOptions(),"objHeightAllowedDifference");
	}

	@Override
	protected void addComponentsListeners() {

	}
}
