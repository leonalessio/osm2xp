package com.osm2xp.utils;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class UiUtil {
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
}
