package com.osm2xp.gui.handlers;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.osm2xp.controllers.BuildController;
import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.generation.options.FsxOptionsProvider;
import com.osm2xp.generation.options.GlobalOptionsProvider;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.gui.dialogs.utils.Osm2xpDialogsHelper;
import com.osm2xp.model.facades.FacadeSetManager;
import com.osm2xp.utils.StatusInfo;
import com.osm2xp.utils.StatusInfo.Severity;
import com.osm2xp.utils.ui.StatusUtil;

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
		String mode = BuildController.getGenerationMode();
		if (mode.equalsIgnoreCase("XPLANE10") || mode.equalsIgnoreCase("XPLANE9")) {
			String facadeSetsStr= XPlaneOptionsProvider.getOptions().getFacadeSets();
			if (StringUtils.isEmpty(facadeSetsStr)) {
				warnings.append(" - ");
				warnings.append("No facade sets was configured. Proceed using built-in facades?");
				warnings.append('\n');
				facadeSetsStr = XPlaneOptionsProvider.getDefaultFacadeSets();
			}
			FacadeSetManager manager = FacadeSetManager.getManager(facadeSetsStr, null);
			StatusInfo facadeSetStatus = manager.getFacadeSetStatus();
			if (facadeSetStatus.getSeverity() == Severity.ERROR) {
				errors.append(" - ");
				errors.append(facadeSetStatus.getMessage());
				errors.append('\n');
			} else if  (facadeSetStatus.getSeverity() == Severity.WARNING) {
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
			return StatusUtil.error(errors.toString());
		}
		if (warnings.length() > 0) {
			return StatusUtil.warning(warnings.toString());
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
