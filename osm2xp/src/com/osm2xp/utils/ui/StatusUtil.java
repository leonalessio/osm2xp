package com.osm2xp.utils.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.osm2xp.gui.Activator;
import com.osm2xp.utils.StatusInfo;

public class StatusUtil {
	public static IStatus fromStatusInfo(StatusInfo statusInfo) {
		return new Status(statusInfo.getSeverity().ordinal(), Activator.PLUGIN_ID,statusInfo.getMessage(), statusInfo.getException());
	}
	
	public static IStatus error(String message) {
		return new Status(IStatus.ERROR, Activator.PLUGIN_ID,message);
	}
	
	public static IStatus warning(String message) {
		return new Status(IStatus.WARNING, Activator.PLUGIN_ID,message);
	}
}
