package com.osm2xp.utils;

/**
 * A settable IStatus. 
 * Can be an error, warning, info or ok. For error, info and warning states,
 * a message describes the problem.
 */
public class StatusInfo {
	
	public enum Severity {
		OK,
		INFO,
		WARNING,
		ERROR
	}

	public static final StatusInfo OK_STATUS = new StatusInfo();
	
	private String fStatusMessage;
	private Severity fSeverity;
	private Throwable excception;
	
	/**
	 * Creates a status set to OK (no message)
	 */
	public StatusInfo() {
		this(Severity.OK, null);
	}

	/**
	 * Creates a status .
	 * @param severity The status severity: ERROR, WARNING, INFO and OK.
	 * @param message The message of the status. Applies only for ERROR,
	 * WARNING and INFO.
	 */	
	public StatusInfo(Severity severity, String message) {
		fStatusMessage= message;
		fSeverity= severity;
	}
	
	public StatusInfo(Severity severity, String message, Throwable excception) {
		fStatusMessage= message;
		fSeverity= severity;
		this.excception = excception;
	}		
	
	/**
	 *  Returns if the status' severity is OK.
	 */
	public boolean isOK() {
		return fSeverity == Severity.OK;
	}

	/**
	 *  Returns if the status' severity is WARNING.
	 */	
	public boolean isWarning() {
		return fSeverity == Severity.WARNING;
	}

	/**
	 *  Returns if the status' severity is INFO.
	 */	
	public boolean isInfo() {
		return fSeverity == Severity.INFO;
	}	

	/**
	 *  Returns if the status' severity is ERROR.
	 */	
	public boolean isError() {
		return fSeverity == Severity.ERROR;
	}
	
	/**
	 * @see IStatus#getMessage
	 */
	public String getMessage() {
		return fStatusMessage;
	}
	
	/**
	 * Sets the status to ERROR.
	 * @param The error message (can be empty, but not null)
	 */	
	public void setError(String errorMessage) {
		fStatusMessage= errorMessage;
		fSeverity= Severity.ERROR;
	}

	/**
	 * Sets the status to WARNING.
	 * @param The warning message (can be empty, but not null)
	 */		
	public void setWarning(String warningMessage) {
		fStatusMessage= warningMessage;
		fSeverity= Severity.WARNING;
	}

	/**
	 * Sets the status to INFO.
	 * @param The info message (can be empty, but not null)
	 */		
	public void setInfo(String infoMessage) {
		fStatusMessage= infoMessage;
		fSeverity= Severity.INFO;
	}	

	/**
	 * Sets the status to OK.
	 */		
	public void setOK() {
		fStatusMessage= null;
		fSeverity= Severity.OK;
	}
	
	public Severity getSeverity() {
		return fSeverity;
	}

	public Throwable getException() {
		return excception;
	}

	/**
	 * Returns always the error severity.
	 * @see IStatus#getCode()
	 */
	public Severity getCode() {
		return fSeverity;
	}
	
	public static StatusInfo error(String message) {
		return new StatusInfo(Severity.ERROR, message);
	}
	
	public static StatusInfo warning(String message) {
		return new StatusInfo(Severity.WARNING, message);
	}
	
	public static StatusInfo info(String message) {
		return new StatusInfo(Severity.INFO, message);
	}

}
