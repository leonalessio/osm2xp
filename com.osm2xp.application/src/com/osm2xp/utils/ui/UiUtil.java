package com.osm2xp.utils.ui;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.osm2xp.constants.Osm2xpConstants;
import com.osm2xp.controllers.BuildController;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.gui.Activator;
import com.osm2xp.gui.views.BrowserView;

public class UiUtil {
	
	private static final String HTML_FILE_PREFFIX = Platform.getInstallLocation().getURL() + "doc/modes/";
	
	private static final String HTML_FILE = "/index.html";
	
	public static void setEnabledRecursive(Composite composite, boolean enabled) {

	    for (Control control : composite.getChildren()) {
	        if (control instanceof Composite) {
	            setEnabledRecursive((Composite) control, enabled);
	        } else {
	            control.setEnabled(enabled);
	        }
	    }
	    composite.setEnabled(enabled);
	}

	/**
	 * switch to another perspective.
	 * 
	 * @param perspectiveID
	 *            rcp perspective id.
	 */
	public static void switchPerspective(String perspectiveID) {
		if (perspectiveID != null) {
			// switch perspective
			IWorkbench workbench = PlatformUI.getWorkbench();
			if (workbench != null) {
				try {
					IWorkbenchWindow win = workbench.getActiveWorkbenchWindow();
					if (win != null) {
						IWorkbenchPage page = win.getActivePage();
						if (page != null) {
							IPerspectiveDescriptor perspective = page.getPerspective();
							String curId = perspective.getId();
							if (perspectiveID.equals(curId)) {
								return; //No need to switch anything
							}
							IEclipsePreferences node = InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID);
							if (!curId.equals(node.get(Osm2xpConstants.LAST_PERSP_PROP, ""))) {
								node.put(Osm2xpConstants.LAST_PERSP_PROP, curId);
								node.flush();
							}
						}
					}
					
					workbench.showPerspective(perspectiveID, win);
				} catch (Exception e) {
					Osm2xpLogger.warning("Error switching perspective", e);
				}
			}
		}
	}

	public static void showCurrentModeInfo(boolean force) {
		String mode = BuildController.getGenerationMode().toLowerCase();
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench != null) {
			IWorkbenchWindow win = workbench.getActiveWorkbenchWindow();
			if (win != null) {
				IWorkbenchPage page = win.getActivePage();
				if (page != null) {
					IViewPart browserView = null;
					if (force) {
						try {
							browserView = page.showView(BrowserView.ID);
						} catch (PartInitException e) {
							Osm2xpLogger.warning("Error showing browser view", e);
						}
					} else {
						browserView = page.findView(BrowserView.ID);
					}
					if (browserView instanceof BrowserView) {
						((BrowserView) browserView).setUrl(HTML_FILE_PREFFIX + mode + HTML_FILE);
					}
				}
			}
		}		
	}
}
