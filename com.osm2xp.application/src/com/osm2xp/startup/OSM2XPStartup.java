package com.osm2xp.startup;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.progress.UIJob;
import org.osgi.service.prefs.BackingStoreException;

import com.osm2xp.constants.Perspectives;
import com.osm2xp.gui.Activator;
import com.osm2xp.gui.views.MainSceneryFileView;
import com.osm2xp.gui.views.XPlaneAirfieldsView;

public class OSM2XPStartup implements IStartup {

	private static final String SHOWN_AIRFIELDS_PROP = "shownAirfields";

	@Override
	public void earlyStartup() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
        new UIJob("Switching perspectives"){
            @Override
            public IStatus runInUIThread(IProgressMonitor monitor) {
                try {
                    workbench.showPerspective(Perspectives.PERSPECTIVE_XPLANE10, workbench.getActiveWorkbenchWindow());
                    IEclipsePreferences node = ConfigurationScope.INSTANCE.getNode(Activator.PLUGIN_ID);
					boolean shownAirfields = node.getBoolean(SHOWN_AIRFIELDS_PROP, false);
                    IWorkbenchPage activePage = workbench.getActiveWorkbenchWindow().getActivePage();
					if (!shownAirfields && activePage.findView(XPlaneAirfieldsView.ID) == null) {
                    	activePage.showView(XPlaneAirfieldsView.ID);
                    	node.putBoolean(SHOWN_AIRFIELDS_PROP, true);
                    	try {
							node.flush();
						} catch (BackingStoreException e) {
							Activator.log(e);
						}
                    } else {
                    	activePage.showView(MainSceneryFileView.ID);
                    }
                } catch (WorkbenchException e) {
                    return new Status(IStatus.ERROR,Activator.PLUGIN_ID,"Error while switching perspectives", e);
                }
                return Status.OK_STATUS;
            }}
        .run(new NullProgressMonitor());

	}

}
