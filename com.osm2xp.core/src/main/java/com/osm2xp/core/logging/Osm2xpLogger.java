package com.osm2xp.core.logging;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * didn't have time to look at log4j & rcp , so did this crappy logger.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class Osm2xpLogger {

	private static Logger logger;
	private static Logger errorFileLogger;

	static {

		logger = Logger.getLogger(Osm2xpLogger.class.getName());
		logger.setUseParentHandlers(false);

		ConsoleHandler ch = new ConsoleHandler();
		ch.setFormatter(new Osm2xpConsoleLoggerFormater());
		logger.addHandler(ch);

		errorFileLogger = Logger.getLogger(Osm2xpLogger.class.getName()
				+ "-error");
		errorFileLogger.setUseParentHandlers(false);
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

	public static void error(Exception e) {
		log(e);
	}
	
	public static void log(Exception e) {
		logger.log(Level.SEVERE, e.getMessage(), e);
	}

}
