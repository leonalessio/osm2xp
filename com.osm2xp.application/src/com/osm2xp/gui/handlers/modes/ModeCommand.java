package com.osm2xp.gui.handlers.modes;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.osm2xp.controllers.BuildController;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.utils.ui.UiUtil;

public class ModeCommand extends AbstractHandler {
	
	private String perspectiveId;

	private String docFolder;

	private String modeId;

	public ModeCommand(String perspectiveId, String docFolder, String modeId) {
		super();
		this.perspectiveId = perspectiveId;
		this.modeId = modeId;
		this.docFolder = docFolder;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
//		GlobalOptionsProvider.getOptions().setOutputFormat( //TODO
//				Perspectives.PERSPECTIVE_XPLANE10);
		UiUtil.switchPerspective(perspectiveId);
		BuildController.setGenerationMode(modeId);		
		UiUtil.showCurrentModeInfo(false);
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null) {
			try {
				activeWorkbenchWindow.getShell().setText("OSM2XP - " + event.getCommand().getName());
			} catch (NotDefinedException e) {
				Osm2xpLogger.error(e);
			}
		}
		return null;
	}
	

}
