package com.osm2xp.gui.handlers;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.InstanceScope;

import com.osm2xp.constants.Perspectives;
import com.osm2xp.controllers.BuildController;
import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.generation.options.FsxOptionsProvider;
import com.osm2xp.generation.options.GlobalOptionsProvider;
import com.osm2xp.gui.Activator;
import com.osm2xp.gui.dialogs.utils.Osm2xpDialogsHelper;
import com.osm2xp.model.facades.FacadeSetHelper;
import com.osm2xp.model.facades.FacadeSetManager;
import com.osm2xp.utils.StatusInfo;
import com.osm2xp.utils.helpers.FsxOptionsHelper;
import com.osm2xp.utils.helpers.GuiOptionsHelper;

/**
 * CommandBuildScene.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class CommandBuildScene extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IStatus status = getConfigurationErrors();
		if (status.isOK()) {
			doLaunchBuild();
		} else if (status.getSeverity() == IStatus.WARNING ) {
			if (Osm2xpDialogsHelper.displayQuestionDialog("Configuration problems detected", status.getMessage() + "\nContinue?")) {
				doLaunchBuild();	
			}
		} else {
			Osm2xpDialogsHelper
					.displayErrorDialog("Bad configuration", "Please check following errors:\n"
							+ status.getMessage());
		}
		return null;
	}

	protected void doLaunchBuild() {
		BuildController bc = new BuildController();
		try {
			bc.launchBuild();
		} catch (Osm2xpBusinessException e) {
			Osm2xpLogger.error("Error building scene.", e);
		}
	} 

	private IStatus getConfigurationErrors() {
		
		StringBuilder errors = new StringBuilder();
		StringBuilder warnings = new StringBuilder();
		// Common validation
		if (GlobalOptionsProvider.getOptions().getCurrentFilePath() == null) {
			errors.append(" - No osm file selected.\n");
		}
		// Xplane validation
		String mode = BuildController.getMode();
		if (mode.equalsIgnoreCase("XPLANE10") || mode.equalsIgnoreCase("XPLANE9")) {
			String facadeSetsStr= InstanceScope.INSTANCE.getNode(Activator.PLUGIN_ID).get(FacadeSetManager.FACADE_SETS_PROP,"");
			if (facadeSetsStr.isEmpty()) {
				warnings.append(" - ");
				warnings.append("No facade sets was configured. Proceed using built-in facades?");
				warnings.append('\n');
				facadeSetsStr = FacadeSetHelper.getDefaultFacadePath();
			}
			FacadeSetManager manager = FacadeSetManager.getManager(facadeSetsStr, null);
			IStatus facadeSetStatus = manager.getFacadeSetStatus();
			if (facadeSetStatus.getSeverity() == IStatus.ERROR) {
				errors.append(" - ");
				errors.append(facadeSetStatus.getMessage());
				errors.append('\n');
			} else if  (facadeSetStatus.getSeverity() == IStatus.WARNING) {
				warnings.append(" - ");
				warnings.append(facadeSetStatus.getMessage());
				warnings.append('\n');
			}
		}
		// FSX validation
		else if (mode.equalsIgnoreCase("FSX") && StringUtils.isBlank(FsxOptionsProvider.getOptions()
					.getBglCompPath())) {
			errors.append(" - bglComp.exe location not set!\n");
		}
		if (errors.length() > 0) {
			return new StatusInfo(IStatus.ERROR, errors.toString());
		}
		if (warnings.length() > 0) {
			return new StatusInfo(IStatus.WARNING, warnings.toString());
		}
		return Status.OK_STATUS;
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
