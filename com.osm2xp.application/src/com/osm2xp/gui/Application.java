package com.osm2xp.gui;

import java.io.File;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.osm2xp.application.preferences.EclipsePreferences;
import com.osm2xp.generation.paths.DefaultPathsProvider;
import com.osm2xp.generation.paths.PathsService;
import com.osm2xp.generation.preferences.PreferenceService;

/**
 * This class controls all aspects of the application's execution.
 * 
 * @author Benjamin Blanchet
 */
public class Application implements IApplication {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
	 * IApplicationContext)
	 */
	public Object start(IApplicationContext context) {
		Display display = PlatformUI.createDisplay();
		if (System.getProperty("folder") != null) {
			PathsService.setPathsProvider(new DefaultPathsProvider(new File(System.getProperty("folder"))));
		} else {
			PathsService.setPathsProvider(new RCPPathsProvider());
		}
		PreferenceService.setProgramPreferences(new EclipsePreferences());
		try {
			int returnCode = PlatformUI.createAndRunWorkbench(display,
					new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IApplication.EXIT_RESTART;
			}
			return IApplication.EXIT_OK;
		} finally {
			display.dispose();
		}
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		if (!PlatformUI.isWorkbenchRunning())
			return;
		final IWorkbench workbench = PlatformUI.getWorkbench();
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
}
