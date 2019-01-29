package com.osm2xp.gui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.osm2xp.constants.Osm2xpConstants;
import com.osm2xp.gui.views.LastFilesView;
import com.osm2xp.utils.helpers.GuiOptionsHelper;

/**
 * Command for selecting input file
 * 
 * @author Dmitry Karpenko
 * 
 */
public class CommandSelectFile extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window == null) {
			return null;
		}
		FileDialog dlg = new FileDialog(window.getShell(), SWT.OPEN);
		dlg.setFilterNames(Osm2xpConstants.OSM_FILE_FILTER_NAMES);
		dlg.setFilterExtensions(Osm2xpConstants.OSM_FILE_FILTER_EXTS);
		String fileName = dlg.open();
		if (fileName != null) {
			IViewPart view = window.getActivePage().findView(LastFilesView.ID);
			if (view instanceof LastFilesView) {
				((LastFilesView) view).selectFile(fileName);
			} else {
				GuiOptionsHelper.getOptions().setCurrentFilePath(fileName);
				GuiOptionsHelper.setSceneName(new Path(fileName).lastSegment());
				GuiOptionsHelper.addUsedFile(fileName);
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
