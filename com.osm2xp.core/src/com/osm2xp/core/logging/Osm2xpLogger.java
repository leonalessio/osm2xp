package com.osm2xp.core.logging;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;

/**
 * didn't have time to look at log4j & rcp , so did this crappy logger.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class Osm2xpLogger {

	private static final String ERROR_LOG_NAME = "error.log";
	private static Logger logger;
	private static Logger logger2;
	private static Logger errorFileLogger;

	static {

		logger = Logger.getLogger(Osm2xpLogger.class.getName());
		logger.setUseParentHandlers(false);
		logger2 = Logger.getLogger("org.eclipse.equinox.logger");
		logger2.setUseParentHandlers(false);

		ConsoleHandler ch = new ConsoleHandler();
		ch.setFormatter(new Osm2xpConsoleLoggerFormater());
		logger.addHandler(ch);
		logger2.addHandler(ch);		
		FileHandler fileHandler = null;
		IPath location = ResourcesPlugin.getWorkspace()
		.getRoot().getLocation();
		try {
			fileHandler = new FileHandler(new File(location.toFile(), ERROR_LOG_NAME).getAbsolutePath());
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		errorFileLogger = Logger.getLogger(Osm2xpLogger.class.getName()
				+ "-error");
		errorFileLogger.setUseParentHandlers(false);
		fileHandler.setFormatter(new Osm2xpFileLoggerFormater());
		errorFileLogger.addHandler(fileHandler);
	}

	public static void error(String message, Throwable exception) {
		logger.severe(message + "\n" + exception.getMessage());
		errorFileLogger.severe(message + "\n" + exception.getMessage());
	}

	public static void warning(String message, Throwable exception) {
		logger.warning(message + "\n" + exception.getMessage());
	}

	public static void info(String message) {
		logger.info(message);
	}

	public static void warning(String message) {
		logger.warning(message);
	}
	
	public static void error(String message) {
		logger.severe(message);

	}

	public static void log(Exception e) {
		logger.log(Level.SEVERE, e.getMessage(), e);
	}

}
