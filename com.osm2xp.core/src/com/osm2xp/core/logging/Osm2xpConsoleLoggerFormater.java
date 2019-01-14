package com.osm2xp.core.logging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Osm2xpConsoleLoggerFormater.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class Osm2xpConsoleLoggerFormater extends Formatter {

	public String format(LogRecord record) {

		StringBuffer message = new StringBuffer();
		message.append(calcDate(record.getMillis()));
		if (record.getLevel().equals(Level.WARNING)
				|| record.getLevel().equals(Level.SEVERE)) {
			message.append(" - ");
			message.append(record.getLevel());
		}
		message.append(" : ");
		message.append(record.getMessage());
		message.append(" \n");

		return message.toString();
	}

	private String calcDate(long millisecs) {
		SimpleDateFormat date_format = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date resultdate = new Date(millisecs);
		return date_format.format(resultdate);
	}
}
