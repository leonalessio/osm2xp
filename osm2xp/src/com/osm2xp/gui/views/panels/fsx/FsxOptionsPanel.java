package com.osm2xp.gui.views.panels.fsx;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.osm2xp.generation.options.FsxOptionsProvider;
import com.osm2xp.gui.views.panels.Osm2xpPanel;

/**
 * FsxOptionsPanel.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class FsxOptionsPanel extends Osm2xpPanel {
	private Text bglCompLocationText;
	private static final String[] FILTER_NAMES = { "BglComp.exe)" };
	private static final String[] FILTER_EXTS = { "BglComp.exe" };

	public FsxOptionsPanel(final Composite parent, final int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginTop = 10;
		gridLayout.marginBottom = 10;
		setLayout(gridLayout);

		Label lblBglComp = new Label(this, SWT.NONE);
		lblBglComp.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblBglComp.setText("BglComp location :");

		bglCompLocationText = new Text(this, SWT.BORDER);
		// if bglComp location is set and the file exists, set text
		if ((!StringUtils.isBlank(FsxOptionsProvider.getOptions()
				.getBglCompPath()))
				&& (new File(FsxOptionsProvider.getOptions().getBglCompPath())
						.exists())) {
			bglCompLocationText.setText(FsxOptionsProvider.getOptions()
					.getBglCompPath());
		} else {
			bglCompLocationText.setText("Clic to locate BglComp.exe");

		}
		bglCompLocationText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				FileDialog dlg = new FileDialog(getParent().getShell(),
						SWT.OPEN);
				dlg.setFilterNames(FILTER_NAMES);
				dlg.setFilterExtensions(FILTER_EXTS);
				String fileName = dlg.open();
				if (fileName != null) {
					bglCompLocationText.setText(fileName);
					FsxOptionsProvider.getOptions().setBglCompPath(fileName);
				}
			}
		});
		bglCompLocationText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

	}

	@Override
	protected void initLayout() {

	}

	@Override
	protected void initComponents() {

	}

	@Override
	protected void bindComponents() {

	}

	@Override
	protected void addComponentsListeners() {

	}

}
