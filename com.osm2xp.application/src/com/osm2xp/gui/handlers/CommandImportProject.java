package com.osm2xp.gui.handlers;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import com.osm2xp.controllers.BuildController;
import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.utils.helpers.Osm2xpProjectHelper;

/**
 * CommandImportProject.
 * 
 * @author Benjamin Blanchet
 * 
 * Deprecated. Project contains no useful metadata currently. 
 * Some data like heights and geonames are saved in global prefs for now
 * 
 */
@Deprecated
public class CommandImportProject extends AbstractHandler {
	private static final String[] FILTER_NAMES = { "osm2xp project (*.project)" };
	private static final String[] FILTER_EXTS = { "*.project" };

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		FileDialog dlg = new FileDialog(Display.getCurrent().getActiveShell(),
				SWT.OPEN);
		dlg.setFilterNames(FILTER_NAMES);
		dlg.setFilterExtensions(FILTER_EXTS);
		String fileName = dlg.open();
		if (fileName != null) {
			try {
				Osm2xpProjectHelper.loadProject(fileName);

				if (MessageDialog.openConfirm(Display.getCurrent()
						.getActiveShell(), "Import project", "Import project "
						+ new File(fileName).getName()
						+ " ?\n"
						+ "(osm file : "
						+ Osm2xpProjectHelper.getOsm2XpProject().getFile()
						+ " , "
						+ Osm2xpProjectHelper.getOsm2XpProject()
								.getCoordinatesList().getCoordinates().size()
						+ " tile(s) to process )")) {
					BuildController bc = new BuildController();
//					bc.restartImportedProject(); //TODO not supported for now
				}

			} catch (Osm2xpBusinessException e) {
				Osm2xpLogger.error("Error loading project file", e);
			}
		}

		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

}
