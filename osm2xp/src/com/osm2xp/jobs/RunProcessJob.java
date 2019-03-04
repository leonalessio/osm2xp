package com.osm2xp.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.osm2xp.utils.MiscUtils;

/**
 * RunProcessJob.
 * 
 * @author Benjamin Blanchet
 * 
 */
public class RunProcessJob extends Job {

	private String familly;
	private final transient String command;

	public RunProcessJob(final String name, final String familly,
			final String command) {
		super(name);
		this.familly = familly;
		this.command = command;
	}

	@Override
	protected IStatus run(final IProgressMonitor monitor) {

		MiscUtils.execProgram(this.command);
		
		return Status.OK_STATUS;

	}

	public boolean belongsTo(final Object family) {
		return familly.equals(family);
	}

	public String getFamilly() {
		return familly;
	}

	public void setFamilly(final String familly) {
		this.familly = familly;
	}

}
