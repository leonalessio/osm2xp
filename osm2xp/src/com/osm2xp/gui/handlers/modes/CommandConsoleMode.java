package com.osm2xp.gui.handlers.modes;

import java.io.File;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.ResourcesPlugin;

import com.osm2xp.constants.Perspectives;
import com.osm2xp.gui.perspectives.ConsoleConfigurationPerspective;
import com.osm2xp.gui.views.panels.generic.OutPutFormatPanel;
import com.osm2xp.utils.helpers.GuiOptionsHelper;
import com.osm2xp.utils.ui.UiUtil;

/**
 * CommandConsoleMode.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class CommandConsoleMode extends ModeCommand {
	
	public CommandConsoleMode() {
		super(ConsoleConfigurationPerspective.ID, "console");
	}
	
}
