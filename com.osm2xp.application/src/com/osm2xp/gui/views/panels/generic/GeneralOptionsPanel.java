package com.osm2xp.gui.views.panels.generic;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;

import com.osm2xp.generation.options.GlobalOptionsProvider;
import com.osm2xp.gui.views.panels.Osm2xpPanel;
import com.osm2xp.utils.ui.UiUtil;

/**
 * GeneralXPlaneOptionsPanel.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class GeneralOptionsPanel extends Osm2xpPanel {
	private Button btnDataBaseMode;
	private Button btnApdTime;
	private Button btnApdCoords;
	private Button btnSimplifyShapes;
	private Button btnAnalyzeAreas;
//	private Button btnSinglePass;

	public GeneralOptionsPanel(final Composite parent, final int style) {
		super(parent, style);
	}

	@Override
	protected void initLayout() {
		final GridLayout gridLayout = new GridLayout(3, true);
		gridLayout.verticalSpacing = 15;
		setLayout(gridLayout);
	}

	@Override
	protected void initComponents() {
		btnDataBaseMode = new Button(this, SWT.CHECK);
		btnDataBaseMode.setText("Database mode");
		GridDataFactory.swtDefaults().applyTo(btnDataBaseMode);
		btnSimplifyShapes = new Button(this, SWT.CHECK);
		btnSimplifyShapes.setText("Simplify shapes");
		GridDataFactory.swtDefaults().applyTo(btnSimplifyShapes);
		btnAnalyzeAreas = new Button(this, SWT.CHECK);
		btnAnalyzeAreas.setText("Analyze Landuse areas");
		btnAnalyzeAreas.setToolTipText("Analyze, which Landuse area current point or line belongs to. This requires additional time (for extra pass) and more RAM.");
		GridDataFactory.swtDefaults().applyTo(btnAnalyzeAreas);
		btnApdTime = new Button(this, SWT.CHECK);
		btnApdTime.setText("Add time to scene name");
		GridDataFactory.swtDefaults().applyTo(btnApdTime);
		btnApdCoords = new Button(this, SWT.CHECK);
		btnApdCoords.setText("Add coordinates to scene name");
		GridDataFactory.swtDefaults().applyTo(btnApdCoords);
//		btnSinglePass = new Button(this, SWT.CHECK);
//		btnSinglePass.setText("Single pass mode");
		
		Link link = new Link(this, SWT.NONE);
		link.setText("<a>Read more about current output format</a>");
		link.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				UiUtil.showCurrentModeInfo(true);
			}
			
		});
		GridDataFactory.swtDefaults().span(3,1).applyTo(link);

	}

	@Override
	protected void bindComponents() {
		bindComponent(btnDataBaseMode, GlobalOptionsProvider.getOptions(),
				"databaseMode");
		bindComponent(btnApdTime, GlobalOptionsProvider.getOptions(), "appendHour");
		bindComponent(btnApdCoords, GlobalOptionsProvider.getOptions(), "appendTile");
		bindComponent(btnSimplifyShapes, GlobalOptionsProvider.getOptions(),"simplifyShapes");
		bindComponent(btnAnalyzeAreas, GlobalOptionsProvider.getOptions(),"analyzeAreas");
//		bindComponent(btnSinglePass, GlobalOptionsProvider.getOptions(),
//				"singlePass");

	}

	@Override
	protected void addComponentsListeners() {

	}

}
