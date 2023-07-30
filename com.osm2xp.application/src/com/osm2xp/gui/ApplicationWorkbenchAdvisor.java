package com.osm2xp.gui;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.osgi.service.prefs.BackingStoreException;

import com.osm2xp.constants.Perspectives;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.model.facades.FacadeSetManager;
import com.osm2xp.utils.ProcessExecutor;

/**
 * ApplicationWorkbenchAdvisor
 * 
 * @author Benjamin Blanchet
 * 
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	public String getInitialWindowPerspectiveId() {
		return Perspectives.PERSPECTIVE_STARTUP;

	}

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		// super.initialize(configurer);
		// configurer.setSaveAndRestore(true);
		migrateVer3Settings();
	}

	private void migrateVer3Settings() {
		IEclipsePreferences node = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
		String facadeSets = node.get(FacadeSetManager.FACADE_SETS_PROP,"");
		if (!StringUtils.isEmpty(facadeSets)) {
			XPlaneOptionsProvider.getOptions().setFacadeSets(facadeSets);
			node.remove(FacadeSetManager.FACADE_SETS_PROP);
			try {
				node.flush();
			} catch (BackingStoreException e) {
				Osm2xpLogger.log(e);
			}
		}
	}
	
	@Override
	public boolean preShutdown() {
		ProcessExecutor.getExecutor().shutdown();
		return super.preShutdown();
	}
}
