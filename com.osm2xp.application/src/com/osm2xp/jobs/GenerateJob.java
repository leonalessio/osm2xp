package com.osm2xp.jobs;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;

import com.osm2xp.core.exceptions.Osm2xpBusinessException;
import com.osm2xp.core.logging.Osm2xpLogger;
import com.osm2xp.generation.options.FlightGearOptionsProvider;
import com.osm2xp.generation.options.FsxOptionsProvider;
import com.osm2xp.generation.options.GlobalOptionsProvider;
import com.osm2xp.generation.options.XPlaneOptionsProvider;
import com.osm2xp.utils.helpers.FlyLegacyOptionsHelper;

public abstract class GenerateJob extends Job {

	protected String family;
	protected transient File currentFile;
	protected transient String folderPath;
	
	public GenerateJob(String name, File currentFile, 
			String folderPath, String family) {
		super(name);
		this.currentFile = currentFile;
		this.folderPath = folderPath;
		this.family = family;
	}
	
	protected void init() {
		try {
			GlobalOptionsProvider.saveOptions();
			XPlaneOptionsProvider.saveOptions();
			FsxOptionsProvider.saveOptions();
			FlyLegacyOptionsHelper.saveOptions();
			FlightGearOptionsProvider.saveOptions();
		} catch (Osm2xpBusinessException e) {
			Osm2xpLogger.error(e.getMessage());
		}
	}
	
	@Override
	protected IStatus run(IProgressMonitor monitor) {
		init();
		return doGenerate(monitor);
	}

	protected abstract IStatus doGenerate(IProgressMonitor monitor);

	public boolean belongsTo(Object family) {
		return this.family.equals(family);
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String familly) {
		this.family = familly;
	}

}